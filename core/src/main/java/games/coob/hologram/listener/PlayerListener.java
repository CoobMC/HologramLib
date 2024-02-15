package games.coob.hologram.listener;

import games.coob.nmsinterface.HologramAPI;
import games.coob.nmsinterface.HologramRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        for (final HologramRegistry registry : HologramRegistry.getHolograms()) {
            final HologramAPI hologramAPI = registry.getHologram();

            if (hologramAPI.getViewers().contains(playerUUID) && hologramAPI.canShow(player)) {
                hologramAPI.show(player);
            }
        }
    }

   /* @EventHandler
    public void onRespawn(final PlayerRespawnEvent event) {
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
    }*/
}