package me.prostedeni.goodcraft.welcomeback.Files;

import me.prostedeni.goodcraft.welcomeback.WelcomeBack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FriendlyMessages {

    private static File configFile;
    private static FileConfiguration fileConfiguration;

    public static void configSetup(){

        if (!Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder().exists()) {
            Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder().mkdir();
        }

        configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("WelcomeBack").getDataFolder(), "FriendlyMessages.yml");
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
            writeConfig(configFile, fileConfiguration.getString("Message1"), fileConfiguration.getString("Message2"), fileConfiguration.getString("Message3"), fileConfiguration.getString("Message4"), fileConfiguration.getString("Message5"), fileConfiguration.getString("CheckMessage"), fileConfiguration.getString("CheckOwnMessage"), fileConfiguration.getString("SetMessage"), fileConfiguration.getString("GiveMessage"), fileConfiguration.getString("TakeMessage"));
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
                    writeConfig(configFile, fileConfiguration.getString("Message1"), fileConfiguration.getString("Message2"), fileConfiguration.getString("Message3"), fileConfiguration.getString("Message4"), fileConfiguration.getString("Message5"), fileConfiguration.getString("CheckMessage"), fileConfiguration.getString("CheckOwnMessage"), fileConfiguration.getString("SetMessage"), fileConfiguration.getString("GiveMessage"), fileConfiguration.getString("TakeMessage"));
                } catch (IOException e) {
                    //nothing
                    Bukkit.broadcastMessage("Saving error");
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(WelcomeBack.getInstance());
    }

    public static void saveSynchronously(){
        try {
            fileConfiguration.save(configFile);
            writeConfig(configFile, fileConfiguration.getString("Message1"), fileConfiguration.getString("Message2"), fileConfiguration.getString("Message3"), fileConfiguration.getString("Message4"), fileConfiguration.getString("Message5"), fileConfiguration.getString("CheckMessage"), fileConfiguration.getString("CheckOwnMessage"), fileConfiguration.getString("SetMessage"), fileConfiguration.getString("GiveMessage"), fileConfiguration.getString("TakeMessage"));
        } catch (IOException | NullPointerException e) {
            //nothing
        }
    }

    public static String getMessage1(Integer count, String player){
        String first = (String) fileConfiguration.get("Message1");
        return ChatColor.translateAlternateColorCodes('&', first.replace("%count%", count.toString()).replace("%indicator%", FriendlyConfig.get().getString("Indicator")).replace("%friendliness%", FriendlyStorage.getFriendliness(player).toString()));
    }

    public static String getMessage2(){
        return ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString("Message2"));
    }

    public static String getMessage3(){
        return ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString("Message3"));
    }

    public static String getMessage4(){
        return ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString("Message4"));
    }

    public static String getMessage5(){
        return ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString("Message5"));
    }

    public static String getCheckMessage(String player){
        String first = (String) fileConfiguration.get("CheckMessage");
        return ChatColor.translateAlternateColorCodes('&',first.replace("%indicator%", FriendlyConfig.get().getString("Indicator")).replace("%player%", player).replace("%friendliness%", FriendlyStorage.getFriendliness(player).toString()));
    }

    public static String getCheckOwnMessage(String player){
        String first = (String) fileConfiguration.get("CheckOwnMessage");
        return ChatColor.translateAlternateColorCodes('&',first.replace("%indicator%", FriendlyConfig.get().getString("Indicator")).replace("%friendliness%", FriendlyStorage.getFriendliness(player).toString()));
    }

    public static String getSetMessage(String player, Integer count){
        String first = (String) fileConfiguration.get("SetMessage");
        return ChatColor.translateAlternateColorCodes('&', first.replace("%indicator%", FriendlyConfig.get().getString("Indicator")).replace("%player%", player).replace("%friendliness%", count.toString()));
    }

    public static String getGiveMessage(String player, Integer count){
        String first = (String) fileConfiguration.get("GiveMessage");
        return ChatColor.translateAlternateColorCodes('&', first.replace("%count%", count.toString()).replace("%indicator%",FriendlyConfig.get().getString("Indicator")).replace("%player%", player));
    }

    public static String getTakeMessage(String player, Integer count){
        String first = (String) fileConfiguration.get("TakeMessage");
        return ChatColor.translateAlternateColorCodes('&', first.replace("%count%", count.toString()).replace("%indicator%", FriendlyConfig.get().getString("Indicator")).replace("%player%", player));
    }

    public static void writeDefaultConfig(File file) throws IOException {

        FileWriter writer = new FileWriter(file);
        writer.write("# Message after saying wb\n");
        writer.write("Message1: '&3You recieved &6%count% &3%indicator% &b(You have &6%friendliness% &bin total)'\n");
        writer.write("# Message after invalid arguments\n");
        writer.write("Message2: '&4Invalid arguments. See /welcomeback help'\n");
        writer.write("# Message if player doesn't have permissions\n");
        writer.write("Message3: '&4Insufficient permissions'\n");
        writer.write("# Message after reloading the config\n");
        writer.write("Message4: '&3Config and Message files &6reloaded &3succesfully'\n");
        writer.write("# Error message after attempted overdraft\n");
        writer.write("Message5: '&4Friendliness cannot go into negative numbers'\n");
        writer.write("# Message after /welcomeback check (player) command\n");
        writer.write("CheckMessage: '&3%indicator% of &3%player% &3is &6%friendliness%'\n");
        writer.write("# Message after /welcomeback check command\n");
        writer.write("CheckOwnMessage: '&3Your &3%indicator% &3is &6%friendliness%'\n");
        writer.write("# Message after /welcomeback set command\n");
        writer.write("SetMessage: '&3%indicator% of &3%player% &3set to &6%friendliness%'\n");
        writer.write("# Message after /welcomeback give command\n");
        writer.write("GiveMessage: '&6%count% &3%indicator% &3has been given to &3%player%'\n");
        writer.write("# Message after /welcomeback take command\n");
        writer.write("TakeMessage: '&6%count% &3%indicator% &3has been taken from &3%player%'\n");
        writer.flush();
    }

    public static void writeConfig(File file, String Message1, String Message2, String Message3, String Message4, String Message5, String CheckMessage, String CheckOwnMessage, String SetMessage, String GiveMessage, String TakeMessage) throws IOException {

        FileWriter writer = new FileWriter(file);
        writer.write("# Message after saying wb\n");
        writer.write("Message1: '" + Message1 + "'\n");
        writer.write("# Message after invalid arguments\n");
        writer.write("Message2: '" + Message2 + "'\n");
        writer.write("# Message if player doesn't have permissions\n");
        writer.write("Message3: '" + Message3 + "'\n");
        writer.write("# Message after reloading the config\n");
        writer.write("Message4: '" + Message4 + "'\n");
        writer.write("# Error message after attempted overdraft\n");
        writer.write("Message5: '" + Message5 + "'\n");
        writer.write("# Message after /welcomeback check (player) command\n");
        writer.write("CheckMessage: '" + CheckMessage + "'\n");
        writer.write("# Message after /welcomeback check command\n");
        writer.write("CheckOwnMessage: '" + CheckOwnMessage + "'\n");
        writer.write("# Message after /welcomeback set command\n");
        writer.write("SetMessage: '" + SetMessage + "'\n");
        writer.write("# Message after /welcomeback give command\n");
        writer.write("GiveMessage: '" + GiveMessage + "'\n");
        writer.write("# Message after /welcomeback take command\n");
        writer.write("TakeMessage: '" + TakeMessage + "'\n");
        writer.flush();
    }

}
