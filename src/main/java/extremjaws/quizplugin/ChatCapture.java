package extremjaws.quizplugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatCapture implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!QuizPlugin.pollRunning) {return;}

        String message = event.getMessage();
        player.sendMessage(ChatColor.YELLOW + "Answer accepted.");
        QuizPlugin.answers.put(player, message.replace("_", " "));
        event.setCancelled(true);
    }
}
