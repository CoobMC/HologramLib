package games.coob.hologram.model;

import games.coob.nmsinterface.HologramAPI;
import games.coob.v1_19.Hologram_v1_19;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.exception.FoException;

public final class HologramProvider {

	public static HologramAPI getInstance() {
		final HologramAPI hologram;

		if (MinecraftVersion.equals(MinecraftVersion.V.v1_19))
			hologram = new Hologram_v1_19();
		/*else if (MinecraftVersion.equals(MinecraftVersion.V.v1_18))
			hologram = new NMSHologram_v1_18();
		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_17))
			hologram = new NMSHologram_v1_17();*/
		else
			throw new FoException("Unsupported Minecraft version " + MinecraftVersion.getServerVersion());

		return hologram;
	}
}
