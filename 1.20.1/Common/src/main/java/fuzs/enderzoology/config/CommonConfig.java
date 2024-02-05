package fuzs.enderzoology.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class CommonConfig implements ConfigCore {
    @Config(description = "Should concussion creepers spawn anywhere where creepers can spawn in the overworld.")
    public boolean concussionCreeper = true;
    @Config(description = "Should infested zombies spawn anywhere where zombies can spawn in the overworld.")
    public boolean infestedZombie = true;
    @Config(description = "Should enderminies spawn anywhere where enderman can spawn in the overworld.")
    public boolean enderminy = true;
    @Config(description = "Should enderminies spawn anywhere in snowy biomes where wolves can spawn in the overworld.")
    public boolean direWolf = true;
    @Config(description = "Should wither witches spawn anywhere where witches can spawn in the overworld.")
    public boolean witherWitch = true;
    @Config(description = "Should fallen knights together with their mounts spawn anywhere where zombies can spawn in the overworld.")
    public boolean fallenKnight = true;
    @Config(description = "Should owls spawn anywhere where rabbits can spawn in the overworld.")
    public boolean owl = true;
}
