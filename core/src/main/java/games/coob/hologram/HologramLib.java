package games.coob.hologram;

import games.coob.hologram.model.HologramRegistryProvider;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

/**
 * PluginTemplate is a simple template you can use every time you make
 * a new plugin. This will save you time because you no longer have to
 * recreate the same skeleton and features each time.
 * <p>
 * It uses Foundation for fast and efficient development process.
 */
public final class HologramLib extends SimplePlugin { // TODO make people load the API

	/**
	 * Automatically perform login ONCE when the plugin starts.
	 */
	@Override
	protected void onPluginStart() {
		HologramRegistryProvider.getHologramRegistryInstance().spawnFromDisk();
	}

	/**
	 * Automatically perform login when the plugin starts and each time it is reloaded.
	 */
	@Override
	protected void onReloadablesStart() {
		registerCommand(new HologramCommand());

		Common.runTimer(20, new HologramTask());
	}

	@Override
	protected void onPluginPreReload() {
	}

	/* ------------------------------------------------------------------------------- */
	/* Static */
	/* ------------------------------------------------------------------------------- */

	/**
	 * Return the instance of this plugin, which simply refers to a static
	 * field already created for you in SimplePlugin but casts it to your
	 * specific plugin instance for your convenience.
	 *
	 * @return
	 */
	public static HologramLib getInstance() {
		return (HologramLib) SimplePlugin.getInstance();
	}
}
