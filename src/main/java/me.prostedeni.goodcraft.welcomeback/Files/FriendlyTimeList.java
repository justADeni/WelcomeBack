package me.prostedeni.goodcraft.welcomeback.Files;

import me.prostedeni.goodcraft.welcomeback.WelcomeBack;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FriendlyTimeList {

        private static File storageFile;
        private static FileConfiguration fileConfiguration;

        public static void configSetup() {

            if (!Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder().exists()) {
                Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder().mkdir();
            }

            File generalDataFolder = new File(Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder(), "DataStorage");
            if (!generalDataFolder.exists()) {
                generalDataFolder.mkdirs();
            }

            storageFile = new File(generalDataFolder, "FriendlyList.yml");
            if (!storageFile.exists()) {
                try {
                    storageFile.createNewFile();
                } catch (java.io.IOException e) {
                    //nothing
                }
            }
            fileConfiguration = YamlConfiguration.loadConfiguration(storageFile);
        }

        public static FileConfiguration get() {
            return fileConfiguration;
        }

        public static void save() {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        fileConfiguration.save(storageFile);

                    } catch (IOException | NullPointerException e) {
                        //nothing
                    }
                }
            }.runTaskAsynchronously(WelcomeBack.getInstance());
        }

        public static void saveSynchronously() {
            try {
                fileConfiguration.save(storageFile);

            } catch (IOException e) {
                //nothing
                System.out.println("Saving error");
                e.printStackTrace();
            }
        }

        public static void addPlayer(String player) {
            if (FriendlyConfig.get().getInt("RejoinDelay") != 0) {
                fileConfiguration.set("Data." + player, (java.lang.System.currentTimeMillis() / 1000));
            }
            save();
        }

        public static void removePlayer(String player) {
            fileConfiguration.set("Data." + player, null);
            save();
        }

        public static ArrayList<String> getAllPlayers(){
            ArrayList<String> players = new ArrayList<String>();
            try {
                players.addAll(fileConfiguration.getConfigurationSection("Data.").getKeys(false));
            } catch (NullPointerException err){
                //nothing
            }
            return players;
        }

        public static Integer getPlayerTime(String player){
            try {
                return fileConfiguration.getInt("Data." + player);
            } catch (NullPointerException err){
                return 0;
            }
        }

        public static void RepeatingTask(){
            if (FriendlyConfig.get().getInt("RejoinDelay") != 0) {
                for (String s : getAllPlayers()) {
                    if ((java.lang.System.currentTimeMillis() / 1000) - getPlayerTime(s) >= FriendlyConfig.get().getInt("RejoinDelay")) {
                        removePlayer(s);
                    }
                }
                save();
            } else if (FriendlyConfig.get().getInt("RejoinDelay") == 0) {
                try {
                    for (String s : FriendlyTimeList.get().getConfigurationSection("Data").getKeys(false)) {
                        FriendlyTimeList.get().set("Data." + s, null);
                    }
                    save();
                } catch (NullPointerException err){
                    //nothing
                }
            }
        }
}
