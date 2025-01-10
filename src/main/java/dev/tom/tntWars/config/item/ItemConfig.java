package dev.tom.tntWars.config.item;

import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class ItemConfig {

    private String name;// = defaultItemConfig().getName();
    private List<ItemEntry> items;// = defaultItemConfig().getItems();

    // List of actual itemstacks
    private transient List<ItemStack> parsedItems = new ArrayList<>();

    public ItemConfig() {
    }

    public ItemConfig(String name, List<ItemEntry> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    protected List<ItemEntry> getItems() {
        return items;
    }

    public static ItemConfig defaultItemConfig() {
        return new ItemConfig(
                "example_items",
                java.util.List.of(
                        new ItemEntry("TNT", 64),
                        new ItemEntry("REDSTONE", 64),
                        new ItemEntry("FLINT_AND_STEEL", 1),
                        new ItemEntry("WATER_BUCKET", 1)
                )
        );
    }

    /**
     * Parse material strings into real ItemStacks
     */
    protected void parse(){
        for (ItemEntry item : this.items) {
            item.parse().ifPresent(parsedItems::add);
        }
    }

    public List<ItemStack> getParsedItems() {
        return parsedItems;
    }
}
