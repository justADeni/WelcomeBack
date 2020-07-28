package me.prostedeni.goodcraft.welcomeback;

import me.prostedeni.goodcraft.welcomeback.Files.FriendlyConfig;
import me.prostedeni.goodcraft.welcomeback.Files.FriendlyHelp;
import me.prostedeni.goodcraft.welcomeback.Files.FriendlyMessages;
import me.prostedeni.goodcraft.welcomeback.Files.FriendlyStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class FriendlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("welcomeback")){

            if (sender instanceof Player) {
                //player

                if (sender.hasPermission("welcomeback.admin") || sender.isOp() || sender.hasPermission("welcomeback.checkown")) {
                    if (args.length == 0) {
                        sender.sendMessage(FriendlyMessages.getMessage2());
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("help")) {
                            if (sender.hasPermission("welcomeback.admin")) {
                                FriendlyHelp.messageHelp((Player) sender);
                            } else {
                                sender.sendMessage(FriendlyMessages.getMessage3());
                            }
                        } else if (args[0].equalsIgnoreCase("reload")) {
                            if (sender.hasPermission("welcomeback.admin")) {
                                try {
                                    FriendlyConfig.reloadConfig();
                                    FriendlyConfig.save();

                                    FriendlyMessages.reloadConfig();
                                    FriendlyMessages.save();

                                    FriendlyHelp.reloadConfig();
                                    FriendlyHelp.save();

                                    WelcomeBack.repeatingStuff();

                                    sender.sendMessage(FriendlyMessages.getMessage4());
                                } catch (IOException e) {
                                    System.out.println("Saving error");
                                    e.printStackTrace();
                                }
                            } else {
                                sender.sendMessage(FriendlyMessages.getMessage3());
                            }
                        } else if (args[0].equalsIgnoreCase("check")) {

                            if (sender.hasPermission("welcomeback.checkown")){
                                sender.sendMessage(FriendlyMessages.getCheckOwnMessage(sender.getName()));
                            } else {
                                sender.sendMessage(FriendlyMessages.getMessage3());
                            }

                        }else if ((!args[0].equalsIgnoreCase("help")) && (!args[0].equalsIgnoreCase("reload")) && (!args[0].equalsIgnoreCase("check"))) {
                            sender.sendMessage(FriendlyMessages.getMessage2());
                        }
                    } else if (args.length == 2) {
                        if (sender.hasPermission("welcomeback.admin")) {
                            if (args[0].equalsIgnoreCase("check")) {
                                sender.sendMessage(FriendlyMessages.getCheckMessage(args[1]));
                            } else if (!args[0].equalsIgnoreCase("check")) {
                                sender.sendMessage(FriendlyMessages.getMessage2());
                            }
                        } else {
                            sender.sendMessage(FriendlyMessages.getMessage3());
                        }
                    } else if (args.length == 3) {
                        if (sender.hasPermission("welcomeback.admin")) {
                            if (args[0].equalsIgnoreCase("set")) {
                                if (isInteger(args[2])) {
                                    FriendlyStorage.setFriendliness(Integer.parseInt(args[2]), args[1]);
                                    sender.sendMessage(FriendlyMessages.getSetMessage(args[1], Integer.parseInt(args[2])));
                                } else {
                                    sender.sendMessage(FriendlyMessages.getMessage2());
                                }
                            } else if (args[0].equalsIgnoreCase("give")) {
                                if (isInteger(args[2])) {

                                    FriendlyStorage.addFriendliness(Integer.parseInt(args[2]), args[1]);
                                    sender.sendMessage(FriendlyMessages.getGiveMessage(args[1], Integer.parseInt(args[2])));
                                } else {
                                    sender.sendMessage(FriendlyMessages.getMessage2());
                                }
                            } else if (args[0].equalsIgnoreCase("take")) {
                                if (isInteger(args[2])) {
                                    if ((FriendlyStorage.getFriendliness(args[1])) - Integer.parseInt(args[2]) >= 0) {
                                        FriendlyStorage.takeFriendliness(Integer.parseInt(args[2]), args[1]);
                                        sender.sendMessage(FriendlyMessages.getTakeMessage(args[1], Integer.parseInt(args[2])));
                                    } else if ((FriendlyStorage.getFriendliness(args[1])) - Integer.parseInt(args[2]) < 0) {
                                        sender.sendMessage(FriendlyMessages.getMessage5());
                                    }
                                } else {
                                    sender.sendMessage(FriendlyMessages.getMessage2());
                                }
                            } else if ((!args[0].equalsIgnoreCase("set")) && (!args[0].equalsIgnoreCase("give")) && (!args[0].equalsIgnoreCase("take"))) {
                                sender.sendMessage(FriendlyMessages.getMessage2());
                            }
                        } else {
                            sender.sendMessage(FriendlyMessages.getMessage3());
                        }
                    } else if (args.length > 3) {
                        if (sender.hasPermission("welcomeback.admin")) {
                            sender.sendMessage(FriendlyMessages.getMessage2());
                            //invalid args
                        } else {
                            sender.sendMessage(FriendlyMessages.getMessage3());
                        }
                    }
                } else {
                    sender.sendMessage(FriendlyMessages.getMessage3());
                    //no perms
                }
            } else if (!(sender instanceof Player)){
                //console

                if (args.length == 0) {
                    sender.sendMessage(FriendlyMessages.getMessage2());
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        for (String s : FriendlyHelp.get().getConfigurationSection("Help").getKeys(false)){
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FriendlyHelp.get().getString(("Help." + s))));
                        }
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        try {
                            FriendlyConfig.reloadConfig();
                            FriendlyConfig.save();

                            FriendlyMessages.reloadConfig();
                            FriendlyMessages.save();

                            FriendlyHelp.reloadConfig();
                            FriendlyHelp.save();

                            sender.sendMessage(FriendlyMessages.getMessage4());
                        } catch (IOException e) {
                            System.out.println("Saving error");
                            e.printStackTrace();
                        }
                    } else if ((!args[0].equalsIgnoreCase("help")) && (!args[0].equalsIgnoreCase("reload"))) {
                        sender.sendMessage(FriendlyMessages.getMessage2());
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("check")) {
                        sender.sendMessage(FriendlyMessages.getCheckMessage(args[1]));
                    } else if (!args[0].equalsIgnoreCase("check")) {
                        sender.sendMessage(FriendlyMessages.getMessage2());
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("set")) {
                        if (isInteger(args[2])) {
                            FriendlyStorage.setFriendliness(Integer.parseInt(args[2]), args[1]);
                            sender.sendMessage(FriendlyMessages.getSetMessage(args[1], Integer.parseInt(args[2])));
                        } else {
                            sender.sendMessage(FriendlyMessages.getMessage2());
                        }
                    } else if (args[0].equalsIgnoreCase("give")) {
                        if (isInteger(args[2])) {

                            FriendlyStorage.addFriendliness(Integer.parseInt(args[2]), args[1]);
                            sender.sendMessage(FriendlyMessages.getGiveMessage(args[1], Integer.parseInt(args[2])));
                        } else {
                            sender.sendMessage(FriendlyMessages.getMessage2());
                        }
                    } else if (args[0].equalsIgnoreCase("take")) {
                        if (isInteger(args[2])) {
                            if ((FriendlyStorage.getFriendliness(args[1])) - Integer.parseInt(args[2]) >= 0) {
                                FriendlyStorage.takeFriendliness(Integer.parseInt(args[2]), args[1]);
                                sender.sendMessage(FriendlyMessages.getTakeMessage(args[1], Integer.parseInt(args[2])));
                            } else if ((FriendlyStorage.getFriendliness(args[1])) - Integer.parseInt(args[2]) < 0) {
                                sender.sendMessage(FriendlyMessages.getMessage5());
                            }
                        } else {
                            sender.sendMessage(FriendlyMessages.getMessage2());
                        }
                    } else if ((!args[0].equalsIgnoreCase("set")) && (!args[0].equalsIgnoreCase("give")) && (!args[0].equalsIgnoreCase("take"))) {
                        sender.sendMessage(FriendlyMessages.getMessage2());
                    }
                } else if (args.length > 3) {
                    sender.sendMessage(FriendlyMessages.getMessage2());
                    //invalid args
                }
            }
        }

        return false;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
    //funny shit from https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
}
