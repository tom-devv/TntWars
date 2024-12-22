package dev.tom.tntWars.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class CustomEvent extends Event {

    private final int tickTimestamp;

    public CustomEvent(){
        this.tickTimestamp = Bukkit.getServer().getCurrentTick();
    }

    public int getTickTimestamp() {
        return tickTimestamp;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
