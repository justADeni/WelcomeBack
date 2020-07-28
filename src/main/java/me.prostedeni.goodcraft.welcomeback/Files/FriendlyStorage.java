package me.prostedeni.goodcraft.welcomeback.Files;

import me.prostedeni.goodcraft.welcomeback.WelcomeBack;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class FriendlyStorage {

    private static File storageFile;
    private static FileConfiguration fileConfiguration;

    public static void configSetup(){

        if (!Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder().exists()) {
            Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder().mkdir();
        }

        File generalDataFolder = new File(Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder(), "DataStorage");
        if(!generalDataFolder.exists()) {
            generalDataFolder.mkdirs();
        }

        storageFile = new File(generalDataFolder, "FriendlyData.yml");
        if (!storageFile.exists()){
            try {
                storageFile.createNewFile();
            } catch (java.io.IOException e){
                //nothing
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(storageFile);
    }

    public static FileConfiguration get(){
        return fileConfiguration;
    }

    public static void save(){
        new BukkitRunnable(){
            @Override
            public void run(){
                try {
                    fileConfiguration.save(storageFile);

                } catch (IOException | NullPointerException e) {
                    //nothing
                }
            }
        }.runTaskAsynchronously(WelcomeBack.getInstance());
    }

    public static void saveSynchronously(){
        try {
            fileConfiguration.save(storageFile);

        } catch (IOException e) {
            //nothing
            System.out.println("Saving error");
            e.printStackTrace();
        }
    }

    public static Integer getFriendliness(String player){
        try {
            if (fileConfiguration.get(player) != null) {
                return fileConfiguration.getInt(player);
            } else {
                return 0;
            }
        } catch (NullPointerException err){
            return 0;
        }
    }

    public static void setFriendliness(Integer amount, String player){
        fileConfiguration.set(player, amount);
        save();
    }

    public static void addFriendliness(Integer amount, String player){
        fileConfiguration.set(player, (amount + fileConfiguration.getInt(player)));
        save();
    }

    public static void takeFriendliness(Integer amount, String player){
        fileConfiguration.set(player, (fileConfiguration.getInt(player) - amount));
        save();
    }
}
