package games.coob.hologram.model;

import games.coob.nmsinterface.NMSHologramI;
import games.coob.v1_17.NMSHologram_v1_17;
import games.coob.v1_18.NMSHologram_v1_18;
import games.coob.v1_19.NMSHologram_v1_19;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.exception.FoException;

public final class NMSHologramProvider {


	public static NMSHologramI deserialize(final SerializedMap map) {
		if (MinecraftVersion.equals(MinecraftVersion.V.v1_17))
			return NMSHologram_v1_17.deserialize(map);
		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_18))
			return NMSHologram_v1_18.deserialize(map);
		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_19))
			return NMSHologram_v1_19.deserialize(map);
		else
			throw new FoException("Unsupported Minecraft version " + MinecraftVersion.getServerVersion());
	}

	/*public static void sendTo(final Location location, final Player player, final String... linesOfText) {
		final NMSHologramI hologram;

		if (MinecraftVersion.equals(MinecraftVersion.V.v1_17))
			hologram = new games.coob.v1_17.NMSHologram_v1_17();
		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_18))
			hologram = new games.coob.v1_18.NMSHologram_v1_18();
		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_19))
			hologram = new games.coob.v1_19.NMSHologram_v1_19();
		else
			throw new FoException("Unsupported Minecraft version " + MinecraftVersion.getServerVersion());

		hologram.show(location, player, linesOfText);
	}*/

	public static NMSHologramI getInstance() {
		final NMSHologramI hologram;

		if (MinecraftVersion.equals(MinecraftVersion.V.v1_17))
			hologram = new NMSHologram_v1_17();
		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_18))
			hologram = new NMSHologram_v1_18();
		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_19))
			hologram = new NMSHologram_v1_19();
		else
			throw new FoException("Unsupported Minecraft version " + MinecraftVersion.getServerVersion());

		return hologram;
	}
}
