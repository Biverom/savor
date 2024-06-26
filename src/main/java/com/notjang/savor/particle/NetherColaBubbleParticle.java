package com.notjang.savor.particle;

import com.notjang.savor.init.ParticleInit;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class NetherColaBubbleParticle extends CustomParticle {

    protected NetherColaBubbleParticle(ClientLevel level, double xCoord, double yCoord, double zCoord,
                                       SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, spriteSet, xd, yd, zd);

        this.lifetime = 20;
        this.alpha = 0;
        this.rCol = 1;
        this.gCol = 1;
        this.bCol = 1;
    }

    @Override
    public void tick() {
        alpha = Math.min(Math.max(((float) age / lifetime) * 3 - 1, 0), 1);
        if (this.onGround) remove();
        super.tick();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void remove() {
        this.level.addParticle(ParticleInit.NETHER_COLA_BUBBLE_POP.get(), this.x, this.y, this.z, 0, 0, 0);
        super.remove();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType particleType, @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new NetherColaBubbleParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
