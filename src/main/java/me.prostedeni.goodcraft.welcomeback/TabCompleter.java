package me.prostedeni.goodcraft.welcomeback;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String commandLabel, String[] args){
        if (command.getName().equalsIgnoreCase("welcomeback")){
            if (args.length == 0){
                final ArrayList<String> l = new ArrayList<>();

                if (sender.hasPermission("welcomeback.checkown") || sender.hasPermission("welcomeback.admin")) {
                    l.add("check");
                }
                if (sender.hasPermission("welcomeback.admin")) {
                    l.add("set");
                    l.add("give");
                    l.add("take");
                    l.add("help");
                    l.add("reload");
                }

                return l;

            } else if (args.length == 1){
                final ArrayList<String> l = new ArrayList<>();
                final ArrayList<String> commands = new ArrayList<>();

                if (sender.hasPermission("welcomeback.checkown") || sender.hasPermission("welcomeback.admin")) {
                    commands.add("check");
                }
                if (sender.hasPermission("welcomeback.admin")) {
                    commands.add("set");
                    commands.add("give");
                    commands.add("take");
                    commands.add("help");
                    commands.add("reload");
                }

                StringUtil.copyPartialMatches(args[0], commands, l);

                return l;
            } else if (args.length == 2){

                final ArrayList<String> l = new ArrayList<>();
                final ArrayList<String> commands = new ArrayList<>();

                if (args[0].equalsIgnoreCase("check")){

                    if (sender.hasPermission("welcomeback.admin")) {
                        for (Player s : Bukkit.getOnlinePlayers()) {
                            commands.add(s.getName());
                        }
                    }
                    StringUtil.copyPartialMatches(args[1], commands, l);

                    if (sender.hasPermission("welcomeback.checkown") || sender.hasPermission("welcomeback.admin")) {
                        l.add("<leave blank for self>");
                    }
                    return l;
                } else if (!(args[0].equalsIgnoreCase("check"))){
                    if (sender.hasPermission("welcomeback.admin")){
                        if (sender.hasPermission("welcomeback.admin")) {
                            for (Player s : Bukkit.getOnlinePlayers()) {
                                commands.add(s.getName());
                            }
                        }
                        StringUtil.copyPartialMatches(args[1], commands, l);
                        if (l.isEmpty()){
                            l.add("<player>");
                        }
                        return l;
                    }
                }
            } else if (args.length == 3){
                final ArrayList<String> l = new ArrayList<>();
                final ArrayList<String> commands = new ArrayList<>();

                if (sender.hasPermission("welcomeback.admin")) {
                    if ((args[0].equalsIgnoreCase("set")) || (args[0].equalsIgnoreCase("take")) || (args[0].equalsIgnoreCase("give"))) {
                        if (isInteger(args[2])) {
                            l.add("<number>");
                        }
                    }
                }
                if (l.isEmpty()){
                    l.add("<number>");
                }

                return l;
            } else {
                if (args.length > 3){
                    final ArrayList<String> l = new ArrayList<>();
                    return l;
                }
            }
        }

        return null;
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
