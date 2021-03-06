package com.demmodders.factions.delayedevents;

import com.demmodders.datmoddingapi.delayedexecution.delayedevents.BaseDelayedEvent;
import com.demmodders.datmoddingapi.util.DemConstants;
import com.demmodders.factions.faction.FactionManager;
import com.demmodders.factions.faction.Player;
import com.demmodders.factions.util.FactionConfig;
import com.demmodders.factions.util.enums.FactionRank;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

public class PowerIncrease extends BaseDelayedEvent {
    public boolean cancelled = false;
    public EntityPlayerMP player;
    private int delay;

    public PowerIncrease(int Delay, EntityPlayerMP Player) {
        super(Delay);
        delay = Delay;
        player = Player;
    }

    public double rankMultiplier(FactionRank rank){
        if (rank != null) {
            switch (rank) {
                case GRUNT:
                    return FactionConfig.powerSubCat.powerGainGruntMultiplier;
                case LIEUTENANT:
                    return FactionConfig.powerSubCat.powerGainLieutenantMultiplier;
                case OFFICER:
                    return FactionConfig.powerSubCat.powerGainSergeantMultiplier;
                case OWNER:
                    return FactionConfig.powerSubCat.powerGainOwnerMultiplier;
            }
        }
        return 1.D;
    }

    @Override
    public void execute() {
        // Only execute when a player is in a faction
        if (!FactionManager.getInstance().getPlayersFactionID(player.getUniqueID()).equals(FactionManager.WILDID)) {
            Player fPlayer = FactionManager.getInstance().getPlayer(this.player.getUniqueID());
            fPlayer.addMaxPower((int) Math.ceil(FactionConfig.powerSubCat.maxPowerGainAmount * rankMultiplier(fPlayer.factionRank)));
            fPlayer.addPower((int) Math.ceil(FactionConfig.powerSubCat.powerGainAmount * rankMultiplier(fPlayer.factionRank)));
            this.player.sendMessage(new TextComponentString(DemConstants.TextColour.INFO + "You just gained power! Your power is now " + fPlayer.power.power + "/" + fPlayer.power.maxPower));
        }
        exeTime = System.currentTimeMillis() + delay * 1000L;
    }

    @Override
    public boolean canExecute() {
        if (player.hasDisconnected()){
            cancelled = true;
        }
        return super.canExecute() && !cancelled;
    }

    @Override
    public boolean shouldRequeue(boolean hasFinished) {
        return !cancelled;
    }
}
