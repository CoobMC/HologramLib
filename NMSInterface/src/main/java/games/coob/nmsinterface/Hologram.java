package games.coob.nmsinterface;

import com.earth2me.essentials.libs.checkerframework.checker.nullness.qual.Nullable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class Hologram {
    public abstract Hologram createHologram(String id, Location location, boolean saveToFile, final List<String> linesOfText, @Nullable final String permission);

    public abstract void createFromRegistry(HologramRegistry registry);

    public abstract void show(final Player player);

    public abstract void hide(Player player);

    public abstract void remove();

    public void updateVisibility(final Player player) {
        if (!isInDisplayRange(player) && isViewer(player))
            hide(player);
        else if (isInDisplayRange(player) && !isViewer(player)) {
            show(player);
        }
    }

    public abstract void updateLines(String... lines);

    public abstract void addLines(String... lines);

    public abstract void addLines(int index, final String... lines);

    public abstract void removeLines(Integer... indices);

    public abstract boolean canShow(Player player);

    public abstract boolean isViewer(Player player);

    public abstract boolean isInDisplayRange(Player player);

    public abstract Set<UUID> getViewers();

    public abstract Location getLocation();

    public abstract String getPermission();

    public abstract void setPermission(String permission);

    public abstract double getDisplayRange();

    public abstract void setDisplayRange(double range);
}
