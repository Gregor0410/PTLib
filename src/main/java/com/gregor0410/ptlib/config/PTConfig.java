package com.gregor0410.ptlib.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.gregor0410.ptlib.PTLib;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PTConfig {

    private boolean bridge=true;
    private boolean housing=true;
    private boolean treasure=true;
    private boolean stables=true;
    private int bastionRarity=60;
    private transient List<StructurePoolFeatureConfig> possibleBastionConfigs = Lists.newArrayList(
            new StructurePoolFeatureConfig(new Identifier("bastion/units/base"),60),
            new StructurePoolFeatureConfig(new Identifier("bastion/hoglin_stable/origin"),60),
            new StructurePoolFeatureConfig(new Identifier("bastion/treasure/starters"),60),
            new StructurePoolFeatureConfig(new Identifier("bastion/bridge/start"),60));
    private transient Map<StructureFeature<?>, StructureConfig> netherStructures = Maps.newHashMap(StructuresConfig.DEFAULT_STRUCTURES);
    private transient Map<StructureFeature<?>, StructureConfig> overworldStructures = Maps.newHashMap(StructuresConfig.DEFAULT_STRUCTURES);
    private Map<String,PTConfig.StructureRegion> structureRegions = new HashMap<>();
    private PTLib.DragonType dragonType = PTLib.DragonType.BOTH;
    private List<Boolean> endTowers = Lists.newArrayList(true,true,true,true,true,true,true,true,true,true);
    private boolean eliminateCageSpawns = false;
    private float buriedTreasureProbability = 0.01f;
    private boolean disableBadShipwrecks = false;
    private int surfaceLavaLakeChance = 80;
    private int undergroundLavaLakeChance = 8;
    private boolean seedBasedRng = false;

    public PTConfig(){
        netherStructures.put(StructureFeature.RUINED_PORTAL, new StructureConfig(25, 10, 34222645));
    }

    public PTConfig config(PTConfig config){
        config.possibleBastionConfigs = this.possibleBastionConfigs;
        config.overworldStructures = this.overworldStructures;
        config.netherStructures = this.netherStructures;
        config.update();
        return config;
    }

    private void resetStructures() {
        this.netherStructures.clear();
        this.netherStructures.putAll(StructuresConfig.DEFAULT_STRUCTURES);
        this.netherStructures.put(StructureFeature.RUINED_PORTAL, new StructureConfig(25, 10, 34222645));
        this.overworldStructures.clear();
        this.overworldStructures.putAll(StructuresConfig.DEFAULT_STRUCTURES);
        possibleBastionConfigs.clear();
    }

    private void update(){
        resetStructures();
        structureRegions.forEach((string,structureRegion)->{
            if(string.equalsIgnoreCase("nether_ruined_portal")){
                netherStructures.put(StructureFeature.RUINED_PORTAL,structureRegion.toStructureConfig());
            }else {
                StructureFeature<?> structureFeature = Registry.STRUCTURE_FEATURE.get(new Identifier(string));
                overworldStructures.put(structureFeature,structureRegion.toStructureConfig());
                if(!string.equalsIgnoreCase("ruined_portal")) netherStructures.put(structureFeature,structureRegion.toStructureConfig());
            }
        });
        if(housing) possibleBastionConfigs.add(new StructurePoolFeatureConfig(new Identifier("bastion/units/base"),60));
        if(stables) possibleBastionConfigs.add(new StructurePoolFeatureConfig(new Identifier("bastion/hoglin_stable/origin"),60));
        if(treasure) possibleBastionConfigs.add(new StructurePoolFeatureConfig(new Identifier("bastion/treasure/starters"),60));
        if(bridge) possibleBastionConfigs.add(new StructurePoolFeatureConfig(new Identifier("bastion/bridge/start"),60));
    }

    public static PTConfig fromJson(JsonElement json){
        Gson gson = new Gson();
        return gson.fromJson(json,PTConfig.class);
    }

    public static PTConfig fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json,PTConfig.class);
    }

    public static PTConfig fromFile(Path path){
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(path);
            Gson gson = new Gson();
            PTConfig config = gson.fromJson(reader,PTConfig.class);
            reader.close();
            return config;
        } catch (IOException e) {
            return new PTConfig();
        }
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public List<StructurePoolFeatureConfig> getPossibleBastionConfigs() {
        return possibleBastionConfigs;
    }

    public Map<StructureFeature<?>, StructureConfig> getNetherStructures() {
        return netherStructures;
    }

    public Map<StructureFeature<?>, StructureConfig> getOverworldStructures() {
        return overworldStructures;
    }

    public boolean isBridge() {
        return bridge;
    }

    public PTConfig setBridge(boolean bridge) {
        this.bridge = bridge;
        this.update();
        return this;
    }

    public boolean isHousing() {
        return housing;
    }

    public PTConfig setHousing(boolean housing) {
        this.housing = housing;
        this.update();
        return this;
    }

    public boolean isTreasure() {
        return treasure;
    }

    public PTConfig setTreasure(boolean treasure) {
        this.treasure = treasure;
        this.update();
        return this;
    }

    public boolean isStables() {
        return stables;
    }

    public PTConfig setStables(boolean stables) {
        this.stables = stables;
        this.update();
        return this;
    }

    public int getBastionRarity() {
        return bastionRarity;
    }

    public PTConfig setBastionRarity(int bastionRarity) {
        if(bastionRarity<0 || bastionRarity>100) throw new IllegalArgumentException("Bastion Rarity should be between 0 and 100");
        this.bastionRarity = bastionRarity;
        return this;
    }

    public Map<String, StructureRegion> getStructureRegions() {
        return structureRegions;
    }

    public PTConfig setStructureRegions(Map<String, StructureRegion> structureRegions) {
        this.structureRegions = structureRegions;
        this.update();
        return this;
    }

    public PTLib.DragonType getDragonType() {
        return dragonType;
    }

    public PTConfig setDragonType(PTLib.DragonType dragonType) {
        this.dragonType = dragonType;
        return this;
    }

    public List<Boolean> getEndTowers() {
        return endTowers;
    }

    public PTConfig setEndTowers(List<Boolean> endTowers) {
        if(endTowers.size()!=10) throw new IllegalArgumentException("End towers should be list of size 10");
        this.endTowers = endTowers;
        return this;
    }

    public boolean isEliminateCageSpawns() {
        return eliminateCageSpawns;
    }

    public PTConfig setEliminateCageSpawns(boolean eliminateCageSpawns) {
        this.eliminateCageSpawns = eliminateCageSpawns;
        return this;
    }

    public float getBuriedTreasureProbability() {
        return buriedTreasureProbability;
    }

    public PTConfig setBuriedTreasureProbability(float buriedTreasureProbability) {
        if(buriedTreasureProbability > 1 || buriedTreasureProbability < 0) throw new IllegalArgumentException("Buried treasure probability should be between 0 and 1");
        this.buriedTreasureProbability = buriedTreasureProbability;
        return this;
    }

    public boolean isDisableBadShipwrecks() {
        return disableBadShipwrecks;
    }

    public PTConfig setDisableBadShipwrecks(boolean disableBadShipwrecks) {
        this.disableBadShipwrecks = disableBadShipwrecks;
        return this;
    }

    public int getSurfaceLavaLakeChance() {
        return surfaceLavaLakeChance;
    }

    public PTConfig setSurfaceLavaLakeChance(int surfaceLavaLakeChance) {
        if(surfaceLavaLakeChance <0) throw new IllegalArgumentException("Lava Lake chance must be positive");
        this.surfaceLavaLakeChance = surfaceLavaLakeChance;
        return this;
    }

    public boolean isSeedBasedRng() {
        return seedBasedRng;
    }

    public PTConfig setSeedBasedRng(boolean seedBasedRng) {
        this.seedBasedRng = seedBasedRng;
        return this;
    }

    public int getUndergroundLavaLakeChance() {
        return undergroundLavaLakeChance;
    }

    public PTConfig setUndergroundLavaLakeChance(int undergroundLavaLakeChance) {
        this.undergroundLavaLakeChance = undergroundLavaLakeChance;
        return this;
    }

    public static class StructureRegion{
        public int spacing=1;
        public int separation=0;
        public int salt=0;

        public StructureRegion(int spacing, int separation, int salt){
            if(spacing<1) throw new IllegalArgumentException("Spacing should be at least 1");
            if(separation<0) throw new IllegalArgumentException("Separation should be positive");
            this.spacing = spacing;
            this.separation = separation;
            this.salt = salt;
        }
        public StructureConfig toStructureConfig(){
            return new StructureConfig(spacing,separation,salt);
        }
    }

}
