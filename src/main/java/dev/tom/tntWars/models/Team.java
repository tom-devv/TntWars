package dev.tom.tntWars.models;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Team {

    private Set<UUID> playerUUIDs = new HashSet<>();
    private int number;

    public Team(int number){
        this.number = number;
    }

    public boolean sameTeam(Player player1, Player player2){
        return playerUUIDs.contains(player1.getUniqueId()) && playerUUIDs.contains(player2.getUniqueId());
    }

    /**
     * Apply an action over each team member
     * @param action
     */
    public  void applyPlayers(Consumer<Player> action){
        getPlayers().forEach(action);
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

    /**
     * Get team players from UUID
     * @return all player's who are not null
     */
    public Set<Player> getPlayers() {
        return getPlayerUUIDs().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Set<UUID> getPlayerUUIDs() {
        return playerUUIDs;
    }
}
