package me.prostedeni.goodcraft.welcomeback;

import me.prostedeni.goodcraft.welcomeback.Files.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public final class WelcomeBack extends JavaPlugin implements Listener {

    private static WelcomeBack instance;

    public static WelcomeBack getInstance() {
        return instance;
    }

    public static HashMap<String, Integer> smallTimer;
    public static HashMap<String, Integer> smallTimer2;
    public static ArrayList<String> smallTimerBeta;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        FriendlyStorage.configSetup();
        FriendlyConfig.configSetup();
        FriendlyTimeList.configSetup();
        FriendlyHelp.configSetup();
        FriendlyMessages.configSetup();

        smallTimer = new HashMap<>();
        smallTimer2 = new HashMap<>();
        smallTimerBeta = new ArrayList<>();

        getServer().getPluginManager().registerEvents(this, this);

        this.getCommand("welcomeback").setExecutor(new FriendlyCommand());
        this.getCommand("welcomeback").setTabCompleter(new TabCompleter());

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new WelcomeBackExpansion(this).register();
        }

        repeatingStuff();
    }

    public static void repeatingStuff(){
        if (FriendlyConfig.get().getInt("RejoinDelay") > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    FriendlyTimeList.RepeatingTask();
                }
            }.runTaskTimerAsynchronously(getInstance(), 40, 40);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        FriendlyStorage.saveSynchronously();
        FriendlyConfig.saveSynchronously();
        FriendlyTimeList.saveSynchronously();
        FriendlyHelp.saveSynchronously();
        FriendlyMessages.saveSynchronously();
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent e){

        if (!(FriendlyTimeList.getAllPlayers().contains(e.getPlayer().getName()))) {

            smallTimer.put(e.getPlayer().getName(), ((int) (System.currentTimeMillis() / 1000)));

            if (FriendlyConfig.get().getInt("RewardTimeout") != 0) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (smallTimer.containsKey(e.getPlayer().getName())) {
                            smallTimer.remove(e.getPlayer().getName());
                        } else if (smallTimer2.containsKey(e.getPlayer().getName())){
                            smallTimer2.remove(e.getPlayer().getName());
                        }
                    }
                }.runTaskLaterAsynchronously(this, ((FriendlyConfig.get().getInt("RewardTimeout")) * 20));
            }
        }
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e){

        String message = ChatColor.stripColor(e.getMessage());

        if (message.equalsIgnoreCase("wb")){
            if (!(smallTimerBeta.contains(e.getPlayer().getName()))) {
                if (!smallTimer.isEmpty() || !smallTimer2.isEmpty()) {
                    int count = 0;
                    if (!(FriendlyConfig.get().getBoolean("MultipleRewards"))) {

                        for (Player s : Bukkit.getOnlinePlayers()) {
                            if (!s.equals(e.getPlayer())) {
                                if (smallTimer.containsKey(s.getName())) {
                                    if ((java.lang.System.currentTimeMillis() / 1000) - smallTimer.get(s.getName()) <= FriendlyConfig.get().getInt("RewardTimeout")) {
                                        count++;
                                        smallTimer.remove(s.getName());
                                        if (FriendlyConfig.get().getInt("RejoinDelay") > 0) {
                                            FriendlyTimeList.addPlayer(s.getName());
                                        }
                                    } else if ((java.lang.System.currentTimeMillis() / 1000) - smallTimer.get(s.getName()) > FriendlyConfig.get().getInt("RewardTimeout")) {
                                        smallTimer.remove(s.getName());
                                    }
                                }
                            }
                        }
                        //smallTimer stores joinTime and this determines if player gets a point or two

                        if (count > 0) {
                            FriendlyStorage.addFriendliness(count, e.getPlayer().getName());
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You recieved &6" + String.valueOf(count) + " &3" + FriendlyConfig.get().getString("Indicator") + " &b(You have &6" + String.valueOf(FriendlyStorage.getFriendliness(e.getPlayer().getName())) + "&b in total)"));
                        }
                        //if player got a point, this adds it to storage and tells him

                        smallTimerBeta.add(e.getPlayer().getName());
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                smallTimerBeta.remove(e.getPlayer().getName());
                            }
                        }.runTaskLaterAsynchronously(this, (FriendlyConfig.get().getInt("RewardDelay") * 20));
                        //this excludes player from participating in getting rewards for RewardDelay time

                    } else if (FriendlyConfig.get().getBoolean("MultipleRewards")){

                        for (Player s : Bukkit.getOnlinePlayers()) {
                            if (!s.equals(e.getPlayer())) {
                                if (smallTimer2.containsKey(s.getName())) {
                                    if ((java.lang.System.currentTimeMillis() / 1000) - smallTimer2.get(s.getName()) <= FriendlyConfig.get().getInt("RewardTimeout")) {

                                        count = count + FriendlyConfig.get().getInt("OtherPoints");

                                    } else if ((java.lang.System.currentTimeMillis() / 1000) - smallTimer2.get(s.getName()) > FriendlyConfig.get().getInt("RewardTimeout")) {
                                        smallTimer2.remove(s.getName());
                                    }
                                }
                                if (smallTimer.containsKey(s.getName())) {
                                    if ((java.lang.System.currentTimeMillis() / 1000) - smallTimer.get(s.getName()) <= FriendlyConfig.get().getInt("RewardTimeout")) {

                                        count = count + FriendlyConfig.get().getInt("FirstPoints");

                                        smallTimer2.put(s.getName(), smallTimer.get(s.getName()));
                                        smallTimer.remove(s.getName());

                                        if (FriendlyConfig.get().getInt("RejoinDelay") > 0) {
                                            FriendlyTimeList.addPlayer(s.getName());
                                        }

                                    } else if ((java.lang.System.currentTimeMillis() / 1000) - smallTimer.get(s.getName()) > FriendlyConfig.get().getInt("RewardTimeout")) {
                                        smallTimer.remove(s.getName());
                                    }
                                }
                            }
                        }

                        if (count > 0) {
                            FriendlyStorage.addFriendliness(count, e.getPlayer().getName());
                            e.getPlayer().sendMessage(FriendlyMessages.getMessage1(count, e.getPlayer().getName()));
                        }

                        smallTimerBeta.add(e.getPlayer().getName());
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                smallTimerBeta.remove(e.getPlayer().getName());
                            }
                        }.runTaskLaterAsynchronously(this, (FriendlyConfig.get().getInt("RewardDelay") * 20));
                    }
                }
            }
        }
    }

}

