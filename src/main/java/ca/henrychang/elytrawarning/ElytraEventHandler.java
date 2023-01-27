package ca.henrychang.elytrawarning;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Period;


public class ElytraEventHandler implements Listener {

    ElytraWarning plugin;


    public ElytraEventHandler(ElytraWarning plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerFlying(EntityToggleGlideEvent event){
        Entity entity = event.getEntity();

        if(entity instanceof Player){
            Player player = ((Player) entity).getPlayer();

            if(player.getInventory().getChestplate() == null) return;

            if(player.getInventory().getChestplate().getType() == Material.ELYTRA){

                BukkitRunnable task = new CheckInFlight(player);
                task.runTaskTimer(plugin, 0L, 200L);

            }else
                return;

        }else
            return;
    }

    private void SendWarning(Player player){
        player.sendMessage(ChatColor.RED + "Elytra durability critically low");
        BukkitRunnable sound_task = new ElytraWarningSound(player);
        sound_task.runTaskTimer(plugin, 0L, 5L);
    }

    private class CheckInFlight extends BukkitRunnable {
        Player player;
        int elytra_percent;

        public CheckInFlight(Player player){
            this.player = player;
        }
        @Override
        public void run() {
            if(player.isGliding()){
                int current_max_durability = player.getInventory().getChestplate().getType().getMaxDurability();
                Damageable elytra_meta = (Damageable) player.getInventory().getChestplate().getItemMeta();
                elytra_percent = (current_max_durability-elytra_meta.getDamage())*100/current_max_durability;

                if(elytra_percent < plugin.warning)
                    player.sendMessage(ChatColor.YELLOW + "Elytra durability below " + elytra_percent + "%");
                if (elytra_percent <= plugin.critical)
                {
                    SendWarning(player);
                }
                else
                    return;
            }
            else
                this.cancel();
        }
    }

    private class ElytraWarningSound extends BukkitRunnable {
        int repeat = 0;
        Player player;

        public ElytraWarningSound(Player player){
            this.player = player;
        }

        @Override
        public void run() {
            if(repeat<3){
                repeat++;
                player.playNote(player.getLocation(), Instrument.CHIME, Note.flat(1, Note.Tone.B));
            }
            else
                this.cancel();
        }
    }
}


