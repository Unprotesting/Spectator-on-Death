package unprotesting.com.github;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
  FileConfiguration config = getConfig();
  
  Server getserver = getServer();
  
  public static Plugin instance;
  
  public Plugin getInstance() {
    return instance;
  }
  
  public void onEnable() {
    instance = (Plugin)this;
    saveDefaultConfig();
    this.config.options().copyDefaults(true);
    Bukkit.getPluginManager().registerEvents(this, (Plugin)this);
    Bukkit.getServer().getPluginManager().registerEvents(new DeathEvent(), (Plugin)this);
  }
}