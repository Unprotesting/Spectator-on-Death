package unprotesting.com.github;

import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

public class util {

    public Plugin instance;

    public static void debugLog(String input) {
        if (Main.instance.getConfig().getBoolean("debug-mode")) {
            Main.instance.getLogger().log(Level.WARNING, "[SOD][DEBUG]: " + input);
        }
    }

    public static void Log(String input) {
        Main.instance.getLogger().log(Level.WARNING, "[SOD]: " + input);
    }
}