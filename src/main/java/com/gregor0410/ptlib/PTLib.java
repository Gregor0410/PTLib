package com.gregor0410.ptlib;

import com.google.common.collect.Lists;
import com.gregor0410.ptlib.config.PTConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.util.List;


public class PTLib implements ModInitializer {
    private static PTConfig config = new PTConfig();
    public static final List<Identifier> BAD_SHIPWRECKS = Lists.newArrayList(new Identifier("shipwreck/upsidedown_fronthalf"), new Identifier("shipwreck/sideways_fronthalf"), new Identifier("shipwreck/rightsideup_fronthalf"), new Identifier("shipwreck/upsidedown_fronthalf_degraded"), new Identifier("shipwreck/sideways_fronthalf_degraded"), new Identifier("shipwreck/rightsideup_fronthalf_degraded"));

    public static PTConfig getConfig() {
        return config;
    }


    @Override
    public void onInitialize() {
        setConfig(new PTConfig().setSeedBasedRng(true));
    }

    public static void setConfig(PTConfig config){
        PTLib.config = PTLib.config.config(config);
    }


    public enum DragonType{
        FRONT,
        BACK,
        BOTH
    }
}
