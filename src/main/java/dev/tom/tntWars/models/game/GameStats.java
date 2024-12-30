package dev.tom.tntWars.models;

import dev.tom.tntWars.models.Team;

import java.util.Map;
import java.util.UUID;

public record GameStats(
        // Team-specific stats
        Map<Team, Integer> teamKills,
        Map<Team, Integer> teamDeaths,
        Map<Team, Integer> tntUsed,

        // Player-specific stats
        Map<UUID, Integer> playerKills,
        Map<UUID, Integer> playerDeaths,
        Map<UUID, Integer> blocksPlaced,
        Map<UUID, Double> damageDealt,
        Map<UUID, Double> distanceTraveled,
        Map<UUID, Double> longestKillDistance,
        Map<UUID, Integer> killStreaks,
        Map<UUID, Integer> playerRespawns,
        Map<UUID, Integer> hitsTaken,
        Map<UUID, Long> timeAlive, // in milliseconds
        Map<UUID, Integer> buttonPressed,
        Map<UUID, Integer> leversFlicked,

        // General game stats
        int tntIgnited,
        int blocksBroken,
        int tntPlaced,
        int explosionsTriggered,
        long gameDuration, // in milliseconds
        Team winningTeam,

        // Environmental stats
        int environmentalKills,
        int explosivesRelatedKills,
        Map<UUID, Integer> fallDamageTaken
) {}
