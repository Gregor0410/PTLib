package com.gregor0410.ptlib.mixin;

import com.gregor0410.ptlib.PTLib;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.BuriedTreasureFeature;
import net.minecraft.world.gen.feature.BuriedTreasureFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BuriedTreasureFeature.class)
public class BuriedTreasureFeatureMixin {
    @Inject(cancellable = true,method="shouldStartAt(Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/source/BiomeSource;JLnet/minecraft/world/gen/ChunkRandom;IILnet/minecraft/world/biome/Biome;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/gen/feature/BuriedTreasureFeatureConfig;)Z",at=@At("RETURN"))
    private void modifyBuriedTreasureProbability(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, BuriedTreasureFeatureConfig buriedTreasureFeatureConfig, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(chunkRandom.nextFloat() < PTLib.getConfig().getBuriedTreasureProbability());
    }
}
