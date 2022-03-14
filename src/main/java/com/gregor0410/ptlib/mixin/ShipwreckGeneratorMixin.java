package com.gregor0410.ptlib.mixin;

import com.gregor0410.ptlib.PTLib;
import net.minecraft.structure.ShipwreckGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(ShipwreckGenerator.class)
public class ShipwreckGeneratorMixin {
    @Redirect(method="addParts",at=@At(value="INVOKE",target = "Lnet/minecraft/util/Util;getRandom([Ljava/lang/Object;Ljava/util/Random;)Ljava/lang/Object;"))
    private static <T> T modifyShipwreckTemplates(T[] array, Random random){
        if(PTLib.getConfig().isDisableBadShipwrecks()) {
            Identifier[] identifiers = (Identifier[]) array;
            List<Identifier> goodShipwrecks = new ArrayList<>();
            for (Identifier identifier : identifiers) {
                if (!PTLib.BAD_SHIPWRECKS.contains(identifier)) {
                    goodShipwrecks.add(identifier);
                }
            }
            return (T) Util.getRandom(goodShipwrecks.toArray(), random);
        }else{
            return Util.getRandom(array, random);
        }
    }
}
