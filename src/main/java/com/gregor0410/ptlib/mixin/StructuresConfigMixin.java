package com.gregor0410.ptlib.mixin;

import com.gregor0410.ptlib.PTLib;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(StructuresConfig.class)
public class StructuresConfigMixin {
    @Mutable @Shadow @Final private Map<StructureFeature<?>, StructureConfig> structures;

    @Redirect(method = "<init>(Z)V",at=@At(value="FIELD",target ="Lnet/minecraft/world/gen/chunk/StructuresConfig;structures:Ljava/util/Map;",opcode = Opcodes.PUTFIELD))
    private void modifyStructures(StructuresConfig sc, Map<StructureFeature<?>, StructureConfig> structures){
        this.structures = PTLib.getConfig().getOverworldStructures();
    }
}
