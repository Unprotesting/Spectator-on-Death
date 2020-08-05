package unprotesting.com.github;

import java.util.HashMap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class DeathEvent implements Listener {
  public HashMap<String, Long> cooldown = new HashMap<>();
  
  public Plugin instance;
  
  public void function() {
    Main plugin = (Main)Main.instance;
    plugin.getServer();
  }
  
  String deathmessage = Main.instance.getConfig().getString("death-message");
  
  String AutoRespawnMessage = Main.instance.getConfig().getString("auto-respawn.auto-respawn-message");
  
  String respawnMessage = Main.instance.getConfig().getString("respawn-message");
  
  int Respawncooldown = Main.instance.getConfig().getInt("auto-respawn.time");
  
  String respawnedtitle = Main.instance.getConfig().getString("respawn-title");
  
  String respawnedsubtitle = Main.instance.getConfig().getString("respawn-subtitle");
  
  String BroadcastMessage = Main.instance.getConfig().getString("broadcast-message");
  
  String effect = Main.instance.getConfig().getString("effect");

  String respawnLocation = Main.instance.getConfig().getString("respawn-location");
  
  @EventHandler
  public void damage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
      final Player player = (Player)event.getEntity();
      final Location Loc = event.getEntity().getLocation();
      this.BroadcastMessage.replace("%PLAYER%", player.getName());
      if (player.hasPermission("sod.gospectator") == true && !Main.instance.getConfig().getBoolean("autorespawn")) {
        if (player.getHealth() - event.getFinalDamage() <= 0.0D && event.getEntity() instanceof Player && player.hasPermission("sod.gospectator") == true) {
          event.setCancelled(true);
          player.setGameMode(GameMode.SPECTATOR);
          player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                this.deathmessage.replace("%PLAYER%", player.getName())));
          player.spawnParticle(Particle.valueOf(this.effect), player.getLocation(), 20);
        } 
        if (Main.instance.getConfig().getBoolean("broadcast"))
          Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', 
                Main.instance.getConfig().getString("broadcast-message").replace("%PLAYER%", player.getName()))); 
      } 
      if (Main.instance.getConfig().getBoolean("auto-respawn") && player.hasPermission("sod.gospectator") == true && player.hasPermission("sod.autorespawn") == true &&
        player.getHealth() - event.getFinalDamage() <= 0.0D && event.getEntity() instanceof Player) {
        if (Main.instance.getConfig().getBoolean("effect"))
          player.spawnParticle(Particle.valueOf(this.effect), player.getLocation(), 20); 
          this.cooldown.put(player.getName(), Long.valueOf(System.currentTimeMillis()));
          player.setGameMode(GameMode.SPECTATOR);
          player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
          this.AutoRespawnMessage.replace("%PLAYER%", player.getName())));
          event.setCancelled(true);
          if (Main.instance.getConfig().getBoolean("Broadcast"))
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.BroadcastMessage).replace("%PLAYER%", player.getName())); 
          Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                public void run() {
                  if (Main.instance.getConfig().getBoolean("death-command")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setHealth(20.0D);
                  } else {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setHealth(20.0D);
                    player.teleport(Loc);
                  } 
              }
            },this.Respawncooldown * 20);
      } 
      if (Main.instance.getConfig().getBoolean("playsound"))
        player.spawnParticle(Particle.valueOf(this.effect), player.getLocation(), 20); 
      if (Main.instance.getConfig().getBoolean("death-command") && 
        player.getHealth() - event.getFinalDamage() <= 0.0D && event.getEntity() instanceof Player)
        for (String cmd : Main.instance.getConfig().getStringList("death-commands"))
          Main.instance.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), 
              cmd.replace("%PLAYER%", player.getName()));  
    } 
  }

  public void respawnLocation(Player player){
    if (respawnLocation == "none"){
      return;
    }
    if (respawnLocation == "nearest-player"){
      Player nearestPlayer = getNearestPlayer(player);
      Boolean teleported = player.teleport(nearestPlayer);
      if (teleported){
        player.sendMessage("Teleported to nearest player: " + nearestPlayer.getCustomName());
      }
      else{
        player.sendMessage("Error teleporting to nearest player: " + nearestPlayer.getCustomName());
      }
    }
    if (respawnLocation == "coordinates"){
      player.teleport(new Location(player.getWorld(), Main.instance.getConfig().getDouble("locationx"), Main.instance.getConfig().getDouble("locationy"),
      Main.instance.getConfig().getDouble("locationy"), player.getLocation().getYaw(), player.getLocation().getPitch()));
    }
  }

  public Player getNearestPlayer(Player player) {
    double distNear = 0.0D;
    Player playerNear = null;
    for (Player player2 : Bukkit.getOnlinePlayers()) {
        if (player == player2) { continue; }
        if (player.getWorld() != player2.getWorld()) { continue; }
        Location location = player.getLocation();
        double dist = player.getLocation().distance(location);
        if (playerNear == null || dist < distNear) {
            playerNear = player2;
            distNear = dist;
        }
    }
    return playerNear;
}
}
