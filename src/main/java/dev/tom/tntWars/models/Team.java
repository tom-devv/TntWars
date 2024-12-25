package dev.tom.tntWars.models;

import java.util.Set;
import java.util.UUID;

public class Team {

    private Set<UUID> playerUUIDs;

    public Team(){

    }

    public int size(){
        return playerUUIDs.size();
    }

    public void removePlayer(UUID playerUUID){
        playerUUIDs.remove(playerUUID);
    }

    public void addPlayer(UUID playerUUID){
        playerUUIDs.add(playerUUID);
    }

    public void clearTeam(){
        playerUUIDs.clear();
    }


    public Set<UUID> getPlayerUUIDs() {
        return playerUUIDs;
    }
}
