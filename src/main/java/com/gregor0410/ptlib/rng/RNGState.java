package com.gregor0410.ptlib.rng;

import com.gregor0410.ptlib.PTLib;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RNGState extends PersistentState {
    public static final int BLAZE = 0;
    public static final int BLAZE_SPAWN = 1;
    public static final int BARTER = 2;
    public static final int ENDERMAN = 3;
    public static final int FLINT = 4;
    public static final int EYE = 5;
    public static final int PERCH = 6;
    private final AccessibleRandom[] randoms = new AccessibleRandom[]{new AccessibleRandom(),new AccessibleRandom(),new AccessibleRandom(),new AccessibleRandom(),new AccessibleRandom(),new AccessibleRandom(),new AccessibleRandom()};

    public RNGState(long seed){
        super("rng");
        seed+=0xfe09efad; //this number isn't special I just picked a random number to add to prevent divine
        if(PTLib.getConfig().isSeedBasedRng()){
            for(int i=0;i< randoms.length;i++){
                randoms[i].seed.set(AccessibleRandom.initialScramble(seed+i)); //initial scramble so seed based rng can be predicted with the base Java Random class
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        List<Long> seeds = Arrays.stream(randoms).map(AccessibleRandom::getSeed).collect(Collectors.toList());
        tag.putLongArray("seeds",seeds);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        long[] seeds = tag.getLongArray("seeds");
        for(int i=0; i< seeds.length;i++){
            randoms[i].seed.set(seeds[i]);
        }
    }

    public AccessibleRandom[] getRandoms() {
        this.setDirty(true);
        return randoms;
    }
}
