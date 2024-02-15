/*package games.coob.nmsinterface;

import games.coob.nmsinterface.NMSHologramI;
import lombok.Getter;
import org.mineacademy.fo.settings.YamlConfig;

public abstract class HologramRegistry extends YamlConfig {

	private static final String FOLDER = "holograms";

	//private static final ConfigItems<HologramRegistry> loadedFiles = ConfigItems.fromFolder(FOLDER, HologramRegistry.class);

	@Getter
	private NMSHologramI hologram;

	protected HologramRegistry(final String id) {
		this(id, null);
	}

	protected HologramRegistry(final String id, final NMSHologramI hologram) {
		if (hologram != null)
			this.hologram = hologram;

		this.loadConfiguration(NO_DEFAULT, FOLDER + "/" + id + ".yml");
	}

	@Override
	protected void onLoad() {
		if (this.hologram == null)
			this.hologram = loadHologram();

		this.save();
	}

	@Override
	protected void onSave() {
		this.set("Hologram", this.hologram);
	}

	public abstract NMSHologramI loadHologram();
}*/
