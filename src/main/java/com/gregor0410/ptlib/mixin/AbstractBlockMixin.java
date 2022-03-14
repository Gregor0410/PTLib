package com.gregor0410.ptlib.mixin;

import com.gregor0410.ptlib.rng.RNGState;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GravelBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
    @Shadow protected abstract Block asBlock();

    @Inject(method="getDroppedStacks",at=@At("HEAD"))
    private void onGetDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir){
        if(this.asBlock() instanceof GravelBlock){
            ServerWorld overworld = builder.getWorld().getServer().getOverworld();
            RNGState rngState = overworld.getPersistentStateManager().getOrCreate(()->new RNGState(overworld.getSeed()),"rng");
            builder.random(rngState.getRandoms()[RNGState.FLINT]);
        }
    }
}
