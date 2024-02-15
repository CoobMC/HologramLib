package games.coob.nmsinterface;

import com.earth2me.essentials.libs.checkerframework.checker.nullness.qual.Nullable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface HologramAPI {

    HologramAPI createHologram(String id, Location location, boolean saveToFile, final List<String> linesOfText, @Nullable final String permission);

    void createFromRegistry(HologramRegistry registry);

    void show(final Player player);

    void hide(Player player);

    void remove();

    void updateVisibility(Player player);

    void updateLines(String... lines);

    void addLines(String... lines);

    void removeLines(Integer... indices);

    boolean canShow(Player player);

    boolean isViewer(Player player);

    boolean isInDisplayRange(Player player);

    Set<UUID> getViewers();

    Location getLocation();

    String getPermission();

    void setPermission(String permission);

    double getDisplayRange();

    void setDisplayRange(double range);
}
