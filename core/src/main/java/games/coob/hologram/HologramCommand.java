package games.coob.hologram;

import games.coob.hologram.model.NMSHologramProvider;
import games.coob.nmsinterface.NMSHologramI;
import org.mineacademy.fo.command.SimpleCommand;

public class HologramCommand extends SimpleCommand {
	protected HologramCommand() {
		super("holo");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final NMSHologramI hologramI = NMSHologramProvider.createHologram();

		hologramI.show(getPlayer().getLocation().add(0, 1.5, 0), getPlayer(), "Hello", "there");
	}
}
