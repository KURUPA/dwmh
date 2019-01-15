package com.noobanidus.dwmh.config;

import com.noobanidus.dwmh.DWMH;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("WeakerAccess")
@Config(modid = DWMH.MODID)
@Mod.EventBusSubscriber(modid = DWMH.MODID)
public class DWMHConfig {

    @Config.Comment("Settings related to the Ocarina and its use")
    @Config.Name("Ocarina Settings")
    public static Ocarina Ocarina = new Ocarina();
    @Config.Comment("Settings related to the Enchanted Carrot and its use")
    @Config.Name("Enchanted Carrot Settings")
    public static Carrot EnchantedCarrot = new Carrot();
    @Config.Comment("Options relating to the individual proxies.")
    @Config.Name("Proxy Settings")
    public static Proxies proxies = new Proxies();
    @Config.Comment("Specify a blacklist of entities that should always be ignored, even if generally loaded by their proxy.")
    @Config.Name("Entity Blacklist")
    public static String[] blacklist = new String[]{};
    @Config.RequiresMcRestart
    @Config.Comment("Set to false to disable the craftable saddle recipes")
    @Config.Name("Enable Saddle Recipe")
    public static boolean saddleRecipe = true;

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(DWMH.MODID)) {
            ConfigManager.sync(DWMH.MODID, Config.Type.INSTANCE);
            DWMH.resolveClasses();
            Registrar.ocarina.updateConfig();
            Registrar.carrot.updateConfig();
            Registrar.ocarina.checkRepairItem();
            Registrar.ocarina.checkCostItem();
            Registrar.carrot.checkRepairItem();

            if (Ocarina.functionality.getMaxUses() != Ocarina.functionality.maxUses)
                DWMH.LOG.error(String.format("Invalid configuration value for Ocarina:maxUses: |%d|. Using default 0 instead.", Ocarina.functionality.maxUses));
            if (Ocarina.functionality.getCooldown() != Ocarina.functionality.cooldown)
                DWMH.LOG.error(String.format("Invalid configuration value for Ocarina:cooldown: |%d|. Using default 0 instead.", Ocarina.functionality.cooldown));
            if (Ocarina.functionality.getSummonCost() != Ocarina.functionality.summonCost)
                DWMH.LOG.error(String.format("Invalid configuration value for Ocarina:summonCost: |%d|. Using default 0 instead.", Ocarina.functionality.summonCost));
            if (EnchantedCarrot.durability.getMaxUses() != EnchantedCarrot.durability.maxUses)
                DWMH.LOG.error(String.format("Invalid configuration value for EnchantedCarrot:maxUses: |%d|. Using default 1 instead.", EnchantedCarrot.durability.maxUses));
        }
    }

    public static class Ocarina {
        @Config.Comment("Specify the maximum distance steeds can be summoned from (set to 0 for infinite). Excludes entities in unloaded chunks or different dimensions")
        @Config.RangeDouble(min = 0d)
        @Config.Name("Maximum Summon Distance")
        public double maxDistance = 200d;
        @Config.Comment("Set to true to enable summoning of horses even if they are being ridden by someone else.")
        @Config.Name("Summon With Other Riders")
        public boolean otherRiders = false;
        @Config.Comment("Set to true to swap the effect of sneaking. Thus, right-clicking will list entities, while sneak-right-clicking will summon them.")
        @Config.Name("Swap Sneak Functionality")
        public boolean swap = false;
        @Config.Comment("Set to false to prevent setting a home when summoning steeds. This should prevent them wandering too far from where you summoned them to, but may cause interactions with other mods.")
        @Config.Name("Set Home Location When Summoned")
        public boolean home = true;
        @Config.Comment("Set to true to skip removing a home point when you dismount a horse. This may help with mods that set a home upon dismounting, as well. Otherwise, setting this to false will make horses believe their home is where you last summoned them to was.")
        @Config.Name("Skip Clearing Home on Dismount")
        public boolean skipDismount = false;
        @Config.Comment("Options related to the functionality of the Ocarina")
        @Config.Name("Functionality")
        public Functionality functionality = new Functionality();
        @Config.Comment("Options related to audio and text output of the Ocarina")
        @Config.Name("Responses")
        public Responses responses = new Responses();

        public double getMaxDistance() {
            return Math.max(maxDistance, 0.0d);
        }

        public class Functionality {
            @Config.Comment("Specify a cooldown for each usage of the Ocarina, or specify 0 to disable")
            @Config.Name("Ocarina Cooldown Duration")
            @Config.RangeInt(min = 0)
            public int cooldown = 0;
            @Config.Comment("Specify the maximum durability of the Ocarina. One horse summoned costs one durability. Set to 0 to disable durability.")
            @Config.Name("Maximum Ocarina Durability")
            @Config.RangeInt(min = 0)
            public int maxUses = 0;
            @Config.Comment("Specify the item that can be used to repair the Ocarina in an anvil. Items with NBT are not supported. Format mod:item:metadata (use \"minecraft\" for vanilla items), use 0 for no meteadata.")
            @Config.Name("Ocarina Repair Item")
            public String repairItem = "minecraft:golden_carrot:0";
            @Config.Ignore
            public ItemStack repairItemDefault = new ItemStack(Items.GOLDEN_CARROT);
            @Config.Comment("Specify the item to consume from the player's inventory before summoning a horse. Format: mod:item:metadata (use \"minecraft\" for vanilla items), use 0 for no metadata. Items with NBT are not supported.")
            @Config.Name("Summon Item")
            public String summonItem = "minecraft:carrot:0";
            @Config.Ignore
            public ItemStack summonItemStack = new ItemStack(Items.CARROT);
            @Config.Comment("Specify the quantity of the item to consume from the player's inventory before summoning a horse. Set to 0 to consume nothing.")
            @Config.Name("Summon Cost")
            @Config.RangeInt(min = 0)
            public int summonCost = 0;

            public int getCooldown() {
                return Math.max(cooldown, 0);
            }

            public int getMaxUses() {
                return Math.max(maxUses, 0);
            }

            public int getSummonCost() {
                return Math.max(summonCost, 0);
            }
        }

        public class Responses {
            @Config.Comment("Set to true to disable messages when teleporting a horse to you.")
            @Config.Name("Disable Summoned Messages")
            public boolean quiet = false;

            @Config.Comment("Set to true to compact multiple horse-summoned messages into one.")
            @Config.Name("Combine Summoned Messages")
            public boolean simple = false;

            @Config.Comment("Set to false to disable showing the distance and direction a horse is away from you")
            @Config.Name("Enable Distance/Direction")
            public boolean distance = true;

            @Config.Comment("Set to false to disable sounds being played when the Ocarina is use. These sounds are played on the PLAYERS channel.")
            @Config.Name("Enable Ocarina Tunes")
            public boolean sounds = true;

            @Config.Comment("The delay in seconds between uses of the Ocarina causing a sound event. The Ocarina will still trigger, but silently, during this delay.")
            @Config.Name("Ocarina Sound Delay")
            public int soundDelay = 5;

            @Config.Comment("Set to true to prevent llamas from being listed with the Ocarina.")
            @Config.Name("Disable Listing Llama")
            public boolean noLlamas = false;
        }
    }

    public static class Carrot {
        @Config.RequiresMcRestart
        @Config.Comment("Set to false to disable the Enchanted Carrot recipe from being registered.")
        @Config.Name("Enable Carrot Recipe")
        public boolean enabled = true;

        @Config.Comment("Settings related to the durability of the Enchanted Carrot.")
        @Config.Name("Durability settings")
        public Durability durability = new Durability();
        @Config.Comment("Allows fine-tuning of the individual effects of the Enchanted Carrot")
        @Config.Name("Effects")
        public CarrotEffects effects = new CarrotEffects();
        @Config.Comment("Determine whether messages are displayed when certain functions of the carrot are used")
        @Config.Name("Messages")
        public CarrotMessages messages = new CarrotMessages();
        @Config.Comment("Set to true to enable the enchantment glint. Useful if you are using a texture pack that overrides the Carrot animation. Client-side only.")
        @Config.Name("Enable Enchantment Glint")
        public boolean glint = false;

        public static class CarrotEffects {
            @Config.Comment("Set to false to prevent the automatic taming of rideable entities")
            @Config.Name("Enable Eligible Entity Taming")
            public boolean taming = true;

            @Config.Comment("Set to false to prevent the healing to full of injured rideable entities")
            @Config.Name("Enable Eligible Entity Healing")
            public boolean healing = true;

            @Config.Comment("Set to false to prevent the aging to adulthood of rideable child entities")
            @Config.Name("Enable Eligible Entity Aging")
            public boolean aging = true;

            @Config.Comment("Set to false to prevent putting tamed, adult rideable entities into \"breeding\" mode")
            @Config.Name("Enable Eligible Entity Breeding")
            public boolean breeding = true;
        }

        public static class CarrotMessages {
            @Config.Comment("Set to false to prevent messages while taming")
            @Config.Name("Enable Eligible Entity Taming Message")
            public boolean taming = true;

            @Config.Comment("Set to false to prevent messages while healing")
            @Config.Name("Enable Eligible Entity Healing Message")
            public boolean healing = true;

            @Config.Comment("Set to false to prevent messages while aging")
            @Config.Name("Enable Eligible Entity Aging Message")
            public boolean aging = true;

            @Config.Comment("Set to false to prevent messages while breeding")
            @Config.Name("Enable Eligible Entity Breeding Message")
            public boolean breeding = true;
        }

        public class Durability {
            @Config.RangeInt(min = 1)
            @Config.Comment("Maximum number of uses before the enchanted Enchanted Carrot becomes unusable")
            @Config.Name("Maximum Carrot Durability")
            public int maxUses = 30;
            @Config.Comment("Specify the item that can be used to repair the Enchanted Carrot in an anvil. Items with NBT are not supported. Format: mod:item:metadata. Use \"minecraft\" for vanilla items, and 0 if no metadata is specified.")
            @Config.Name("Carrot Repair Item")
            public String repairItem = "minecraft:gold_block:0";
            @Config.Ignore
            public ItemStack repairItemDefault = new ItemStack(Blocks.GOLD_BLOCK);

            public int getMaxUses() {
                return Math.max(maxUses, 1);
            }
        }
    }

    public static class Proxies {

        @Config.Comment("Overrides to specifically disable certain proxies")
        @Config.Name("Enable/Disable Proxies")
        public Enable enable = new Enable();
        @Config.Comment("Options related specifically to the Animania ")
        @Config.Name("Animania Settings")
        public Animania Animania = new Animania();
        @Config.Comment("Options related specifically to ZAWA")
        @Config.Name("ZAWA Settings")
        public ZAWA ZAWA = new ZAWA();
        @Config.Comment("Options related specifically to Mo Creatures")
        @Config.Name("Mo Creatures Settings")
        public MoCreatures MoCreatures = new MoCreatures();

        public class Enable {
            @Config.RequiresMcRestart
            @Config.Comment("Set to false to disable the Animania proxy (even if it would normally load)")
            @Config.Name("Animania")
            public boolean animania = true;

            @Config.RequiresMcRestart
            @Config.Comment("Set to false to disable the Mo Creatures proxy (even if it would normally load)")
            @Config.Name("Mo Creatures")
            public boolean mocreatures = true;

            @Config.RequiresMcRestart
            @Config.Comment("Set to false to disable the ZAWA Rebuilt proxy (even if it would normally load)")
            @Config.Name("ZAWA Rebuilt")
            public boolean zawa = true;

            @Config.RequiresMcRestart
            @Config.Comment("Set to false to disable the Ultimate Unicorn Mod proxy (even if it would normally load)")
            @Config.Name("Ultimate Unicorn Mod")
            public boolean ultimate_unicorn_mod = true;
        }

        public class Animania {
            @Config.Comment("Specify list of Animania classes that are considered steeds. Use /dwmh entity while targetting to get the full name")
            public String[] classes = new String[]{"com.animania.common.entities.horses.EntityStallionDraftHorse", "com.animania.common.entities.horses.EntityMareDraftHorse"};
        }

        public class ZAWA {
            @Config.Comment("Specify list of ZAWA Rebuilt classes that are considered steeds. Use /dwmh entity while targetting to get the full name")
            public String[] classes = new String[]{"org.zawamod.entity.land.EntityAsianElephant", "org.zawamod.entity.land.EntityGaur", "org.zawamod.entity.land.EntityGrevysZebra", "org.zawamod.entity.land.EntityOkapi", "org.zawamod.entity.land.EntityReticulatedGiraffe"};
        }

        public class MoCreatures {
            @Config.Comment("Specify list of entity translation keys which should be modified to insert spaces (where relevant)")
            public String[] entities = new String[]{"entity.mocreatures:blackbear.name", "entity.mocreatures:grizzlybear.name", "entity.mocreatures:komododragon.name", "entity.mocreatures:petscorpion.name", "entity.mocreatures:wildhorse.name", "entity.mocreatures:wildpolarbear.name"};
        }
    }
}
