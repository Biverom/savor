package com.notjang.savor.entity;

import com.notjang.savor.init.EntityInit;
import com.notjang.savor.init.ItemInit;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrackerEntity extends Animal {
    public CrackerEntity(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState crunchAnimationState = new AnimationState();

    private static final EntityDataAccessor<Boolean> IS_CRUNCHING = SynchedEntityData.defineId(CrackerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> CRUNCH_TIMER = SynchedEntityData.defineId(CrackerEntity.class, EntityDataSerializers.BYTE);

    public boolean getIsCrunching() {
        return this.getEntityData().get(IS_CRUNCHING);
    }
    public void setIsCrunching(boolean f) {
        this.getEntityData().set(IS_CRUNCHING, f);
        setCrunchTimer(f ? 40 : 0);
    }

    public int getCrunchTimer() {
        return this.getEntityData().get(CRUNCH_TIMER);
    }
    public void setCrunchTimer(int v) {
        this.getEntityData().set(CRUNCH_TIMER, (byte)v);
    }

    public Player feederPlayer = null;
    @OnlyIn(Dist.CLIENT)
    private boolean waitingForParticles = false;
    @OnlyIn(Dist.CLIENT)
    private ItemStack lastItem = ItemStack.EMPTY;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_CRUNCHING, false);
        this.getEntityData().define(CRUNCH_TIMER, (byte)0);
    }

    @Override
    public void tick() {
        if (level.isClientSide()) {
            setupAnimationStates();
            if (!getMainHandItem().isEmpty())
                lastItem = getMainHandItem();
            if (getCrunchTimer() > 15) {
                waitingForParticles = true;
            }
            else if (getMainHandItem().isEmpty() && waitingForParticles) {
                waitingForParticles = false;
                for (int i = 0; i < 16; ++i) {
                    Vec3 vec3 = (new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D)).xRot(-this.getXRot() * ((float) Math.PI / 180F)).yRot(-this.getYRot() * ((float) Math.PI / 180F));
                    this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, lastItem), this.getX() + vec3.x*5, this.getY() + vec3.y*5 + 0.5D, this.getZ() + vec3.z*5, vec3.x, vec3.y + 0.05D, vec3.z);
                }
                lastItem = ItemStack.EMPTY;
            }
        } else {
            if (!getMainHandItem().isEmpty() && getCrunchTimer() <= 0) {
                setIsCrunching(true);
            }
            if (getIsCrunching()) {
                setCrunchTimer(getCrunchTimer()-1);
                if (getCrunchTimer() <= 0) {
                    setIsCrunching(false);
                    crunchItem();
                } else if (getCrunchTimer() <= 9) {
                    if (!getMainHandItem().isEmpty())
                        crunchItem();
                }
            }
        }
        super.tick();
    }

    @Override
    public void aiStep() {
        if (!getIsCrunching())
            super.aiStep();
    }

    @Override
    public boolean hurt(@NotNull DamageSource p_27567_, float p_27568_) {
        if (this.level.isClientSide) {
            waitingForParticles = false;
            return false;
        } else {
            if (getIsCrunching()) {
                setIsCrunching(false);
                ItemStack item = getMainHandItem();
                setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                ItemEntity itementity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), item);
                this.level.addFreshEntity(itementity);
            }
        }
        return super.hurt(p_27567_, p_27568_);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!isBaby()) {
            ItemStack itemstack = player.getItemInHand(hand);
            boolean mouthIsFull = getIsCrunching();
            boolean handHasItem = !itemstack.isEmpty();
            if (mouthIsFull)
                return InteractionResult.PASS;
            if (handHasItem) {
                if (CrackerLoader.instance.uncraftItem(level, itemstack.getItem()) != null || isFood(itemstack)) {
                    ItemStack mouthItem = itemstack.copy();
                    mouthItem.setCount(1);
                    setItemSlot(EquipmentSlot.MAINHAND, mouthItem);
                    setGuaranteedDrop(EquipmentSlot.MAINHAND);
                    if (!player.getAbilities().instabuild)
                        itemstack.shrink(1);
                    feederPlayer = player;
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.FAIL;
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    public void crunchItem() {
        ItemStack item = getMainHandItem();
        if (item.isEmpty())
            return;
        setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);

        if (isFood(item)) {
            if (canFallInLove())
                setInLove(feederPlayer);
        } else if (item.is(Items.TNT) || item.is(Items.TNT_MINECART) || item.is(Items.END_CRYSTAL)) {
            level.explode(this, getX(), getY() + 0.5d, getZ(), 4.0f, Explosion.BlockInteraction.BREAK);
            hurt(DamageSource.playerAttack(feederPlayer), this.getHealth() + 1);
        } else {
            List<ItemStack> dropItems = CrackerLoader.instance.uncraftItem(level, item.getItem());
            if (dropItems != null) {
                for (ItemStack dropItem : dropItems) {
                    int count = dropItem.getCount();
                    dropItem.setCount(1);
                    for (int i = 0; i < count; i++) {
                        ItemEntity itementity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), dropItem.copy());
                        this.level.addFreshEntity(itementity);
                    }
                }
            }
        }
    }

    private void setupAnimationStates() {
        if (getIsCrunching()) {
            this.crunchAnimationState.startIfStopped(this.tickCount);
            this.idleAnimationState.stop();
            this.walkAnimationState.stop();
        } else {
            this.crunchAnimationState.stop();
            if (this.isMovingOnLand()) {
                this.walkAnimationState.startIfStopped(this.tickCount);
                this.idleAnimationState.stop();
            } else {
                this.idleAnimationState.startIfStopped(this.tickCount);
                this.walkAnimationState.stop();
            }
        }
    }

    private boolean isMovingOnLand() {
        return this.onGround && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D && !this.isInWaterOrBubble();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, Ingredient.of(ItemInit.JAWBREAKER.get()), false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D, 60));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, CrackerEntity.class, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 5)
                .add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel p_146743_, @NotNull AgeableMob p_146744_) {
        return EntityInit.CRACKER.get().create(p_146743_);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(ItemInit.JAWBREAKER.get());
    }
}
