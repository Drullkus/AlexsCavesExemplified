package org.crimsoncrips.alexscavesexemplified.config;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LavaCauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraftforge.common.ForgeConfigSpec;

public class ACEConfigList {
    public final ForgeConfigSpec.BooleanValue GLUTTONY_ENABLED;
    public final ForgeConfigSpec.BooleanValue REGULAR_REFERENCE_ENABLED;
    public final ForgeConfigSpec.BooleanValue REDOABLE_SPELUNKY_ENABLED;
    public final ForgeConfigSpec.BooleanValue KNAWING_ENABLED;
    public final ForgeConfigSpec.BooleanValue AMPUTATION_ENABLED;
    public final ForgeConfigSpec.IntValue EXEMPLIFIED_IRRADIATION_AMOUNT;
    public final ForgeConfigSpec.BooleanValue FORLORN_LIGHT_EFFECT_ENABLED;
    public final ForgeConfigSpec.BooleanValue BURST_OUT_ENABLED;
    public final ForgeConfigSpec.BooleanValue CORRODENT_CONVERSION_ENABLED;
    public final ForgeConfigSpec.BooleanValue CRUMBY_RAGE_ENABLED;
    public final ForgeConfigSpec.BooleanValue NUCLEAR_PISTONATION_ENABLED;
    public final ForgeConfigSpec.BooleanValue HIVE_MIND_ENABLED;
    public final ForgeConfigSpec.BooleanValue STABILIZER_COMPATIBILITY_ENABLED;
    public final ForgeConfigSpec.BooleanValue ANTI_SACRIFICE_ENABLED;
    public final ForgeConfigSpec.BooleanValue VESPER_HUNT_ENABLED;
    public final ForgeConfigSpec.BooleanValue VANILLA_ADAPTIONS_ENABLED;
    public final ForgeConfigSpec.BooleanValue JELLYBEAN_CHANGES_ENABLED;
    public final ForgeConfigSpec.BooleanValue SWEETISH_SPEEDUP_ENABLED;
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
    public final ForgeConfigSpec.BooleanValue RESISTOR_MAGNETISM_ENABLED;
    public final ForgeConfigSpec.BooleanValue PURPLE_LEATHERED_ENABLED;
    public final ForgeConfigSpec.BooleanValue FISH_MUTATION_ENABLED;
    public final ForgeConfigSpec.BooleanValue STICKY_CARAMEL_ENABLED;
    public final ForgeConfigSpec.BooleanValue STICKY_SODA_ENABLED;
    public final ForgeConfigSpec.BooleanValue PRIMORDIAL_OXYGEN_ENABLED;
    public final ForgeConfigSpec.BooleanValue VOLCANIC_SACRIFICE_ENABLED;
    public final ForgeConfigSpec.BooleanValue RADIANT_WRATH_ENABLED;
    public final ForgeConfigSpec.BooleanValue PRESSURED_HOOKS_ENABLED;
    public final ForgeConfigSpec.BooleanValue LOGICAL_RIDING_ENABLED;
    public final ForgeConfigSpec.BooleanValue FEARED_ANCESTORS_ENABLED;
    public final ForgeConfigSpec.BooleanValue DINOSAUR_EGG_ANGER_ENABLED;
    public final ForgeConfigSpec.BooleanValue IRRADIATED_CREEPER_ENABLED;
    public final ForgeConfigSpec.BooleanValue NUCLEAR_CHAIN_ENABLED;
    public final ForgeConfigSpec.BooleanValue MUTATED_DEATH_ENABLED;
    public final ForgeConfigSpec.BooleanValue WASTE_PICKUP_ENABLED;
    public final ForgeConfigSpec.BooleanValue CAT_MUTATION_ENABLED;
    public final ForgeConfigSpec.BooleanValue DARKNESS_APPLYED_ENABLED;
    public final ForgeConfigSpec.BooleanValue DISORIENTED_ENABLED;
    public final ForgeConfigSpec.BooleanValue UNDERZEALOT_RESPECT_ENABLED;
    public final ForgeConfigSpec.BooleanValue SCAVENGING_ENABLED;
    public final ForgeConfigSpec.BooleanValue AMBER_HEAL_ENABLED;
    public final ForgeConfigSpec.BooleanValue VESPER_SHOTDOWN_ENABLED;
    public final ForgeConfigSpec.BooleanValue GUANO_SLOW_ENABLED;
    public final ForgeConfigSpec.BooleanValue ADDITIONAL_FLAMMABILITY_ENABLED;
    public final ForgeConfigSpec.BooleanValue STOMP_DAMAGE_ENABLED;
    public final ForgeConfigSpec.BooleanValue PAINT_EFFECTS_ENABLED;
    public final ForgeConfigSpec.BooleanValue WASTE_POWERUP_ENABLED;
    public final ForgeConfigSpec.BooleanValue KIROV_REPORTING_ENABLED;
    public final ForgeConfigSpec.IntValue SPELUNKY_ATTEMPTS_AMOUNT;
    public final ForgeConfigSpec.BooleanValue EXTINGUISH_CAMPFIRES_ENABLED;
    public final ForgeConfigSpec.BooleanValue BEDWARS_ENABLED;
    public final ForgeConfigSpec.BooleanValue CANIAC_MANIAC_ENABLED;
    public final ForgeConfigSpec.BooleanValue DROPPED_SATING_ENABLED;
    public final ForgeConfigSpec.BooleanValue GUM_TRAMPLE_ENABLED;
    public final ForgeConfigSpec.BooleanValue SUGAR_CRASH_ENABLED;
    public final ForgeConfigSpec.BooleanValue ICED_CREAM_ENABLED;
    public final ForgeConfigSpec.BooleanValue SOLIDIFIED_WATCHER_ENABLED;
    public final ForgeConfigSpec.BooleanValue GINGER_DISINTEGRATE_ENABLED;
    public final ForgeConfigSpec.BooleanValue CARBONATED_STOMACH_ENABLED;
    public final ForgeConfigSpec.BooleanValue HEAVY_GRAVITY_ENABLED;
    public final ForgeConfigSpec.BooleanValue PRESERVED_AMBER_ENABLED;
    public final ForgeConfigSpec.BooleanValue SEETHED_TAMING_ENABLED;
    public final ForgeConfigSpec.BooleanValue HAZMAT_AMPLIFIED_ENABLED;
    public final ForgeConfigSpec.BooleanValue TREMOR_V_TREMOR_ENABLED;
    public final ForgeConfigSpec.BooleanValue PEERING_TRIGGER_ENABLED;
    public final ForgeConfigSpec.BooleanValue RADIOACTIVE_AWARENESS_ENABLED;









    public ACEConfigList(final ForgeConfigSpec.Builder builder) {
        builder.push("General");
         this.REDOABLE_SPELUNKY_ENABLED = buildBoolean(builder, "REDOABLE_SPELUNKY_ENABLED", " ", true, "Whether it gives you back the tablet when exiting the spelunky table");
         this.SPELUNKY_ATTEMPTS_AMOUNT = buildInt(builder, "SPELUNKY_ATTEMPTS_AMOUNT", " ", 5, 1, Integer.MAX_VALUE, "Amount of tries you get for the spelunky table per tablet");
         this.VANILLA_ADAPTIONS_ENABLED = buildBoolean(builder, "VANILLA_ADAPTIONS_ENABLED", " ", true, "Whether vanilla enchantments adapts to weapons (ex.Dreadbow, etc..)");
         this.CHARGED_CAVE_CREEPER_CHANCE = buildDouble(builder, "CHARGED_CAVE_CREEPER_CHANCE", " ", 0.2,0.0,1.0, "Chances of creepers from caves to be charged (0 for disable)");
         this.ADDITIONAL_FLAMMABILITY_ENABLED = buildBoolean(builder, "ADDITIONAL_FLAMMABILITY_ENABLED", " ", true, "Adds flammability to blocks that should be flammable");


        builder.pop();
        builder.push("Candy Cavity");
        this.GLUTTONY_ENABLED = buildBoolean(builder, "GLUTTONY_ENABLED", " ", true, "Whether blocks inside candy cavity, can be eaten by right clicking");
        this.AMPLIFIED_FROSTMINT_ENABLED = buildBoolean(builder, "AMPLIFIED_FROSTMINT_ENABLED", " ", true, "Item and spear entity explodes as well in Purple Soda");
        this.SOLIDIFIED_ENABLED = buildBoolean(builder, "SOLIDIFIED_ENABLED", " ", true, "Frostmint Spears causes liquid blocks to freeze at the cost of the spear");
        this.PURPLE_LEATHERED_ENABLED = buildBoolean(builder, "PURPLE_LEATHERED_ENABLED", " ", true, "Whether purple soda, purples dyable armor");
        this.STICKY_SODA_ENABLED = buildBoolean(builder, "STICKY_SODA_ENABLED", " ", true, "Whether purple soda causes stickyness when in it");
        this.RADIANT_WRATH_ENABLED = buildBoolean(builder, "RADIANT_WRATH_ENABLED", " ", true, "Having Radiant Essence amplifies the attacks of Sugar Staff");
        this.DROPPED_SATING_ENABLED = buildBoolean(builder, "DROPPED_SATING_ENABLED", " ", true, "Dropping foods in a dropped sack of sating will consume the dropped foods");
        this.SUGAR_CRASH_ENABLED = buildBoolean(builder, "SUGAR_CRASH_ENABLED", " ", true, "Sugar Rushes at the end will cause sugar crashed dealing damage and temporary slowness");
        this.ICED_CREAM_ENABLED = buildBoolean(builder, "ICED_CREAM_ENABLED", " ", true, "Thrown ice cream slightly freezes those that are hit");
        this.CARBONATED_STOMACH_ENABLED = buildBoolean(builder, "CARBONATED_STOMACH_ENABLED", " ", true, "You will explode if you drink purple soda, and then eat frostmint (or vise versa)");

        builder.push("Caniac");
        this.CANIAC_SENSITIVITY_ENABLED = buildBoolean(builder, "CANIAC_SENSITIVITY_ENABLED", " ", true, "Whether Caniac dissolve in water and will actively avoid it");
        this.BEDWARS_ENABLED = buildBoolean(builder, "BEDWARS_ENABLED", " ", true, "Whether Caniacs randomly destroy beds");
        this.CANIAC_MANIAC_ENABLED = buildBoolean(builder, "CANIAC_MANIAC_ENABLED", " ", true, "Whether Caniacs randomly attacks others");

        builder.pop();
        builder.push("Caramel Cube");
        this.STICKY_CARAMEL_ENABLED = buildBoolean(builder, "STICKY_CARAMEL_ENABLED", " ", true, "Whether caramel cube's stickiness applies to its attacks");
        builder.pop();
        builder.push("Gingerbread Man");
        this.AMPUTATION_ENABLED = buildBoolean(builder, "AMPUTATION_ENABLED", " ", true, "Whether Gingerbread Men can be amputated with an axe");
        this.HIVE_MIND_ENABLED = buildBoolean(builder, "HIVE_MIND_ENABLED", " ", true, "Whether Gingerbread Men will attack you if you attack one of them.");
        this.CRUMBY_RAGE_ENABLED = buildBoolean(builder, "CRUMBY_RAGE_ENABLED", " ", true, "Whether Gingerbread Men will attack you if you eat their crumbs");
        this.GINGER_DISINTEGRATE_ENABLED = buildBoolean(builder, "GINGER_DISINTEGRATE_ENABLED", " ", true, "Whether Gingerbread Men are sensitive to water");

        builder.pop();
        builder.push("Gumbeeper");
        this.REGULAR_REFERENCE_ENABLED = buildBoolean(builder, "REGULAR_REFERENCE_ENABLED", " ", true, "Whether Gumbeepers will attack, Blue jays and Raccoons in Alexs Mobs");
        builder.pop();
        builder.push("Gummy Bear");
        this.JELLYBEAN_CHANGES_ENABLED = buildBoolean(builder, "JELLYBEAN_CHANGES_ENABLED", " ", true, "Jellybean harvesting is changed where amount of jellybeans is made the longer they hibernate");
        this.SWEETISH_SPEEDUP_ENABLED = buildBoolean(builder, "SWEETISH_SPEEDUP_ENABLED", " ", true, "Whether feeding Sweetish Fish to a Gummy Bear that are of the same color will speed up its hibernation");

        builder.pop();
        builder.push("Gum Worm");
        this.PRESSURED_HOOKS_ENABLED = buildBoolean(builder, "PRESSURED_HOOKS_ENABLED", " ", true, "Whether candy hooks will take damage overtime when riding a gum worm");
        this.LOGICAL_RIDING_ENABLED = buildBoolean(builder, "LOGICAL_RIDING_ENABLED", " ", true, "Whether to kick you off the gum worm when either of your hooks are not present");
        this.GUM_TRAMPLE_ENABLED = buildBoolean(builder, "GUM_TRAMPLE_ENABLED", " ", true, "Whether gum worm head damages nearby enemies");

        builder.pop();
        
        

        builder.pop();
        builder.push("Forlorn Hollows");
        this.FORLORN_LIGHT_EFFECT_ENABLED = buildBoolean(builder, "FORLORN_LIGHT_EFFECT_ENABLED", " ", true, "Whether most forlorn mammals are effected when a player holds light");
        this.BURST_OUT_ENABLED = buildBoolean(builder, "BURST_OUT_ENABLED", " ", true, "Whether breaking Forlorn Hollows blocks has a chance to burst out Underzealots or Corrodents");
        this.DARKNESS_APPLYED_ENABLED = buildBoolean(builder, "DARKNESS_APPLYED_ENABLED", " ", true, "Whether darkness arrows have a 1% chance of inflicting darkness");
        this.RABIES_ENABLED = buildBoolean(builder, "RABIES_ENABLED", " ", true, "Whether corrodents inflict rabies-like symptoms");
        this.GUANO_SLOW_ENABLED = buildBoolean(builder, "GUANO_SLOW_ENABLED", " ", true, "Whether guano slows you down");
        this.PEERING_TRIGGER_ENABLED = buildBoolean(builder, "PEERING_TRIGGER_ENABLED", " ", true, "Whether peering coprolith interact with players holding light in different ways");

        builder.push("Corrodent");
        this.CORRODENT_CONVERSION_ENABLED = buildBoolean(builder, "CORRODENT_CONVERSION_ENABLED", " ", true, "Whether corrodents can be converted to underzealots through ritual");
        this.KNAWING_ENABLED = buildBoolean(builder, "KNAWING_ENABLED", " ", true, "Whether corrodents knaw on dropped items");

        builder.pop();
        builder.push("Underzealot");
        this.UNDERZEALOT_RESPECT_ENABLED = buildBoolean(builder, "UNDERZEALOT_RESPECT_ENABLED", " ", true, "Whether underzealot respect those that wear the darkness");
        this.EXTINGUISH_CAMPFIRES_ENABLED = buildBoolean(builder, "EXTINGUISH_CAMPFIRES_ENABLED", " ", true, "Whether underzealots extinguish campfires");

        builder.pop();
        builder.push("Vesper");
        this.ANTI_SACRIFICE_ENABLED = buildBoolean(builder, "ANTI_SACRIFICE_ENABLED", " ", true, "Whether vespers attack underzealots sacrificing vespers");
        this.VESPER_HUNT_ENABLED = buildBoolean(builder, "VESPER_HUNT_ENABLED", " ", true, "Additional mobs vesper hunt");
        this.VESPER_SHOTDOWN_ENABLED = buildBoolean(builder, "VESPER_SHOTDOWN_ENABLED", " ", true, "Whether vespers can be shot down");

        builder.pop();
        builder.push("Watcher");
        this.STABILIZER_COMPATIBILITY_ENABLED = buildBoolean(builder, "STABILIZER_COMPATIBILITY_ENABLED", " ", true, "Whether the Stabilizer enchantment from Alexs Mobs Interaction negates possession from the Watcher");
        this.DISORIENTED_ENABLED = buildBoolean(builder, "DISORIENTED_ENABLED", " ", true, "Whether you get disoriented when possessed by a watcher");
        this.SOLIDIFIED_WATCHER_ENABLED = buildBoolean(builder, "SOLIDIFIED_WATCHER_ENABLED", " ", true, "Whether watchers solidify into totems when long enough");

        builder.pop();
        builder.pop();

        builder.push("Toxic Caves");
        this.EXEMPLIFIED_IRRADIATION_AMOUNT = buildInt(builder, "EXEMPLIFIED_IRRADIATION_AMOUNT", " ", 5, 1, Integer.MAX_VALUE, "Amount of irradiation level to get the deadly effects");
        this.IRRADIATED_CREEPER_ENABLED = buildBoolean(builder, "IRRADIATED_CREEPER_ENABLED", " ", true, "Whether Irradiated Creepers have their explosions amplified");
        this.IRRADIATION_WASHOFF_ENABLED = buildBoolean(builder, "IRRADIATION_WASHOFF_ENABLED", " ", true, "Whether Irradiation wears off faster when in water");
        this.GEOTHERMAL_EFFECTS_ENABLED = buildBoolean(builder, "GEOTHERMAL_EFFECTS_ENABLED", " ", true, "Whether Geothermal Vents have effects when standing on top depending on the spewed smoke");
        this.KIROV_REPORTING_ENABLED = buildBoolean(builder, "KIROV_REPORTING_ENABLED", " ", true, "Allows lighting of explosives, including nuclear bombs with a flint and steel off-hand during flight");
        this.HAZMAT_AMPLIFIED_ENABLED = buildBoolean(builder, "HAZMAT_AMPLIFIED_ENABLED", " ", true, "Hazmat reduces amount of irradiation recieved from rayguns");
        this.RADIOACTIVE_AWARENESS_ENABLED = buildBoolean(builder, "RADIOACTIVE_AWARENESS_ENABLED", " ", true, "Radioactive blocks emit irradiation to nearby players");

        builder.push("Braniac");
        this.MUTATED_DEATH_ENABLED = buildBoolean(builder, "MUTATED_DEATH_ENABLED", " ", true, "Whether players with irradiated with an amplifier of 2 or more spawns braniacs");
        this.WASTE_PICKUP_ENABLED = buildBoolean(builder, "WASTE_PICKUP_ENABLED", " ", true, "Whether Braniacs can pickup waste drums if they have non");
        this.WASTE_POWERUP_ENABLED = buildBoolean(builder, "WASTE_POWERUP_ENABLED", " ", true, "Whether Braniacs drinking waste drums powers them up");

        builder.pop();
        builder.push("Gammaroach");
        this.GAMMAROACH_FOODING_ENABLED = buildBoolean(builder, "GAMMAROACH_FOODING_ENABLED", " ", true, "Whether gammaroaches hunt dropped food");
        builder.pop();
        builder.push("Nucleeper");
        this.NUCLEAR_CHAIN_ENABLED = buildBoolean(builder, "NUCLEAR_CHAIN_ENABLED", " ", true, "Whether nucleepers explode when they die from explosions");
        builder.pop();
        builder.push("Radgill");
        this.FISH_MUTATION_ENABLED = buildBoolean(builder, "FISH_MUTATION_ENABLED", " ", true, "Whether fish have a chance to turn into radgill when dowsed in acid");
        builder.pop();
        builder.push("Raycat");
        this.CAT_MUTATION_ENABLED = buildBoolean(builder, "CAT_MUTATION_ENABLED", " ", true, "Whether cats have a chance to turn into raycat when dowsed in acid");
        builder.pop();
        builder.pop();

        builder.push("Primordial Caves");
        this.FLY_TRAP_ENABLED = buildBoolean(builder, "FLY_TRAP_ENABLED", " ", true, "Flytraps close shut when a fly (From Alexs Mobs) comes into contact with it");
        this.PRIMORDIAL_OXYGEN_ENABLED = buildBoolean(builder, "PRIMORDIAL_OXYGEN_ENABLED", " ", true, "Whether you have less oxygen inside Primordial Caves");
        this.FEARED_ANCESTORS_ENABLED = buildBoolean(builder, "FEARED_ANCESTORS_ENABLED", " ", true, "Whether you are attacked by some dinosaurs if seen with a limestone spear");
        this.DINOSAUR_EGG_ANGER_ENABLED = buildBoolean(builder, "DINOSAUR_EGG_ANGER_ENABLED", " ", true, "Whether untamed dinosaurs will attack any that are seen with their egg");
        this.SCAVENGING_ENABLED = buildBoolean(builder, "SCAVENGING_ENABLED", " ", true, "Whether carnivores scavenge for placed dinosaur chops");
        this.AMBER_HEAL_ENABLED = buildBoolean(builder, "AMBER_HEAL_ENABLED", " ", true, "Whether stepping on amber can rarely heal you");
        this.STOMP_DAMAGE_ENABLED = buildBoolean(builder, "STOMP_DAMAGE_ENABLED", " ", true, "Whether Atlatitans/Lux stomps will cause damage");
        this.PAINT_EFFECTS_ENABLED = buildBoolean(builder, "PAINT_EFFECTS_ENABLED", " ", true, "Whether painting dinosaurs gives temporary effects");
        this.PRESERVED_AMBER_ENABLED = buildBoolean(builder, "PRESERVED_AMBER_ENABLED", " ", true, "Whether amber when generated could contain mobs or items");

        builder.push("Atlatitan");
        this.VOLCANIC_SACRIFICE_ENABLED = buildBoolean(builder, "VOLCANIC_SACRIFICE_ENABLED", " ", true, "Whether atlatitan eggs or babies can be sacrificed to a volcano to refresh its lux cooldown");
        builder.pop();
        builder.push("Tremorsaurus");
        this.SEETHED_TAMING_ENABLED = buildBoolean(builder, "SEETHED_TAMING_ENABLED", " ", true, "Whether tremorsaurus can be tamed alternatively with meat(MUST HAVE SCAVENGING FEATURE ENABLED)");
        this.TREMOR_V_TREMOR_ENABLED = buildBoolean(builder, "TREMOR_V_TREMOR_ENABLED", " ", true, "Whether tremorsaurus fights other tremorsaurus when attacked");

        builder.pop();
        builder.pop();


        builder.push("Magnetic Caves");
        this.RESISTOR_MAGNETISM_ENABLED = buildBoolean(builder, "RESISTOR_MAGNETISM_ENABLED", " ", true, "Resistor Shield attracts/repels magnetic items, or more..");
        this.HEAVY_GRAVITY_ENABLED = buildBoolean(builder, "HEAVY_GRAVITY_ENABLED", " ", true, "Entities holding heavy-weight item will increase falling speed");


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
