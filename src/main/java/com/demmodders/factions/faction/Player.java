package com.demmodders.factions.faction;

import com.demmodders.factions.util.enums.FactionChatMode;

import java.util.UUID;

public class Player {
    public UUID faction;
    public String factionRank;
    public Power power;
    public long lastSeen;
    public transient UUID lastFactionLand;
    public transient FactionChatMode factionChat = FactionChatMode.NORMAL;

    public Player(){
        faction = null;
        factionRank = "";
        power = new Power();
        lastSeen = System.currentTimeMillis();
    }
    public Player(UUID Faction, String FactionRank, Power power){
        this.faction = Faction;
        this.factionRank = FactionRank;
        this.power = power;
        lastSeen = System.currentTimeMillis();
    }

    public void clearFaction(){
        this.faction = null;
        this.factionRank = null;
        factionChat = FactionChatMode.NORMAL;
    }
}
