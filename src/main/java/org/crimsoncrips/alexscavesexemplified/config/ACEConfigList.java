package org.crimsoncrips.alexscavesexemplified.config;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LavaCauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraftforge.common.ForgeConfigSpec;

public class ACEConfigList {



    public final ForgeConfigSpec.BooleanValue GLUTTONY_ENABLED;
    public final ForgeConfigSpec.BooleanValue REGULAR_REFERENCE_ENABLED;

    public final ForgeConfigSpec.BooleanValue REDOABLE_SPELUNKY_ENABLED;

    public final ForgeConfigSpec.BooleanValue AMPUTATION_ENABLED;
    public final ForgeConfigSpec.IntValue EXEMPLIFIED_IRRADIATION_AMOUNT;


    public final ForgeConfigSpec.BooleanValue FORLORN_LIGHT_EFFECT_ENABLED;

    public final ForgeConfigSpec.BooleanValue BURST_OUT_ENABLED;
    public final ForgeConfigSpec.BooleanValue CORRODENT_CONVERSION_ENABLED;


    public final ForgeConfigSpec.BooleanValue NUCLEAR_PISTONATION_ENABLED;






    public final ForgeConfigSpec.BooleanValue HIVE_MIND_ENABLED;

    public final ForgeConfigSpec.BooleanValue STABILIZER_COMPATIBILITY_ENABLED;
    public final ForgeConfigSpec.BooleanValue ANTI_SACRIFICE_ENABLED;

    public final ForgeConfigSpec.BooleanValue VESPER_CANNIBALIZE_ENABLED;

    public final ForgeConfigSpec.BooleanValue VANILLA_ADAPTIONS_ENABLED;
    public final ForgeConfigSpec.BooleanValue JELLYBEAN_CHANGES_ENABLED;


    public final ForgeConfigSpec.BooleanValue FLY_TRAP_ENABLED;

    public final ForgeConfigSpec.BooleanValue RATATATATATA_ENABLED;
    public final ForgeConfigSpec.BooleanValue GEOTHERMAL_EFFECTS_ENABLED;
    public final ForgeConfigSpec.BooleanValue RABIES_ENABLED;

    public final ForgeConfigSpec.BooleanValue AMPLIFIED_FROSTMINT_ENABLED;
    public final ForgeConfigSpec.BooleanValue SOLIDIFIED_ENABLED;
    public final ForgeConfigSpec.BooleanValue CANIAC_SENSITIVITY_ENABLED;
    public final ForgeConfigSpec.BooleanValue IRRADIATION_WASHOFF_ENABLED;
    public final ForgeConfigSpec.BooleanValue GAMMAROACH_FOODING_ENABLED;

    public final ForgeConfigSpec.DoubleValue CHARGED_CAVE_CREEPER_CHANCE;
    public final ForgeConfigSpec.BooleanValue RESISTOR_MAGNETISM;
    public final ForgeConfigSpec.BooleanValue PURPLE_LEATHERED_ENABLED;

    public final ForgeConfigSpec.BooleanValue STICKY_SODA_ENABLED;

    public final ForgeConfigSpec.IntValue SPELUNKY_ATTEMPTS_AMOUNT;
    



    public ACEConfigList(final ForgeConfigSpec.Builder builder) {
        builder.push("General");
         this.REDOABLE_SPELUNKY_ENABLED = buildBoolean(builder, "REDOABLE_SPELUNKY_ENABLED", " ", true, "Whether it gives you back the tablet when exiting the spelunky table");
         this.SPELUNKY_ATTEMPTS_AMOUNT = buildInt(builder, "SPELUNKY_ATTEMPTS_AMOUNT", " ", 5, 1, Integer.MAX_VALUE, "Amount of tries you get for the spelunky table per tablet");
         this.VANILLA_ADAPTIONS_ENABLED = buildBoolean(builder, "VANILLA_ADAPTIONS_ENABLED", " ", true, "Whether vanilla enchantments adapts to weapons (ex.Dreadbow, etc..)");
         this.CHARGED_CAVE_CREEPER_CHANCE = buildDouble(builder, "CHARGED_CAVE_CREEPER_CHANCE", " ", 0.2,0.0,1.0, "Chances of creepers from caves to be charged (0 for disable)");
        this.PURPLE_LEATHERED_ENABLED = buildBoolean(builder, "PURPLE_LEATHERED_ENABLED", " ", true, "Whether purple soda, purples non-dyed leather armor");
        this.STICKY_SODA_ENABLED = buildBoolean(builder, "STICKY_SODA_ENABLED", " ", true, "Whether purple soda causes stickyness when in it");


        builder.pop();
        builder.push("Candy Cavity");
        this.GLUTTONY_ENABLED = buildBoolean(builder, "GLUTTONY_ENABLED", " ", true, "Whether blocks inside candy cavity, can be eaten by right clicking");
        this.AMPLIFIED_FROSTMINT_ENABLED = buildBoolean(builder, "AMPLIFIED_FROSTMINT_ENABLED", " ", true, "Item and spear entity explodes as well in Purple Soda");
        this.SOLIDIFIED_ENABLED = buildBoolean(builder, "SOLIDIFIED_ENABLED", " ", true, "Frostmint Spears causes liquid blocks to freeze at the cost of the spear");

        builder.push("Caniac");
        this.CANIAC_SENSITIVITY_ENABLED = buildBoolean(builder, "CANIAC_SENSITIVITY_ENABLED", " ", true, "Whether Caniac dissolve in water and will actively avoid it");
         builder.pop();
        builder.push("Gingerbread Man");
        this.AMPUTATION_ENABLED = buildBoolean(builder, "AMPUTATION_ENABLED", " ", true, "Whether Gingerbread Men can be amputated with an axe");
        this.HIVE_MIND_ENABLED = buildBoolean(builder, "HIVE_MIND_ENABLED", " ", true, "Whether Gingerbread Men will attack you if you attack one of them.");
        builder.pop();
        builder.push("Gumbeeper");
        this.REGULAR_REFERENCE_ENABLED = buildBoolean(builder, "REGULAR_REFERENCE_ENABLED", " ", true, "Whether Gumbeepers will attack, Blue jays and Raccoons in Alexs Mobs");
        builder.pop();
        builder.push("Gummy Bear");
        this.JELLYBEAN_CHANGES_ENABLED = buildBoolean(builder, "JELLYBEAN_CHANGES_ENABLED", " ", true, "Jellybean harvesting is changed where amount of jellybeans is made the longer they hibernate");
        builder.pop();
        
        

        builder.pop();
        builder.push("Forlorn Hollows");
        this.FORLORN_LIGHT_EFFECT_ENABLED = buildBoolean(builder, "FORLORN_LIGHT_EFFECT_ENABLED", " ", true, "Whether most forlorn mammals are effected when a player holds light");
        this.BURST_OUT_ENABLED = buildBoolean(builder, "BURST_OUT_ENABLED", " ", true, "Whether breaking Forlorn Hollows blocks has a chance to burst out Underzealots or Corrodents");
        builder.push("Corrodent");
        this.CORRODENT_CONVERSION_ENABLED = buildBoolean(builder, "CORRODENT_CONVERSION_ENABLED", " ", true, "Whether corrodents can be converted to underzealots through ritual");
        this.RABIES_ENABLED = buildBoolean(builder, "RABIES_ENABLED", " ", true, "Whether corrodents inflict rabies-like symptoms");
        builder.pop();
        builder.push("Vesper");
        this.ANTI_SACRIFICE_ENABLED = buildBoolean(builder, "ANTI_SACRIFICE_ENABLED", " ", true, "Whether vespers attack underzealots sacrificing vespers");

        this.VESPER_CANNIBALIZE_ENABLED = buildBoolean(builder, "VESPER_CANNIBALIZE_ENABLED", " ", true, "Whether vespers attack bats wondering by");
        builder.pop();
        builder.push("Watcher");
        this.STABILIZER_COMPATIBILITY_ENABLED = buildBoolean(builder, "STABILIZER_COMPATIBILITY_ENABLED", " ", true, "Whether the Stabilizer enchantment from Alexs Mobs Interaction negates possession from the Watcher");
        builder.pop();
        builder.pop();

        builder.push("Toxic Caves");
        this.EXEMPLIFIED_IRRADIATION_AMOUNT = buildInt(builder, "EXEMPLIFIED_IRRADIATION_AMOUNT", " ", 3, 0, Integer.MAX_VALUE, "Amount of irradiation level to get the deadly effects");

        this.IRRADIATION_WASHOFF_ENABLED = buildBoolean(builder, "IRRADIATION_WASHOFF_ENABLED", " ", true, "Whether Irradiation wears off faster when in water");
        this.GEOTHERMAL_EFFECTS_ENABLED = buildBoolean(builder, "GEOTHERMAL_EFFECTS_ENABLED", " ", true, "Whether Geothermal Vents have effects when standing on top depending on the spewed smoke");

        builder.push("Gammaroach");
        this.GAMMAROACH_FOODING_ENABLED = buildBoolean(builder, "GAMMAROACH_FOODING_ENABLED", " ", true, "Whether gammaroaches hunt dropped food");
        builder.pop();
        builder.pop();

        builder.push("Primordial Caves");
        this.FLY_TRAP_ENABLED = buildBoolean(builder, "FLY_TRAP_ENABLED", " ", true, "Flytraps close shut when a fly (From Alexs Mobs) comes into contact with it");
        builder.pop();


        builder.push("Magnetic Caves");
        this.RESISTOR_MAGNETISM = buildBoolean(builder, "RESISTOR_MAGNETISM", " ", true, "Resistor Shield attracts/repels magnetic items, or more..");


        builder.pop();
        builder.push("Goofy Mode");
        this.RATATATATATA_ENABLED = buildBoolean(builder, "RATATATATATA_ENABLED", " ", false, "Whether Relentless Darkness dreadbow has no cooldown on its firing");
        this.NUCLEAR_PISTONATION_ENABLED = buildBoolean(builder, "NUCLEAR_PISTONATION_ENABLED", " ", false, "Whether Nuclear Bombs can be pushed by piston (therefore duplicatable)");


    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, String catagory, double defaultValue, double min, double max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
