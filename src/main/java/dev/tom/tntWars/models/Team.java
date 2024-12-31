package dev.tom.tntWars.models;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Team {

    private Set<UUID> playerUUIDs = new HashSet<>();
    private int number;

    public Team(int number){
        this.number = number;
    }

    public boolean sameTeam(Player player1, Player player2){
        return playerUUIDs.contains(player1.getUniqueId()) && playerUUIDs.contains(player2.getUniqueId());
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

    public int getNumber() {
        return number;
    }

    public Set<UUID> getPlayerUUIDs() {
        return playerUUIDs;
    }
}
