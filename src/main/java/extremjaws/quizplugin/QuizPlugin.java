package extremjaws.quizplugin;

import extremjaws.quizplugin.Commands.QuizCommand;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class QuizPlugin extends JavaPlugin {
    public static QuizPlugin plugin;
    public static Player asker;
    public static String answer;
    public static Boolean pollRunning = false;
    public static int timertaskID;
    public static int time = 0;
    public static List<String> correctNames;
    static {
        correctNames = new ArrayList<String>();
    }
    public static HashMap<Player, String> answers;
    static {
        answers = new HashMap<Player, String>();
    }
    public static List<Player> incorrect;
    static {
        incorrect = new ArrayList<Player>();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(new ChatCapture(), this);
        getCommand("quiz").setExecutor(new QuizCommand());
    }
    public void startTimer() {
        time = 15;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        timertaskID = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                time--;
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + Integer.toString(time) + " seconds left to answer!"));
                    if (time <= 3) {
                        all.getWorld().playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0.5f);
                    }
                }
                if (time <= 0) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.getWorld().playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                    }
                    stopTimer();
                    check();
                    Bukkit.broadcastMessage(ChatColor.RED + "Time!");
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The answer was: " + answer);
                    Bukkit.broadcastMessage(ChatColor.GREEN + "People who got it correct: " + correctNames);
                    Bukkit.broadcastMessage(ChatColor.RED + "Incorrect answers: ");
                    for (Player all : incorrect) {
                        if (answers.get(all) != null) {
                            Bukkit.broadcastMessage(ChatColor.YELLOW + all.getName() + " said \"" + answers.get(all) + "\"");
                        } else {
                            Bukkit.broadcastMessage(ChatColor.RED + all.getName() + " did not answer.");
                        }
                    }
                    pollRunning = false;
                    answers.clear();
                    correctNames.clear();
                    incorrect.clear();
                }
            }
        }, 0L, 20L);

    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(timertaskID);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static QuizPlugin getInstance() {
        return plugin;
    }
    public void check() {
        for (Map.Entry<Player, String> entry : answers.entrySet()) {
            Player player = entry.getKey();
            String panswer = entry.getValue();
            if (panswer.equals(answer)) {
                correctNames.add(player.getName());
            } else {
                incorrect.add(player);
            }
        }
    }
}
