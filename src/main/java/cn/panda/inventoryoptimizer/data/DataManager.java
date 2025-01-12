package cn.panda.inventoryoptimizer.data;

import cn.panda.inventoryoptimizer.InventoryOptimizer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Material;

public class DataManager {

    private final InventoryOptimizer plugin;
    private final Map<UUID, PlayerData> playerDataCache = new HashMap<>();
    private final File dataFolder;

    public DataManager(InventoryOptimizer plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
    }

    public PlayerData getOrCreatePlayerData(Player player) {
        return playerDataCache.computeIfAbsent(player.getUniqueId(), uuid -> loadPlayerData(player.getUniqueId()));
    }

    public PlayerData getPlayerData(UUID playerId) {
        return playerDataCache.get(playerId);
    }

    public void savePlayerData(PlayerData playerData) {
        File playerFile = new File(dataFolder, playerData.getPlayerId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        // 保存物品使用计数
        playerData.getItemUsageCount().forEach((material, count) -> config.set("itemUsage." + material.toString(), count));

        // 保存槽位偏好
        playerData.getSlotPreference().forEach((material, slotCounts) ->
                slotCounts.forEach((slot, count) -> config.set("slotPreference." + material.toString() + "." + slot, count))
        );

        config.set("learningComplete", playerData.isLearningComplete());

        try {
            config.save(playerFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法保存玩家数据 " + playerData.getPlayerId() + ": " + e.getMessage());
        }
    }

    public PlayerData loadPlayerData(UUID playerId) {
        File playerFile = new File(dataFolder, playerId + ".yml");
        if (!playerFile.exists()) {
            return new PlayerData(playerId);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        PlayerData playerData = new PlayerData(playerId);

        // 加载物品使用计数
        if (config.getConfigurationSection("itemUsage") != null) {
            for (String key : config.getConfigurationSection("itemUsage").getKeys(false)) {
                try {
                    Material material = Material.valueOf(key);
                    playerData.getItemUsageCount().put(material, config.getInt("itemUsage." + key));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("无效的物品类型在玩家数据中: " + key);
                }
            }
        }

        // 加载槽位偏好
        if (config.getConfigurationSection("slotPreference") != null) {
            for (String materialKey : config.getConfigurationSection("slotPreference").getKeys(false)) {
                try {
                    Material material = Material.valueOf(materialKey);
                    if (config.getConfigurationSection("slotPreference." + materialKey) != null) {
                        for (String slotKey : config.getConfigurationSection("slotPreference." + materialKey).getKeys(false)) {
                            try {
                                int slot = Integer.parseInt(slotKey);
                                playerData.incrementSlotPreference(material, slot);
                            } catch (NumberFormatException e) {
                                plugin.getLogger().warning("无效的槽位编号在玩家数据中: " + slotKey);
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("无效的物品类型在玩家偏好数据中: " + materialKey);
                }
            }
        }

        playerData.setLearningComplete(config.getBoolean("learningComplete", false));

        playerDataCache.put(playerId, playerData);
        return playerData;
    }

    public void unloadPlayerData(UUID playerId) {
        playerDataCache.remove(playerId);
    }
}