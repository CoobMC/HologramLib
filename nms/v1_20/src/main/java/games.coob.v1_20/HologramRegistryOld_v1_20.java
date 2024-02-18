/*package games.coob.v1_19;

import games.coob.nmsinterface.HologramRegistry;
import games.coob.nmsinterface.NMSHologramI;
import lombok.NonNull;
import org.mineacademy.fo.settings.ConfigItems;

import java.util.List;
import java.util.Set;

public class HologramRegistry_v1_19 extends HologramRegistry {

	private static final String FOLDER = "holograms";

	private static final ConfigItems<HologramRegistry_v1_19> loadedFiles = ConfigItems.fromFolder(FOLDER, HologramRegistry_v1_19.class); // TODO fix this

	protected HologramRegistry_v1_19(final String id) {
		super(id);
	}

	protected HologramRegistry_v1_19(final String id, final NMSHologramI hologram) {
		super(id, hologram);
	}

	@Override
	public NMSHologramI loadHologram() {
		return this.get("Hologram", NMSHologram_v1_19.class);
	}

	// -----------------------------------------------------------------
	// Static
	// -----------------------------------------------------------------

	public static void createHologram(@NonNull final String id, @NonNull final NMSHologram_v1_19 hologram) {
		loadedFiles.loadOrCreateItem(id, () -> new HologramRegistry_v1_19(id, hologram) {
		});
	}

	public static List<HologramRegistry_v1_19> getHolograms() {
		return loadedFiles.getItems();
	}

	public static void removeHologram(final NMSHologramI hologram) {
		for (final HologramRegistry_v1_19 registry : getHolograms())
			if (registry.getHologram().equals(hologram))
				loadedFiles.removeItem(registry);
	}

	public static void loadHolograms() {
		loadedFiles.loadItems();
	}

	public static Set<String> getHologramIDs() {
		return loadedFiles.getItemNames();
	}

	public static void removeHologram(final String id) {
		loadedFiles.removeItemByName(id);
	}

	public static boolean isHologramLoaded(final String id) {
		return loadedFiles.isItemLoaded(id);
	}

	public static HologramRegistry findById(@NonNull final String id) {
		return loadedFiles.findItem(id);
	}
}*/
