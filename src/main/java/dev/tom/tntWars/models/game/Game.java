package dev.tom.tntWars.models.game;

import dev.tom.tntWars.TNTWars;
import dev.tom.tntWars.models.map.Map;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.utils.NameGenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Game {

    private final String gameId;
    private final GameSettings settings;
    /**
     * Map of player UUID to team
     */
    private final java.util.Map<UUID, Team> playerTeams = new HashMap<>();
    /**
     * Map of team number to Team
     */
    private final java.util.Map<Integer, Team> teams = new HashMap<>();
    private Set<UUID> participants = new HashSet<>();
    private Map map;
    private GameState state;
    private GameStats stats;
    private long epochSeconds = 0;

    public Game(GameSettings settings, Collection<Team> teams){
        this.settings = settings;
        teams.forEach(team -> this.teams.put(team.getNumber(), team));
        teams.forEach(team -> {
            team.getPlayerUUIDs().forEach(uuid -> {
                playerTeams.put(uuid, team);
            });
        });
        this.state = GameState.INACTIVE;
        this.gameId = NameGenerator.generateName();
        this.participants = resolveParticipants();
        this.stats = new GameStats(this);
    }

    // game timer for ending game and placeholder etc
    public void startTimer(){
        long maxTimeSeconds = this.settings.getTimeSeconds();
        Game game = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (epochSeconds >= maxTimeSeconds) {
                    TNTWars.getGameController().endGame(game);
                    this.cancel();
                }
                if(game.getState() == GameState.ENDED) {
                    this.cancel();
                }
                // don't add time if paused or inactive
                if(game.getState() == GameState.INACTIVE || game.getState() == GameState.PAUSED) {
                    return;
                }
                epochSeconds++;
            }
        }.runTaskTimer(TNTWars.getPlugin(), 0, 20); // run every second
    }

    public Set<UUID> getParticipants(){
        return this.participants;
    }

    /**
     * Fetch player from participants UUID and removed null players
     * @return
     */
    public Set<Player> getPlayers(){
        return this.participants.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * Pulls UUID out of the teams to get a complete set of all players in the game
     */
    private Set<UUID> resolveParticipants() {
        Set<UUID> participants = new HashSet<>();
        if(this.teams == null) return participants;
        this.teams.values().forEach(team -> {
            participants.addAll(team.getPlayerUUIDs());
        });
        return participants;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        if (this.map != null && map != null) {
            throw new IllegalStateException("Map is already set for this game.");
        }
        this.map = map;
    }

    public void setState(GameState state) {
        if(getState().isTransitionAllowed(state)){
            this.state = state;
        } else {
            throw new IllegalStateException("Cannot transition game: " + gameId + " from state: " + getState() + " to state: " + state);
        }
    }

    /**
     * Checks if two players are on the same team
     * @param player1
     * @param player2
     * @return
     */
    public boolean sameTeam(Player player1, Player player2){
        for (Team team : getTeams()) {
            if(team.sameTeam(player1, player2)){
                return true;
            }
        }
        return false;
    }

    public GameStats getStats() {
        return stats;
    }

    /**
     * Get a players team
     * @param player
     * @return an optional as the player may not have a team?
     */
    public Optional<Team> getTeam(Player player){
        Team team = playerTeams.get(player.getUniqueId());
        if(team == null) return Optional.empty();
        return Optional.of(team);
    }

    /**
     * Apply an action over each game member
     * @param action
     */
    public  void applyPlayers(Consumer<Player> action){
        getPlayers().forEach(action);
    }

    /**
     * Get a team by its number
      * @param number
     * @return the team or null
     */
    public @Nullable Team getTeam(int number){
        return teams.get(number);
    }

    public Collection<Team> getTeams() {
        return teams.values();
    }

    public String getGameId() {
        return gameId;
    }

    public GameState getState() {
        return state;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public long getSecondsLeft() {
        return settings.getTimeSeconds() - epochSeconds;
    }
}
