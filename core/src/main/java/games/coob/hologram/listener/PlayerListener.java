package games.coob.hologram.listener;

import games.coob.nmsinterface.Hologram;
import games.coob.nmsinterface.HologramRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * Listens for player join events to manage hologram visibility for newly joined players.
 * When a player joins the server, this listener checks if there are any holograms that
 * should be visible to them based on predefined criteria, such as viewer lists and permissions,
 * and makes those holograms visible to the player.
 */
public class PlayerListener implements Listener {

    /**
     * Handles the {@link PlayerJoinEvent} to show applicable holograms to the joining player.
     * Iterates through all registered holograms and shows those that allowed to see.
     *
     * @param event The player join event, containing information about the joining player.
     */
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        for (final HologramRegistry registry : HologramRegistry.getHolograms()) {
            final Hologram hologram = registry.getHologram();

            if (hologram.getViewers().contains(playerUUID) && hologram.canShow(player)) {
                hologram.show(player);
            }
        }
    }
}