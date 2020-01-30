package com.demmodders.factions.util;

import com.demmodders.factions.Factions;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Factions.MODID)
public class FactionConfig {
    public static Faction factionSubCat = new Faction();
    public static Player playerSubCat = new Player();
    public static Land landSubCat = new Land();
    public static Flags flagSubCat = new Flags();
    public static PowerConfig powerSubCat = new PowerConfig();

    public static class Faction {
        @Config.Name("Max Faction Name Length")
        @Config.RangeInt(min = 1)
        @Config.Comment("The maximum length of characters in a faction's name")
        public int maxFactionNameLength = 20;

        @Config.Name("Max Faction Description Length")
        @Config.RangeInt(min = 1)
        @Config.Comment("The maximum length of characters in a faction's description")
        public int maxFactionDescLength = 100;

        @Config.Name("Max Faction MOTD Length")
        @Config.RangeInt(min = 1)
        @Config.Comment("The maximum length of characters in a faction's MOTD")
        public int maxFactionMOTDLength = 100;

        @Config.Name("MOTD Header")
        @Config.Comment("The text that appears at the top of every MOTD, use %s for the faction name")
        public String factionMOTDHeader = "_[%s MOTD]_";

        @Config.Name("Faction Starting Power")
        @Config.RangeInt(min = 1)
        @Config.Comment("The amount of power the faction starts with when it's created")
        public int factionStartingPower = 100;

        @Config.Name("Faction Starting Max Power")
        @Config.RangeInt(min = 1)
        @Config.Comment("The maximum amount of power the player can have when they it's created")
        public int factionStartingMaxPower = 100;

        @Config.Name("Allow ally build")
        @Config.Comment("Permit allies to build on each other's land")
        public boolean allyBuild = true;

        @Config.Name("Allow enemy build")
        @Config.Comment("Permit enemies to build on each other's land")
        public boolean enemyBuild = false;

        @Config.Name("Max Faction Members")
        @Config.RangeInt(min = 0)
        @Config.Comment("The maximum amount of members each faction is allowed (0 for infinite)")
        public int maxMembers = 0;
    }

    public static class Player {
        @Config.Name("Player Starting Power")
        @Config.RangeInt(min = 1)
        @Config.Comment("The amount of power the player starts with when they first join the server")
        public int playerStartingPower = 100;

        @Config.Name("Player Starting Max Power")
        @Config.RangeInt(min = 1)
        @Config.Comment("The maximum amount of power the player can have when they first join the server")
        public int playerStartingMaxPower = 100;

        @Config.Name("Player Starting Max Power cap")
        @Config.RangeInt(min = 1)
        @Config.Comment("The maximum amount of power the player can ever have")
        public int playerMaxPowerCap = 1000;

        @Config.Name("Faction home Delay")
        @Config.RangeInt()
        @Config.Comment("The delay in seconds before a player teleports when using /faction home")
        public int teleportDelay = 3;

        @Config.Name("Faction home again Delay")
        @Config.RangeInt()
        @Config.Comment("The delay in seconds before a player can teleport when using /faction home another time")
        public int reTeleportDelay = 0;
    }

    public static class PowerConfig {
        @Config.Name("Kill power gain")
        @Config.Comment("The amount of power gained for killing someone")
        @Config.RangeInt(min=0)
        public int killPowerGain = 10;

        @Config.Name("Kill max power gain")
        @Config.Comment("The amount of max power increases by when killing someone")
        @Config.RangeInt(min=0)
        public int killMaxPowerGain = 20;

        @Config.Name("Enemy kill multiplier")
        @Config.Comment("How much more power & max power is gained by killing")
        @Config.RangeDouble(min=0f)
        public double enemyKillMultiplier = 2f;

        @Config.Name("Death power Loss")
        @Config.Comment("The amount of power lost for dying")
        @Config.RangeInt(min=0)
        public int deathPowerLoss = 10;

        @Config.Name("Death by enemy loss multiplier")
        @Config.RangeDouble(min=0f)
        @Config.Comment("How much more power lost for dying")
        public double deathByEnemyMultiplier = 2f;

        @Config.Name("Lieutenant Multiplier")
        @Config.RangeDouble(min=0f)
        @Config.Comment("How much extra power is gained/lost for killing/dying as a lieutenant")
        public double lieutenantMultiplier = 1.5f;

        @Config.Name("Officer Multiplier")
        @Config.RangeDouble(min=0f)
        @Config.Comment("How much extra power is gained/lost for killing/dying as a officer")
        public double officerMultiplier = 2f;

        @Config.Name("Owner Multiplier")
        @Config.RangeDouble(min=0f)
        @Config.Comment("How much extra power is gained/lost for killing/dying as the owner of a faction")
        public double ownerMultiplier = 3f;
    }

    public static class Land {
        @Config.Name("Land Power worth")
        @Config.RangeInt(min = 1)
        @Config.Comment("The amount of power each chunk takes up when claimed")
        public int landPowerCost = 20;

        @Config.Name("Require land to connect")
        @Config.Comment("Require newly claimed land to be right next to previously claimed land")
        public boolean landRequireConnect = true;

        @Config.Name("Require land to connect when stealing")
        @Config.Comment("Require newly claimed land to be right next to previously claimed land when stealing the land of other factions")
        public boolean landRequireConnectWhenStealing = true;
    }

    public static class Flags {
        @Config.Name("Bonus Power Multiplier")
        @Config.Comment("The multiplier for the amount of power you lose/gain in factions with the BonusPower tag (Such as the War Zone)")
        public float bonusPowerMultiplier = 1.5f;
    }

    @Mod.EventBusSubscriber(modid = Factions.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void configChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Factions.MODID)) {
                ConfigManager.sync(Factions.MODID, Config.Type.INSTANCE);
            }
        }
    }


}
