package com.gregor0410.ptlib.mixin;

import com.gregor0410.ptlib.rng.RNGState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EyeOfEnderEntity.class)
public abstract class EyeOfEnderEntityMixin extends Entity {
    @Shadow private boolean dropsItem;

    public EyeOfEnderEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method="moveTowards",at=@At("TAIL"))
    private void onMoveTowards(BlockPos pos, CallbackInfo ci){
        ServerWorld overworld = ((ServerWorld) this.world).getServer().getOverworld();
        RNGState rngState = overworld.getPersistentStateManager().getOrCreate(()->new RNGState(overworld.getSeed()),"rng");
        this.dropsItem = rngState.getRandoms()[RNGState.EYE].nextInt(5) > 0;
    }
}
