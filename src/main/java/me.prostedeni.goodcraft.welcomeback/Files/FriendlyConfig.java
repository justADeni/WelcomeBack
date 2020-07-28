package me.prostedeni.goodcraft.welcomeback.Files;

import me.prostedeni.goodcraft.welcomeback.WelcomeBack;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;

public class FriendlyConfig {

    private static File configFile;
    private static FileConfiguration fileConfiguration;

    public static void configSetup(){

        if (!Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder().exists()) {
            Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder().mkdir();
        }

        configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder(), "FriendlyConfig.yml");
        if (!configFile.exists()){
            try {
                configFile.createNewFile();

                writeDefaultConfig(configFile);

                new BukkitRunnable(){
                    public void run(){
                        save();
                    }
                }.runTaskLaterAsynchronously(WelcomeBack.getInstance(), 3);
            } catch (java.io.IOException e){
                //nothing
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

    }

    public static void reloadConfig() throws IOException {
        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
            writeConfig(configFile, fileConfiguration.getString("Indicator"), fileConfiguration.getInt("RejoinDelay"), fileConfiguration.getInt("RewardDelay"), fileConfiguration.getInt("RewardTimeout"), fileConfiguration.getBoolean("MultipleRewards"), fileConfiguration.getInt("FirstPoints"), fileConfiguration.getInt("OtherPoints"));
        } catch (IOException err){
            //nothing
            Bukkit.broadcastMessage("Saving error");
            err.printStackTrace();
        }
    }

    public static FileConfiguration get(){
        return fileConfiguration;
    }

    public static void save(){
        new BukkitRunnable(){
            @Override
            public void run(){
                try {
                    fileConfiguration.save(configFile);
                    writeConfig(configFile, fileConfiguration.getString("Indicator"), fileConfiguration.getInt("RejoinDelay"), fileConfiguration.getInt("RewardDelay"), fileConfiguration.getInt("RewardTimeout"), fileConfiguration.getBoolean("MultipleRewards"), fileConfiguration.getInt("FirstPoints"), fileConfiguration.getInt("OtherPoints"));
                } catch (IOException | NullPointerException e) {
                    //nothing
                }
            }
        }.runTaskAsynchronously(WelcomeBack.getInstance());
    }

    public static void saveSynchronously(){
        try {
            fileConfiguration.save(configFile);
            writeConfig(configFile, fileConfiguration.getString("Indicator"), fileConfiguration.getInt("RejoinDelay"), fileConfiguration.getInt("RewardDelay"), fileConfiguration.getInt("RewardTimeout"), fileConfiguration.getBoolean("MultipleRewards"), fileConfiguration.getInt("FirstPoints"), fileConfiguration.getInt("OtherPoints"));
        } catch (IOException e) {
            //nothing
            Bukkit.broadcastMessage("Saving error");
            e.printStackTrace();
        }
    }

    public static void writeDefaultConfig(File file) throws IOException {

        FileWriter writer = new FileWriter(file);
        writer.write("# The name of the economy balance that is awarded when saying wb\n");
        writer.write("Indicator: 'Friendliness'\n");
        writer.write("# The delay between player rejoining others being able to get rewarded for wb-ing him (in seconds)\n");
        writer.write("RejoinDelay: 3600\n");
        writer.write("# The delay between being able to get rewarded for saying wb (in seconds) NOT recommended to set below RewardTimeout\n");
        writer.write("RewardDelay: 5\n");
        writer.write("# The max. time player can get rewarded in for saying wb after player joins (in seconds)\n");
        writer.write("RewardTimeout: 5\n");
        writer.write("# If true, more than one player can say wb and get reward after one player joins within RewardTimeout\n");
        writer.write("MultipleRewards: false\n");
        writer.write("# How many points does the first one to say wb recieve\n");
        writer.write("FirstPoints: 2\n");
        writer.write("# How many points do other players recieve\n");
        writer.write("OtherPoints: 1\n");
        writer.flush();
    }

    public static void writeConfig(File file, String Indicator, Integer RejoinDelay, Integer RewardDelay, Integer RewardTimeout, Boolean MultiplePoints, Integer First, Integer Others) throws IOException {

        FileWriter writer = new FileWriter(file);
        writer.write("# The name of the economy balance that is awarded when saying wb\n");
        writer.write("Indicator: " + Indicator + "\n");
        writer.write("# The delay between player rejoining others being able to get rewarded for wb-ing him (in seconds)\n");
        writer.write("RejoinDelay: " + RejoinDelay.toString() + "\n");
        writer.write("# The delay between being able to get rewarded for saying wb (in seconds) NOT recommended to set below RewardTimeout\n");
        writer.write("RewardDelay: " + RewardDelay.toString() + "\n");
        writer.write("# The max. time player can get rewarded in for saying wb after player joins (in seconds)\n");
        writer.write("RewardTimeout: " + RewardTimeout.toString() + "\n");
        writer.write("# If true, more than one player can say wb and get reward after one player joins within RewardTimeout\n");
        writer.write("MultipleRewards: " + MultiplePoints.toString() + "\n");
        writer.write("# How many points does the first one to say wb recieve\n");
        writer.write("FirstPoints: " + First.toString() + "\n");
        writer.write("# How many points do other players recieve\n");
        writer.write("OtherPoints: " + Others.toString() + "\n");
        writer.flush();
    }
}
