package com.gregor0410.ptlib.mixin;

import com.gregor0410.ptlib.rng.RNGState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin {
    @Shadow @Nullable protected abstract Identifier getEntityId();

    @Shadow private int spawnDelay;

    @Shadow private int maxSpawnDelay;

    @Shadow private int minSpawnDelay;

    @Shadow public abstract World getWorld();

    @Redirect(at=@At(value="FIELD",target="Lnet/minecraft/world/MobSpawnerLogic;spawnDelay:I",opcode = Opcodes.PUTFIELD),method="updateSpawns")
    private void blazeSpawnerRng(MobSpawnerLogic instance, int value){
        if(new Identifier("minecraft:blaze")==this.getEntityId()){
            ServerWorld overworld = ((ServerWorld) this.getWorld()).getServer().getOverworld();
            RNGState rngState = overworld.getPersistentStateManager().getOrCreate(()->new RNGState(overworld.getSeed()),"rng");
            this.spawnDelay = this.maxSpawnDelay <= this.minSpawnDelay ? this.minSpawnDelay : this.minSpawnDelay + rngState.getRandoms()[RNGState.BLAZE_SPAWN].nextInt(this.maxSpawnDelay - this.minSpawnDelay);
        }
    }
}
