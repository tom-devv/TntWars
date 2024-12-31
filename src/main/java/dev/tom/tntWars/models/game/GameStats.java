package dev.tom.tntWars.models.game;

import dev.tom.tntWars.models.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameStats {

    // Team-specific stats
    private Map<Team, Integer> teamKills;
    private Map<Team, Integer> teamDeaths;
    private Map<Team, Integer> tntUsed;

    // Player-specific stats
    private Map<UUID, Integer> playerKills;
    private Map<UUID, Integer> playerDeaths;
    private Map<UUID, Integer> blocksPlaced;
    private Map<UUID, Double> damageDealt;
    private Map<UUID, Double> distanceTraveled;
    private Map<UUID, Double> longestKillDistance;
    private Map<UUID, Integer> killStreaks;
    private Map<UUID, Integer> playerRespawns;
    private Map<UUID, Integer> hitsTaken;
    private Map<UUID, Long> timeAlive; // in milliseconds
    private Map<UUID, Integer> buttonPressed;
    private Map<UUID, Integer> leversFlicked;

    // General game stats
    private int tntIgnited;
    private int blocksBroken;
    private int tntPlaced;
    private int explosionsTriggered;
    private long gameDuration; // in milliseconds
    private Team winningTeam;

    // Environmental stats
    private int environmentalKills;
    private int explosivesRelatedKills;
    private Map<UUID, Integer> fallDamageTaken;

    // Empty constructor with default values
    public GameStats(Game game) {
        this.teamKills = new HashMap<>();
        this.teamDeaths = new HashMap<>();
        this.tntUsed = new HashMap<>();
        this.playerKills = new HashMap<>();
        this.playerDeaths = new HashMap<>();
        this.blocksPlaced = new HashMap<>();
        this.damageDealt = new HashMap<>();
        this.distanceTraveled = new HashMap<>();
        this.longestKillDistance = new HashMap<>();
        this.killStreaks = new HashMap<>();
        this.playerRespawns = new HashMap<>();
        this.hitsTaken = new HashMap<>();
        this.timeAlive = new HashMap<>();
        this.buttonPressed = new HashMap<>();
        this.leversFlicked = new HashMap<>();
        this.tntIgnited = 0;
        this.blocksBroken = 0;
        this.tntPlaced = 0;
        this.explosionsTriggered = 0;
        this.gameDuration = 0L;
        this.winningTeam = null;
        this.environmentalKills = 0;
        this.explosivesRelatedKills = 0;
        this.fallDamageTaken = new HashMap<>();
    }

    public Map<UUID, Integer> getPlayerDeaths() {
        return playerDeaths;
    }

    public void setPlayerDeaths(Map<UUID, Integer> playerDeaths) {
        this.playerDeaths = playerDeaths;
    }

    public Map<Team, Integer> getTeamKills() {
        return teamKills;
    }

    /**
     * Adds a kill to a team
     * @param team the team who earned the kill
     * @return the new amount of kills this team has
     */
    public int addTeamKill(Team team) {
        this.teamKills.putIfAbsent(team, 0);
        int current = this.teamKills.get(team) + 1;
        this.teamKills.put(team, current);
        return current;
    }

    public void setTeamKills(Map<Team, Integer> teamKills) {
        this.teamKills = teamKills;
    }

    /**
     * Add a death to a team, used for tracking winners and losers
     * @param team
     * @return the new death count
     */
    public int addTeamDeath(Team team){
        this.teamDeaths.putIfAbsent(team, 0);
        int current = this.teamDeaths.get(team) + 1;
        this.teamDeaths.put(team, current);
        return current;
    }

    public Map<Team, Integer> getTeamDeaths() {
        return teamDeaths;
    }

    public void setTeamDeaths(Map<Team, Integer> teamDeaths) {
        this.teamDeaths = teamDeaths;
    }

    public Map<Team, Integer> getTntUsed() {
        return tntUsed;
    }

    public void setTntUsed(Map<Team, Integer> tntUsed) {
        this.tntUsed = tntUsed;
    }

    public Map<UUID, Integer> getPlayerKills() {
        return playerKills;
    }

    public void addPlayerKill(UUID uuid){
        this.playerKills.putIfAbsent(uuid, 0);
        this.playerKills.put(uuid, this.playerKills.get(uuid) + 1);
    }

    public void setPlayerKills(Map<UUID, Integer> playerKills) {
        this.playerKills = playerKills;
    }

    public Map<UUID, Integer> getBlocksPlaced() {
        return blocksPlaced;
    }

    public void setBlocksPlaced(Map<UUID, Integer> blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }

    public Map<UUID, Double> getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(Map<UUID, Double> damageDealt) {
        this.damageDealt = damageDealt;
    }

    public Map<UUID, Double> getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(Map<UUID, Double> distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public Map<UUID, Double> getLongestKillDistance() {
        return longestKillDistance;
    }

    public void setLongestKillDistance(Map<UUID, Double> longestKillDistance) {
        this.longestKillDistance = longestKillDistance;
    }

    public Map<UUID, Integer> getKillStreaks() {
        return killStreaks;
    }

    public void setKillStreaks(Map<UUID, Integer> killStreaks) {
        this.killStreaks = killStreaks;
    }

    public Map<UUID, Integer> getPlayerRespawns() {
        return playerRespawns;
    }

    public void setPlayerRespawns(Map<UUID, Integer> playerRespawns) {
        this.playerRespawns = playerRespawns;
    }

    public Map<UUID, Integer> getHitsTaken() {
        return hitsTaken;
    }

    public void setHitsTaken(Map<UUID, Integer> hitsTaken) {
        this.hitsTaken = hitsTaken;
    }

    public Map<UUID, Long> getTimeAlive() {
        return timeAlive;
    }

    public void setTimeAlive(Map<UUID, Long> timeAlive) {
        this.timeAlive = timeAlive;
    }

    public Map<UUID, Integer> getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(Map<UUID, Integer> buttonPressed) {
        this.buttonPressed = buttonPressed;
    }

    public Map<UUID, Integer> getLeversFlicked() {
        return leversFlicked;
    }

    public void setLeversFlicked(Map<UUID, Integer> leversFlicked) {
        this.leversFlicked = leversFlicked;
    }

    public int getTntIgnited() {
        return tntIgnited;
    }

    public void setTntIgnited(int tntIgnited) {
        this.tntIgnited = tntIgnited;
    }

    public int getBlocksBroken() {
        return blocksBroken;
    }

    public void setBlocksBroken(int blocksBroken) {
        this.blocksBroken = blocksBroken;
    }

    public int getTntPlaced() {
        return tntPlaced;
    }

    public void setTntPlaced(int tntPlaced) {
        this.tntPlaced = tntPlaced;
    }

    public int getExplosionsTriggered() {
        return explosionsTriggered;
    }

    public void setExplosionsTriggered(int explosionsTriggered) {
        this.explosionsTriggered = explosionsTriggered;
    }

    public long getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(long gameDuration) {
        this.gameDuration = gameDuration;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public int getEnvironmentalKills() {
        return environmentalKills;
    }

    public void setEnvironmentalKills(int environmentalKills) {
        this.environmentalKills = environmentalKills;
    }

    public int getExplosivesRelatedKills() {
        return explosivesRelatedKills;
    }

    public void setExplosivesRelatedKills(int explosivesRelatedKills) {
        this.explosivesRelatedKills = explosivesRelatedKills;
    }

    public Map<UUID, Integer> getFallDamageTaken() {
        return fallDamageTaken;
    }

    public void setFallDamageTaken(Map<UUID, Integer> fallDamageTaken) {
        this.fallDamageTaken = fallDamageTaken;
    }
}

