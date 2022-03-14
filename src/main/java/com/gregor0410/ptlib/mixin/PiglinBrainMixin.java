package com.gregor0410.ptlib.mixin;

import com.gregor0410.ptlib.rng.RNGState;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    private static PiglinEntity piglinEntity;
    @Inject(method="getBarteredItem",at=@At("HEAD"))
    private static void getPiglin(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir){
        piglinEntity = piglin;
    }
    @Redirect(method="getBarteredItem",at=@At(value="INVOKE",target = "Lnet/minecraft/loot/context/LootContext$Builder;random(Ljava/util/Random;)Lnet/minecraft/loot/context/LootContext$Builder;"))
    private static LootContext.Builder modifyPiglinRandom(LootContext.Builder instance, Random random){
        ServerWorld overworld = piglinEntity.getServer().getOverworld();
        RNGState rngState = overworld.getPersistentStateManager().getOrCreate(()->new RNGState(overworld.getSeed()),"rng");
        return instance.random(rngState.getRandoms()[RNGState.BARTER]);
    }
}
