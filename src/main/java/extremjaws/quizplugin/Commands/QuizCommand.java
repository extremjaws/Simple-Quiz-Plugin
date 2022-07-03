package extremjaws.quizplugin.Commands;

import extremjaws.quizplugin.QuizPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class QuizCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (QuizPlugin.pollRunning) {
            sender.sendMessage(ChatColor.RED + "Cannot start a quiz while one is already running.");
        } else {
            Bukkit.broadcastMessage(ChatColor.YELLOW + sender.getName() + ChatColor.GREEN + " has started a quiz: ");
            StringBuilder message = new StringBuilder(args[0]);
            for (int arg = 1; arg < args.length-1; arg++) {
                message.append(" ").append(args[arg]);
            }
            Bukkit.broadcastMessage(ChatColor.YELLOW + message.toString());
            QuizPlugin.getInstance().startTimer();
            QuizPlugin.asker = sender.getServer().getPlayer(sender.getName());
            QuizPlugin.answer = args[args.length-1].replace("_", " ");
            QuizPlugin.pollRunning = true;
        }
        return true;
    }
}
