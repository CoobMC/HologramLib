//package games.coob.hologram.listener;
//
//import games.coob.hologram.model.HologramRegistryProvider;
//import org.bukkit.World;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.world.WorldLoadEvent;
//import org.bukkit.event.world.WorldUnloadEvent;
//import org.mineacademy.fo.remain.Remain;
//
//public class WorldListener implements Listener {
//
//	@EventHandler
//	public void onWorldUnload(final WorldUnloadEvent event) {
//		final World world = event.getWorld();
//
//		for (final Player player : Remain.getOnlinePlayers())
//			HologramRegistryProvider.getHolograms().stream()
//					.filter(registry -> registry.getHologram().getLocation().getWorld().equals(world))
//					.filter(registry -> registry.getHologram().isViewer(player))
//					.forEach(registry -> registry.setEnabled(false));
//	}
//
//	@EventHandler
//	public void onWorldLoad(final WorldLoadEvent event) {
//		for (final Player player : Remain.getOnlinePlayers())
//			HologramRegistryProvider.getHolograms().stream()
//					.filter(registry -> registry.getHologram().isViewer(player))
//					.forEach(registry -> {
//						registry.getHologram().show(player);
//						registry.setEnabled(true);
//					});
//	}
//}