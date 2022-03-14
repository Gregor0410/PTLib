package com.gregor0410.ptlib.mixin;

import com.gregor0410.ptlib.rng.RNGState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "dropLoot",at=@At("LOAD"))
    protected LootContext.Builder modifyBuilder(LootContext.Builder builder){
        RNGState rngState = this.getServer().getOverworld().getPersistentStateManager().getOrCreate(()->new RNGState(this.getServer().getOverworld().getSeed()),"rng");
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if(livingEntity instanceof BlazeEntity){
            Random random = rngState.getRandoms()[RNGState.BLAZE];
            builder.random(random);
        }else if(livingEntity instanceof EndermanEntity){
            Random random = rngState.getRandoms()[RNGState.ENDERMAN];
            builder.random(random);
        }
        return builder;
    }
}
