package cn.panda.inventoryoptimizer.data;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private UUID playerId;
    private Map<Material, Integer> itemUsageCount = new HashMap<>();
    private Map<Material, Map<Integer, Integer>> slotPreference = new HashMap<>(); // Material -> Slot -> Count
    private boolean learningComplete = false;

    public PlayerData(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Map<Material, Integer> getItemUsageCount() {
        return itemUsageCount;
    }

    public int getItemUsageCount(Material material) {
        return itemUsageCount.getOrDefault(material, 0);
    }

    public void incrementItemUsage(Material material) {
        itemUsageCount.put(material, itemUsageCount.getOrDefault(material, 0) + 1);
    }

    public Map<Material, Map<Integer, Integer>> getSlotPreference() {
        return slotPreference;
    }

    public int getSlotPreference(Material material, int slot) {
        return slotPreference.getOrDefault(material, new HashMap<>()).getOrDefault(slot, 0);
    }

    public void incrementSlotPreference(Material material, int slot) {
        slotPreference.computeIfAbsent(material, k -> new HashMap<>());
        slotPreference.get(material).put(slot, slotPreference.get(material).getOrDefault(slot, 0) + 1);
    }

    public boolean isLearningComplete() {
        return learningComplete;
    }

    public void setLearningComplete(boolean learningComplete) {
        this.learningComplete = learningComplete;
    }
}