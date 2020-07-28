package me.prostedeni.goodcraft.welcomeback.Files;

import me.prostedeni.goodcraft.welcomeback.WelcomeBack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class FriendlyHelp {

    private static File configFile;
    private static FileConfiguration fileConfiguration;

    public static void configSetup(){

        if (!Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder().exists()) {
            Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder().mkdir();
        }

        configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder(), "FriendlyHelp.yml");
        if (!configFile.exists()){
            try {
                configFile.createNewFile();

                new BukkitRunnable(){
                    @Override
                    public void run() {
                        writeDefaultConfig();

                        new BukkitRunnable(){
                            public void run(){
                                save();
                            }
                        }.runTaskLaterAsynchronously(WelcomeBack.getInstance(), 3);
                    }
                }.runTaskLaterAsynchronously(WelcomeBack.getInstance(), 5);
            } catch (java.io.IOException e){
                //nothing
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

    }

    public static void reloadConfig() throws IOException {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
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
                } catch (IOException | NullPointerException e) {
                    //nothing
                }
            }
        }.runTaskAsynchronously(WelcomeBack.getInstance());
    }

    public static void saveSynchronously(){
        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            //nothing
            Bukkit.broadcastMessage("Saving error");
            e.printStackTrace();
        }
    }

    public static void writeDefaultConfig() {

        fileConfiguration.set("Help.1", "&3------------ &6&lWelcomeBack &r&3------------");
        fileConfiguration.set("Help.2", "&b/welcomeback check &cchecks own balance");
        fileConfiguration.set("Help.3", "&b/welcomeback check {player} &cchecks balance");
        fileConfiguration.set("Help.4", "&b/welcomeback set {player} {amount} &csets balance");
        fileConfiguration.set("Help.5", "&b/welcomeback give {player} {amount} &cadds to balance");
        fileConfiguration.set("Help.6", "&b/welcomeback take {player} {amount} &ctakes from balance");
        fileConfiguration.set("Help.7", "&b/welcomeback help &cdisplays this message");
        fileConfiguration.set("Help.8", "&b/welcomeback reload &creloads config");
    }

    public static void messageHelp(Player player){
        for (String s : fileConfiguration.getConfigurationSection("Help").getKeys(false)){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String) fileConfiguration.get(("Help." + s))));
        }
    }

}
