package games.coob.hologram.model;

import games.coob.commons.Hologram;
import games.coob.v1_17.Hologram_v1_17;
import games.coob.v1_18.Hologram_v1_18;
import games.coob.v1_19.Hologram_v1_19;
import games.coob.v1_20.Hologram_v1_20;
import games.coob.v1_21.Hologram_v1_21;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.exception.FoException;

/**
 * Provides a mechanism for obtaining an instance of {@link Hologram} that is compatible
 * with the server's current Minecraft version. This factory class abstracts away the
 * version-specific instantiation logic, enabling the creation of holograms without
 * directly coupling to specific version implementations.
 */
public final class HologramProvider {

    /**
     * Retrieves an instance of {@link Hologram} suitable for the server's Minecraft version.
     * This method checks the server's version and returns an implementation of Hologram
     * designed for that version. If no suitable implementation is found, it throws an exception.
     *
     * @return An instance of {@link Hologram} tailored to the server's version.
     * @throws FoException if the server's Minecraft version is not supported by any available {@link Hologram} implementation.
     */
    public static Hologram getInstance() {
        final Hologram hologram;

        if (MinecraftVersion.equals(MinecraftVersion.V.v1_17))
            hologram = new Hologram_v1_17();
        else if (MinecraftVersion.equals(MinecraftVersion.V.v1_18))
            hologram = new Hologram_v1_18();
        else if (MinecraftVersion.equals(MinecraftVersion.V.v1_19))
            hologram = new Hologram_v1_19();
        else if (MinecraftVersion.equals(MinecraftVersion.V.v1_20))
            hologram = new Hologram_v1_20();
        else if (MinecraftVersion.equals(MinecraftVersion.V.v1_21))
            hologram = new Hologram_v1_21();
        else
            throw new FoException("Unsupported Minecraft version " + MinecraftVersion.getFullVersion());

        return hologram;
    }
}
