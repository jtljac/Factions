package com.demmodders.factions.faction;

import com.demmodders.factions.Factions;
import com.demmodders.factions.util.FactionConfig;
import com.demmodders.factions.util.Utils;
import com.demmodders.factions.util.enums.RelationState;
import com.demmodders.factions.util.structures.Location;
import com.demmodders.factions.util.structures.Power;
import com.demmodders.factions.util.structures.Relationship;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Faction {
    public String name = "";
    public String desc = "";
    public String motd = "";
    public Location homePos= null;
    public Long foundingTime = 0L;
    public Power power = null;
    public ArrayList<UUID> invites;
    public HashMap<String, Boolean> flags;
    public HashMap<UUID, Relationship> relationships = new HashMap<>();
    public transient ArrayList<UUID> members;
    public transient HashMap<Integer, ArrayList<String>> land;

    Faction(){

    }

    public Faction(String name, UUID playerID){
        this.name = name;
        this.foundingTime = System.currentTimeMillis();

        power = new Power();
        invites = new ArrayList<>();

        flags = new HashMap<>();
        relationships = new HashMap<>();

        this.land = new HashMap<>();
        this.members = new ArrayList<>();
        this.members.add(playerID);
    }

    /**
     * Adds the specified player to the faction
     * @param PlayerID the ID of the player to add
     */
    public void addPlayer(UUID PlayerID){
        if (!members.contains(PlayerID)) {
            members.add(PlayerID);
        } else {
            Factions.LOGGER.info("Tried to add player " + PlayerID + " to faction " + name + " when it already has that player, ignoring");
        }
    }

    /**
     * Removes the specified player to the faction
     * @param PlayerID the ID of the player to remove
     */
    public void removePlayer(UUID PlayerID){
        if (members.contains(PlayerID)) {
            members.remove(PlayerID);
        } else {
            Factions.LOGGER.info("Tried to remove player " + PlayerID + " from faction " + name + " when it already has that player, ignoring");
        }
    }

    /**
     * Adds chunk to the faction
     * @param Dimension The dimension of the chunk
     * @param Land The position of the chunk
     */
    public void addLandToFaction(int Dimension, String Land){
        if (!land.containsKey(Dimension)){
            land.put(Dimension, new ArrayList<>());
        }

        if (!land.get(Dimension).contains(Land)){
            land.get(Dimension).add(Land);
        } else {
            Factions.LOGGER.info("Tried to add claimed land " + Land + " in Dim " + Dimension + " to faction " + name + " when it already has that land, ignoring");
        }
    }

    /**
     * Checks if the faction has the specified flag
     * @param Flag The flag to check for
     * @return Whether the faction has the flag
     */
    public boolean hasFlag(String Flag){
        return flags.getOrDefault(Flag, false);
    }

    /**
     * Calculates the amount of power this faction has
     * @return The amount of power the faction has
     */
    public int calculatePower(){
        int factionPower = power.power;
        FactionManager fMan = FactionManager.getInstance();
        for (UUID memberID : members) {
            factionPower += fMan.getPlayer(memberID).power.power;
        }
        return factionPower;
    }

    /**
     *  Calculates the maximum amount of power this faction can have
     * @return the maximum amount of power the faction can have
     */
    public int calculateMaxPower(){
        int factionMaxPower = power.maxPower;
        FactionManager fMan = FactionManager.getInstance();
        for (UUID memberID : members) {
            factionMaxPower += fMan.getPlayer(memberID).power.maxPower;
        }
        return factionMaxPower;
    }

    /**
     * Calculates the cost of all the land this faction owns
     * @return the cost of the land this faction owns
     */
    public int calculateLandCost(){
        int landCount = 0;
        for (int dim: land.keySet()) {
            landCount += land.get(dim).size();
        }
        return landCount * FactionConfig.landSubCat.landPowerCost;
    }

    /**
     * Calculates whether the faction can have the extra amount of land given
     * @param extraLand the amount more land to test
     * @return Whether the faction can claim the amount of land given
     */
    public boolean calculateLandCost(int extraLand){
        int landCount = extraLand;
        for (int dim: land.keySet()) {
            landCount += land.get(dim).size();
        }
        return landCount * FactionConfig.landSubCat.landPowerCost < calculatePower();
    }


    public boolean checkLandTouches(int Dim, int X, int Z){
        // TODO: Consider moving to thread
        String[] coords;
        for (String key : land.get(Dim)){
            coords = key.split(", ");
            if (X == Integer.parseInt(coords[0]) && Z == (Integer.parseInt(coords[1]) + 1) ||
                X == Integer.parseInt(coords[0]) && Z == (Integer.parseInt(coords[1]) - 1) ||
                X == (Integer.parseInt(coords[0]) + 1) && Z == Integer.parseInt(coords[1]) ||
                X == (Integer.parseInt(coords[0]) - 1) && Z == Integer.parseInt(coords[1])) {
                return true;
            }
        }
        return false;
    }

    public String printFactionInfo(){
        FactionManager fman = FactionManager.getInstance();

        // Work out age to display
        StringBuilder message = new StringBuilder();
        String age;

        long minutes = Utils.calculateAge(foundingTime) / 60000;
        if (minutes < 60){
            age = Math.round(minutes) + " Minutes";
        } else if (minutes < 1440){
            age = String.valueOf(minutes / 60) + " Hours";
        } else {
            age = String.valueOf(minutes / 1440) + " Days";
        }

        // Work out invitation policy to display
        String invitePolicy;
        if (hasFlag("Open")){
            invitePolicy = "Open";
        } else {
            invitePolicy = "Invite only";
        }

        // Format Members
        StringBuilder memberText = new StringBuilder();
        Boolean first = true;
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        for (UUID player : members){
            if(!first) {
                memberText.append(TextFormatting.RESET).append(", ");
            } else {
                first = false;
            }
            if(playerList.getPlayerByUUID(player) != null) memberText.append(TextFormatting.GREEN);
            else memberText.append(TextFormatting.RED);
            memberText.append(fman.getPlayer(player).lastKnownName);
        }

        StringBuilder allies = new StringBuilder();
        StringBuilder enemies = new StringBuilder();
        first = true;
        boolean enemyFirst = true;

        for (UUID factionID : relationships.keySet()){
            if (relationships.get(factionID).relation == RelationState.ALLY){
                if (!first) allies.append(TextFormatting.RESET).append(", ");
                else first = false;
                allies.append(TextFormatting.GREEN);
            } else {
                if (!enemyFirst) allies.append(TextFormatting.RESET).append(", ");
                else enemyFirst = false;
                enemies.append(TextFormatting.RED);
            }
        }

        message.append(TextFormatting.GOLD).append("==").append(TextFormatting.DARK_GREEN).append(TextFormatting.GOLD).append(name).append("==\n");
        message.append(TextFormatting.GOLD).append("Description: ").append(TextFormatting.RESET).append(desc).append("\n");
        message.append(TextFormatting.GOLD).append("Age: ").append(TextFormatting.RESET).append(age).append("\n");
        message.append(TextFormatting.GOLD).append("Invitation Policy: ").append(TextFormatting.RESET).append(invitePolicy).append("\n");
        message.append(TextFormatting.GOLD).append("Land worth: ").append(TextFormatting.RESET).append(calculateLandCost()).append("\n");
        message.append(TextFormatting.GOLD).append("Power: ").append(TextFormatting.RESET).append(calculatePower()).append("\n");
        message.append(TextFormatting.GOLD).append("Max Power: ").append(TextFormatting.RESET).append(calculateMaxPower()).append("\n");
        message.append(TextFormatting.GOLD).append("Members: ").append(memberText.toString()).append("\n");
        message.append(TextFormatting.GOLD).append("Allies: ").append(allies.toString()).append("\n");
        message.append(TextFormatting.GOLD).append("Enemies: ").append(enemies.toString()).append("\n");

        return message.toString();
    }
}


