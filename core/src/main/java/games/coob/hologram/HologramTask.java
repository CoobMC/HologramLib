package games.coob.hologram;

import games.coob.hologram.model.HologramRegistryProvider;
import games.coob.nmsinterface.HologramRegistryI;
import games.coob.nmsinterface.NMSHologramI;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.Remain;

/**
 * Represents a self-repeating task managing hologram.
 */
@RequiredArgsConstructor
public final class HologramTask extends BukkitRunnable {

	@Override
	public void run() {
		final HologramRegistryI registry = HologramRegistryProvider.getHologramRegistryInstance();
		final double range = 20;

		for (final Player player : Remain.getOnlinePlayers()) {
			for (final NMSHologramI hologram : registry.getLoadedHolograms()) {
				if (!player.hasMetadata(hologram.getUniqueId().toString()) && registry.isRegistered(hologram))
					showPlayersInRange(hologram, player, range);

				if (player.getLocation().distance(hologram.getLocation()) > range && !hologram.isHidden())
					hologram.hide(player);
			}
		}
	}

	/*
	 * Shows the hologram to players within the set range
	 */
	private void showPlayersInRange(final NMSHologramI hologram, final Player player, final double range) {
		final Location hologramLocation = hologram.getLocation();
		final Location playerLocation = player.getLocation();
		final String[] array = new String[hologram.getLines().size()];

		if (player.getWorld().equals(hologramLocation.getWorld()) && playerLocation.distance(hologramLocation) <= range)
			hologram.show(hologramLocation, player, hologram.getLines().toArray(array));
	}
}