package dev.tom.tntWars.config.item;


import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteItemNBT;
import dev.tom.tntWars.utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Optional;
import java.util.function.Function;

@ConfigSerializable
public class ItemEntry {

    public static String TNTWARS_ITEM_NBT_KEY = "tntwars";

    private String material;
    private int amount;

    public ItemEntry() {
    }

    public ItemEntry(String material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public String getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    protected Optional<ItemStack> parse(){
        Material mat = Material.getMaterial(material);
        if(mat == null) return Optional.empty();
        ItemStack item = Util.createItemStack(mat, mat.getMaxStackSize());
        NBT.modify(item, nbt -> {
            nbt.setBoolean(TNTWARS_ITEM_NBT_KEY, true);
        });
        return Optional.of(item);
    }
}
