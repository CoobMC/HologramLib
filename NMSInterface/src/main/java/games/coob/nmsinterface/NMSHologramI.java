/*package games.coob.nmsinterface;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NMSHologramI extends ConfigSerializable { // TODO add back

	void createHologram(String id, Location location, final Player player, final String... linesOfText); // TODO create with or withought registry

	void sendPackets(Object nmsArmorStand, Player... player);

	UUID getUniqueId();

	Location getLocation();

	//void setLines(boolean update, String... lines);

	void updateLines(String... lines);

	void addLines(String... lines);

	void removeLines(Integer... index);

	List<String> getLines();

	void remove();

	void hide(Player player);

	void show(final Player player);

	void updateVisibility(Player player);

	//boolean isShown(Player player);

	boolean isViewer(Player player);

	Set<UUID> getViewers();

	boolean canShow(Player player);

	void setPermission(String permission);

	String getPermission();

	double getDisplayRange();

	void setDisplayRange(double range);

	boolean isInDisplayRange(Player player);
}*/
