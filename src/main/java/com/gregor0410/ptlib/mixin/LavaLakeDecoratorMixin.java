package com.gregor0410.ptlib.mixin;

import com.gregor0410.ptlib.PTLib;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.LavaLakeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.stream.Stream;

@Mixin(LavaLakeDecorator.class)
public class LavaLakeDecoratorMixin {
    @Inject(cancellable=true,method = "getPositions(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/world/gen/decorator/ChanceDecoratorConfig;Lnet/minecraft/util/math/BlockPos;)Ljava/util/stream/Stream;",at=@At("HEAD"))
    private void onGetPositions(WorldAccess worldAccess, ChunkGenerator chunkGenerator, Random random, ChanceDecoratorConfig chanceDecoratorConfig, BlockPos blockPos, CallbackInfoReturnable<Stream<BlockPos>> cir){
        if(PTLib.getConfig().getSurfaceLavaLakeChance()==80&&PTLib.getConfig().getUndergroundLavaLakeChance()==8)return;

        boolean shouldGenerateUnderground = random.nextInt(PTLib.getConfig().getUndergroundLavaLakeChance()) == 0;
        int i = random.nextInt(16) + blockPos.getX();
        int j = random.nextInt(16) + blockPos.getZ();
        int k = random.nextInt(random.nextInt(chunkGenerator.getMaxY() - 8) + 8);
        if (k < worldAccess.getSeaLevel()) {
            if(shouldGenerateUnderground) {
                cir.setReturnValue(Stream.of(new BlockPos(i, k, j)));
                return;
            }
        }else if(random.nextInt(PTLib.getConfig().getSurfaceLavaLakeChance())==0) {
            cir.setReturnValue(Stream.of(new BlockPos(i, k, j)));
            return;
        }
        cir.setReturnValue(Stream.empty());
    }
}
