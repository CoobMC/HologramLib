package games.coob.hologram;

import games.coob.nmsinterface.HologramAPI;
import games.coob.nmsinterface.HologramRegistry;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.Remain;

/**
 * Represents a self-repeating task managing hologram.
 */
@RequiredArgsConstructor
public final class HologramTask extends BukkitRunnable { // TODO show holograms when rejoining

    @Override
    public void run() {
        for (final HologramRegistry registry : HologramRegistry.getHolograms()) {
            final HologramAPI hologramAPI = registry.getHologram();

            for (final Player player : Remain.getOnlinePlayers())
                hologramAPI.updateVisibility(player);
        }

        //for (final Player player : Remain.getOnlinePlayers()) {
        //  for (final HologramRegistry hologram : HologramRegistryProvider.getHolograms()) {
        //    hologram.getHologram().updateVisibility(player);
				/*if (!player.hasMetadata(hologram.getUniqueId().toString()))
					hologram.show(player);

				if (player.getLocation().distance(hologram.getLocation()) > hologram.getDisplayRange() && hologram.isViewer(player)) // TODO isShown for a specific player
					hologram.hide(player);*/
    }
}
