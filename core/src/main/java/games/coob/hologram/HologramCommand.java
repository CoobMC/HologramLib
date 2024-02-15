package games.coob.hologram;

import games.coob.hologram.model.HologramProvider;
import games.coob.nmsinterface.HologramAPI;
import games.coob.nmsinterface.HologramRegistry;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

/**
 * Used for testing purposes
 */
public class HologramCommand extends SimpleCommand {
    protected HologramCommand() {
        super("holo");
    }

    @Override
    protected void onCommand() {
        checkConsole();

        final String param = args[0].toLowerCase();
        final String id = args[1];

        final List<String> lines = List.of("Hello", "there");

        switch (param) {
            case "create" -> {
                final HologramAPI hologram = HologramProvider.getInstance().createHologram(id, getPlayer().getLocation(), true, lines, null);
                hologram.show(getPlayer());
            }
            case "remove" -> HologramRegistry.findById(id).getHologram().remove();
        }
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1)
            return this.completeLastWord("create", "remove");
        if (args.length == 2)
            return this.completeLastWord("<id/name>");

        return NO_COMPLETE;
    }
}
