package dev.tom.tntWars.placeholders;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameState;
import dev.tom.tntWars.models.game.GameStats;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class TntPlaceholders extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "tntwars";
    }

    @Override
    public @NotNull String getAuthor() {
        return "tom";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        UUID uuid = player.getUniqueId();
        /**
         * Handle optionals and return early if one isn't found
         */
        Optional<Game> optionalGame = TntWarsPlugin.getGameController().getGameByPlayer(player.getPlayer());
        if(optionalGame.isEmpty()) return null;
        Game game = optionalGame.get();
        GameStats stats = game.getStats();
        Optional<Team> optionalTeam = game.getTeam(player);
        if(optionalTeam.isEmpty()) return null;
        Team team = optionalTeam.get();
        Integer teamNumber = team.getNumber();

        switch (params){
            case "game-id":
                return game.getGameId();
            case "game-state":
                return game.getStats().toString();
            case "map-name":
                return game.getMap().getName();
            case "team-number":
                return teamNumber.toString();
            case "team-lives-remaining":
                return stats.getTeamDeaths().get(team).toString();
            case "team-kills":
                return stats.getTeamKills().get(team).toString();
/*            case "time-elapsed":
                return stats.*/
        }

        return super.onPlaceholderRequest(player, params);
    }
}
