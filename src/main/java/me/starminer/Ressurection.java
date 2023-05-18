package me.starminer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Ressurection implements Listener {
    int genocide = 0;

    public Ressurection(Respawn plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        player.setGameMode(GameMode.SPECTATOR);
        int survivalist = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.getGameMode() == GameMode.SURVIVAL) {
                survivalist++;
            }
        }
        if(survivalist >= 1) { //If genocide == 1, last player to die will respawn at spawn
            new Delayer(() -> {
                sendGameOverTitle(player, "YOU DIED!", ChatColor.RED);
                new Delayer(() -> {
                    PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 600, 255);
                    player.addPotionEffect(blind);

                    new Delayer(() -> {
                        sendTitle(player, "3", ChatColor.DARK_GREEN);
                        new Delayer(() -> {
                            sendTitle(player, "2", ChatColor.YELLOW);
                            new Delayer(() -> {
                                PotionEffect res = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 255);
                                player.addPotionEffect(res);
                                sendTitle(player, "1", ChatColor.DARK_RED);
                                new Delayer(() -> {

                                    player.sendMessage("That was 30 seconds?");
                                    if (player.isOnline()) {
                                        //Choose a random player that is alive
                                        Random random = new Random();

                                        List<Player> survivalPlayers = new ArrayList<>();

                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (p.getGameMode() == GameMode.SURVIVAL) {
                                                player.sendMessage("ALIVE: " + p.getName());
                                                survivalPlayers.add(p);
                                            }
                                        }
                                        System.out.println("alivePlayers: " + survivalPlayers.size());
                                        if (survivalPlayers.size() != 0) {
                                            int pInt = random.nextInt(survivalPlayers.size());
                                            System.out.println("pInt: " + pInt);
                                            player.setGameMode(GameMode.SURVIVAL);
                                            player.teleport(survivalPlayers.get(pInt));
                                        } else {
                                            System.out.println("!!!!!SIZE = 0!!!!!");
                                        }
                                    }
                                }, 20);
                            }, 20);
                        }, 20);
                    }, 530);
                }, 10);
            }, 0);
        }
        else{
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    private void sendTitle(Player player, String title, ChatColor color) {
        player.sendTitle(color + title, "", 5, 10, 5);
    }
    private void sendGameOverTitle(Player player, String title, ChatColor color) {
        player.sendTitle(color + title, "", 10, 50, 10);
    }
}
