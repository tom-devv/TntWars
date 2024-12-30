package dev.tom.tntWars.models.game;

import dev.tom.tntWars.models.map.Map;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.utils.NameGenerator;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Game {

    private final String gameId;
    private final GameSettings settings;
    private final Collection<Team> teams;
    private Set<UUID> participants = new HashSet<>();
    private Map map;
    private GameState state;

    public Game(GameSettings settings, Collection<Team> teams){
        this.settings = settings;
        this.teams = teams;
        this.state = GameState.INACTIVE;
        this.gameId = NameGenerator.generateName();
        this.participants = resolveParticipants();
    }

    public Set<UUID> getParticipants(){
        return this.participants;
    }

    /**
     * Pulls UUID out of the teams to get a complete set of all players in the game
     */
    private Set<UUID> resolveParticipants() {
        Set<UUID> participants = new HashSet<>();
        this.teams.forEach(team -> {
            participants.addAll(team.getPlayerUUIDs());
        });
        return participants;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        if (this.map != null) {
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

    public Collection<Team> getTeams() {
        return teams;
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
}
