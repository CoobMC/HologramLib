package games.coob.hologram;

import games.coob.commons.Hologram;
import games.coob.commons.HologramRegistry;
import games.coob.hologram.model.HologramProvider;
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
                final Hologram hologram = HologramProvider.getInstance().createHologram(id, getPlayer().getLocation(), true, lines, null);
                hologram.show(getPlayer());
            }
            case "remove" -> HologramRegistry.findById(id).getHologram().remove();
            case "addline" -> {
                final Hologram hologramAPI = HologramRegistry.findById(id).getHologram();
                hologramAPI.addLines("Minecraft", "game");
            }
            case "addatindex" -> {
                final Hologram hologramAPI = HologramRegistry.findById(id).getHologram();
                hologramAPI.addLines(1, "hi");
            }
            case "removeline" -> {
                final Hologram hologramAPI = HologramRegistry.findById(id).getHologram();
                hologramAPI.removeLines(1);
            }
            case "update" -> {
                final Hologram hologramAPI = HologramRegistry.findById(id).getHologram();
                hologramAPI.updateLines("what's", "up");
            }
        }
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1)
            return this.completeLastWord("create", "remove", "addLine", "addAtIndex", "removeLine", "update");
        if (args.length == 2)
            return this.completeLastWord("<id/name>");

        return NO_COMPLETE;
    }
}
