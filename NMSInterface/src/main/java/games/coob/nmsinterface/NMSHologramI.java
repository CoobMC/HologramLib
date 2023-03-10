package games.coob.nmsinterface;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.model.ConfigSerializable;

import java.util.List;
import java.util.UUID;

public interface NMSHologramI extends ConfigSerializable {

	void createHologram(Location location, final Player player, final String... linesOfText);

	void sendPackets(Player player, Object nmsArmorStand);

	UUID getUniqueId();

	Location getLocation();

	void setLines(List<String> lines);

	List<String> getLines();

	void remove(Player player);

	void hide(Player player);

	void show(final Location location, final Player player, final String... linesOfText);

	boolean isHidden();
}
