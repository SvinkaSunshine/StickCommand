package ru.svinka.stickcommand;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.svinka.stickcommand.commands.SCCommand;
import ru.svinka.stickcommand.listeners.StickListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickCommand extends JavaPlugin {
    private static StickCommand instance;
    private Map<String, StickData> sticks = new HashMap<>();
    private FileConfiguration messages;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadSticks();

        createMessagesFile();

        getCommand("sc").setExecutor(new SCCommand());
        getServer().getPluginManager().registerEvents(new StickListener(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void reload() {
        reloadConfig();
        loadSticks();
        loadMessages();
    }

    private void loadSticks() {
        sticks.clear();
        FileConfiguration config = getConfig();
        for (String key : config.getConfigurationSection("StickCommand").getKeys(false)) {
            String name = config.getString("StickCommand." + key + ".name");
            String command = config.getString("StickCommand." + key + ".command");
            boolean enchanting = config.getBoolean("StickCommand." + key + ".enchanting", false);
            List<String> lore = config.getStringList("StickCommand." + key + ".lore");

            sticks.put(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name)), new StickData(name, command, enchanting, lore));
        }
    }

    private void createMessagesFile() {
        File messagesFolder = new File(getDataFolder(), "messages");
        if (!messagesFolder.exists()) {
            messagesFolder.mkdirs();
        }

        File enFile = new File(messagesFolder, "en.yml");
        if (!enFile.exists()) {
            saveResource("messages/en.yml", false);
        }

        File ruFile = new File(messagesFolder, "ru.yml");
        if (!ruFile.exists()) {
            saveResource("messages/ru.yml", false);
        }

        loadMessages();
    }

    private void loadMessages() {
        String langFileName = getConfig().getString("language", "en.yml");
        File messagesFile = new File(getDataFolder(), "messages/" + langFileName);
        if (!messagesFile.exists()) {
            getLogger().severe("The language file " + langFileName + " was not found. Using default messages.");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getMessage(String key) {
        return messages.getString("messages." + key, "Message not found: " + key);
    }

    public Map<String, StickData> getSticks() {
        return sticks;
    }

    public static StickCommand getInstance() {
        return instance;
    }

    public static class StickData {
        private final String name;
        private final String command;
        private final boolean enchanting;
        private final List<String> lore;

        public StickData(String name, String command, boolean enchanting, List<String> lore) {
            this.name = name;
            this.command = command;
            this.enchanting = enchanting;
            this.lore = lore;
        }

        public String getName() {
            return name;
        }

        public String getCommand() {
            return command;
        }

        public boolean isEnchanting() {
            return enchanting;
        }

        public List<String> getLore() {
            return lore;
        }
    }
}
