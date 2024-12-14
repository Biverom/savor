package com.notjang.savor.proxy;

import com.notjang.savor.effect.AcidPiercingMobEffect;
import com.notjang.savor.effect.CarbonationMobEffect;
import com.notjang.savor.effect.TenacityWallJump;
import com.notjang.savor.entity.CrackerLoader;
import com.notjang.savor.entity.CrackerEntity;
import com.notjang.savor.init.*;
import com.notjang.savor.network.NMSetAbsoluteLevel;
import com.notjang.savor.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

public class CommonProxy {

    public void start() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        EntityInit.ENTITIES.register(bus);
        MobEffectInit.MOB_EFFECTS.register(bus);
        SoundEffectInit.SOUND_EVENTS.register(bus);
        ParticleInit.PARTICLE_TYPES.register(bus);
        RecipeInit.RECIPE_SERIALIZERS.register(bus);
        MinecraftForge.EVENT_BUS.register(this);

        bus.addListener(this::registerAttributes);
    }

    public void setupCommon() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setupClient() {

    }

    public static final HashMap<UUID, Integer> absoluteCooldowns = new HashMap<>();

    public static boolean isAbsoluteCooldown(LivingEntity entity) {
        int cd = 0;
        if (absoluteCooldowns.containsKey(entity.getUUID())) {
            cd = absoluteCooldowns.get(entity.getUUID());
        }
        return cd > 0;
    }

    public static void setAbsoluteCooldown(LivingEntity entity, int cd) {
        absoluteCooldowns.put(entity.getUUID(), cd);
    }

    public static int getAbsoluteLevel(LivingEntity entity) {
        int level = -1;
        if ((entity != null) && (entity.hasEffect(MobEffectInit.ABSOLUTE.get()))) {
            level = Objects.requireNonNull(entity.getEffect(MobEffectInit.ABSOLUTE.get())).getAmplifier();
        }
        return level;
    }

    public static void setAbsoluteLevel(LivingEntity entity, int level, boolean direct) {
        if (entity != null) {
            int duration = 0;
            if (entity.hasEffect(MobEffectInit.ABSOLUTE.get())) {
                duration = Objects.requireNonNull(entity.getEffect(MobEffectInit.ABSOLUTE.get())).getDuration();
                entity.removeEffect(MobEffectInit.ABSOLUTE.get());
            }
            SoundEvent sound = (level > -1) ? SoundEffectInit.EFFECT_ABSOLUTE_EVADE.get() : SoundEffectInit.EFFECT_ABSOLUTE_EVADE_LAST.get();
            entity.playSound(sound, 1f, 1f);
            if (level > -1)
                entity.addEffect(new MobEffectInstance(MobEffectInit.ABSOLUTE.get(), duration, level));
            entity.invulnerableTime = 10;
            setAbsoluteCooldown(entity, 20);
        }
    }

    public static int getTenacityLevel(LivingEntity entity) {
        int level = -1;
        if ((entity != null) && (entity.hasEffect(MobEffectInit.TENACITY.get()))) {
            level = Objects.requireNonNull(entity.getEffect(MobEffectInit.TENACITY.get())).getAmplifier();
        }
        return level;
    }

    public static int getCarbonationLevel(LivingEntity entity) {
        int level = -1;
        if ((entity != null) && (entity.hasEffect(MobEffectInit.CARBONATION.get()))) {
            level = Objects.requireNonNull(entity.getEffect(MobEffectInit.CARBONATION.get())).getAmplifier();
        }
        return level;
    }

    @SubscribeEvent
    public void livingAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();
        if (isAbsoluteCooldown(entity)) {
            event.setCanceled(true);
        }
        if (!entity.isInvulnerable()) {
            int level = getAbsoluteLevel(entity);
            if (level > -1) {
                event.setCanceled(true);
                if (!isAbsoluteCooldown(entity)) {
                    if (entity instanceof Player) {
                        if (entity instanceof ServerPlayer serverPlayer) {
                            setAbsoluteLevel(entity, level - 1, true);
                            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new NMSetAbsoluteLevel(level - 1));
                        }
                    }
                    else {
                        setAbsoluteLevel(entity, level - 1, true);
                    }
                }
            }
        }
        if (event.getSource().getDirectEntity() instanceof Projectile) {
            LivingEntity target = event.getEntity();
            if (target.hasEffect(MobEffectInit.CARBONATION.get())) {
                Projectile projectile = (Projectile) event.getSource().getDirectEntity();

                double motionX = -projectile.getDeltaMovement().x;
                double motionY = -projectile.getDeltaMovement().y;
                double motionZ = -projectile.getDeltaMovement().z;
                projectile.setDeltaMovement(-motionX * 2, -motionY * 2, -motionZ * 2);
                //CarbonationMobEffect.pushEntityConstant(entity, projectile, 10, true);

                projectile.setPos(projectile.getX() + (motionX * 1), projectile.getY() + (motionY * 1), projectile.getZ() + (motionZ * 1));

                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (attacker.hasEffect(MobEffectInit.ACID_PIERCING.get())) {
                float newDamage = AcidPiercingMobEffect.calculateArmorPiercingDamage(attacker, event.getEntity(), event.getAmount());
                System.out.println(event.getAmount());
                System.out.println(newDamage);
                event.setAmount(newDamage);
            }
        }
    }

    public static final UUID foodFlavorAttributeModifierUUID = Mth.createInsecureUUID(RandomSource.createNewThreadLocalInstance());

    @SubscribeEvent
    public void livingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null) return;

        if (!(entity instanceof Player)) TenacityWallJump.tickEntity(entity, getTenacityLevel(entity) >= 0);

        if (entity.hasEffect(MobEffectInit.CARBONATION.get())) {
            CarbonationMobEffect.pushEntityGroup(entity, LivingEntity.class, 0.15);
            //CarbonationMobEffect.pushEntityGroup(entity, Projectile.class, 0.5);
        }
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        Iterator<Map.Entry<UUID, Integer>> iterator = absoluteCooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            System.out.println(entry);
            int newValue = entry.getValue() - 1;
            if (newValue >= 0) {
                entry.setValue(newValue);
            } else {
                iterator.remove();
            }
        }
    }

    @SubscribeEvent
    public void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityInit.CRACKER.get(), CrackerEntity.createAttributes().build());
    }

    @SubscribeEvent
    public void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(new CrackerLoader());
    }
}
