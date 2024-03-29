/*package games.coob.v1_18;

import games.coob.nmsinterface.HologramRegistryI;
import games.coob.nmsinterface.NMSHologramI;
import lombok.Getter;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.constants.FoConstants;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public final class HologramRegistry_v1_18 extends YamlConfig implements HologramRegistryI {

	@Getter
	private static final HologramRegistry_v1_18 instance = new HologramRegistry_v1_18();


	private List<NMSHologramI> loadedHolograms = new ArrayList<>();


	private HologramRegistry_v1_18() {
		this.loadConfiguration(NO_DEFAULT, FoConstants.File.DATA);
	}


	@Override
	public List<NMSHologramI> loadHolograms() {
		final List<NMSHologramI> loadedHologram = new ArrayList<>();

		for (final SerializedMap map : getMapList("Saved_Holograms")) {
			final NMSHologramI hologram = NMSHologram_v1_18.deserialize(map);

			loadedHologram.add(hologram);
		}

		return loadedHologram;
	}


	@Override
	public void register(final NMSHologramI hologram) {
		Valid.checkBoolean(!this.isRegistered(hologram), hologram + " is already registered!");

		this.loadedHolograms.add(hologram);
		this.save();
	}

	@Override
	public void unregister(final NMSHologramI hologram) {
		this.loadedHolograms.remove(hologram);
		this.save();
	}

	@Override
	public boolean isRegistered(final NMSHologramI hologram) {
		return this.isRegistered(hologram.getUniqueId());
	}

	@Override
	public boolean isRegistered(final UUID entityUniqueId) {
		for (final NMSHologramI hologram : this.loadedHolograms)
			if (hologram != null && hologram.getUniqueId().equals(entityUniqueId))
				return true;

		return false;
	}

	@Override
	public List<NMSHologramI> getLoadedHolograms() {
		return Collections.unmodifiableList(loadedHolograms);
	}

	@Override
	public void spawnFromDisk() {
		if (!MinecraftVersion.atLeast(MinecraftVersion.V.v1_16))
			return;

		// Tricky: This automatically calls the spawn method which puts the hologram to our loadedHolograms list

		this.loadedHolograms = loadHolograms();

		Common.log("@Found " + this.loadedHolograms.size() + " Holograms on the disk");

		for (final NMSHologramI hologram : this.loadedHolograms)
			Common.log("\tspawned " + hologram + " at " + hologram.getLocation());
	}

	@Override
	protected void onSave() {
		this.set("Saved_Holograms", this.getLoadedHolograms());
	}
}*/
