package com.gregor0410.ptlib.mixin;

import com.gregor0410.ptlib.rng.RNGState;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.HoldingPatternPhase;
import net.minecraft.server.world.ServerWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Random;

@Mixin(HoldingPatternPhase.class)
public abstract class HoldingPatternPhaseMixin extends AbstractPhase {
    public HoldingPatternPhaseMixin(EnderDragonEntity dragon) {
        super(dragon);
    }

    @Redirect(method="method_6841",at=@At(value="INVOKE",target="Ljava/util/Random;nextInt(I)I"),slice=@Slice(from=@At("HEAD"),to=@At(value="JUMP",opcode= Opcodes.IFEQ,ordinal = 0)))
    private int perchRng(Random random,int bound){
        int i = this.dragon.getFight() == null ? 0 : this.dragon.getFight().getAliveEndCrystals();
        ServerWorld overworld = this.dragon.getServer().getOverworld();
        RNGState rngState = overworld.getPersistentStateManager().getOrCreate(()->new RNGState(overworld.getSeed()),"rng");
        if(rngState.getRandoms()[RNGState.PERCH].nextFloat()<=1f/(i+3)) {
            return 0;
        }else{
            return 1;
        }
    }
}
