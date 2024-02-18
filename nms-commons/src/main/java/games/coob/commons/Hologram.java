package games.coob.commons;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Represents an abstract base class for creating and managing holograms in Minecraft.
 * This class provides the foundational methods required to create, display, and manipulate holograms,
 * with specifics to be implemented by version-specific subclasses.
 */
@Getter
@Setter
public abstract class Hologram {

    /**
     * The lines of text displayed by the hologram.
     */
    private List<String> lines = new LinkedList<>();

    /**
     * A set of UUIDs representing players currently viewing the hologram.
     */
    private Set<UUID> viewers = new HashSet<>();

    /**
     * Optional permission required for a player to view the hologram.
     */
    private String permission;

    /**
     * The display range within which the hologram is visible to players.
     */
    private double displayRange = 48.0;

    /**
     * The hologram's location in the world.
     */
    private Location location;

    /**
     * Indicates whether the hologram is saved to file for persistence.
     */
    private boolean savedToFile;

    /**
     * A unique identifier for the hologram.
     */
    private String id;

    /**
     * Creates an entity for a given line of text at a specified location.
     * The actual entity creation logic is version-specific and must be implemented by subclasses.
     *
     * @param location The location at which to create the entity.
     * @param line     The line of text the entity will display.
     * @return The created entity object.
     */
    protected abstract Object createEntity(Location location, String line);

    /**
     * Creates the lines of text for the hologram at a specified location.
     * The actual entity creation logic is version-specific and must be implemented by subclasses.
     *
     * @param location   The location at which to create the hologram.
     * @param lineOfText The lines of text to display.
     */
    protected abstract void createEntityLines(Location location, List<String> lineOfText);

    /**
     * Displays the hologram to a specified player.
     * Subclasses must implement the logic to make the hologram visible to the player.
     *
     * @param player The player to whom the hologram should be shown.
     */
    public abstract void show(final Player player);

    /**
     * Hides the hologram from a specified player.
     * Subclasses must implement the logic to make the hologram invisible to the player.
     *
     * @param player The player from whom the hologram should be hidden.
     */
    public abstract void hide(final Player player);

    /**
     * Removes the hologram.
     * Subclasses must implement the logic for removing the hologram and cleaning up any resources.
     */
    public abstract void remove();

    /**
     * Updates the lines of text displayed by the hologram.
     * Subclasses must implement the logic to update the hologram's text.
     *
     * @param lines The new lines of text to display.
     */
    public abstract void updateLines(String... lines);

    /**
     * Factory method to create and initialize a hologram with the specified properties.
     * This method sets up a new hologram at the given location, with the provided text lines
     * and optional permission. If saveToFile is true, the hologram's data is persisted.
     *
     * @param id          The unique identifier for the hologram. Must be unique across all holograms.
     * @param location    The initial location where the hologram will be displayed.
     * @param saveToFile  Whether to save the hologram's data to file for persistence.
     * @param linesOfText The text lines that the hologram will display.
     * @param permission  An optional permission string. Players must have this permission to see the hologram.
     * @return The created Hologram object, or null if a hologram with the same ID already exists.
     */
    public Hologram createHologram(final String id, final Location location, final boolean saveToFile, final List<String> linesOfText, @Nullable final String permission) {
        if (HologramRegistry.getHologramIDs().contains(id)) {
            Common.log("Unable to create hologram because it already exists!");
            return null;
        }

        this.id = id;
        this.permission = permission;
        this.location = location;
        this.lines = linesOfText;

        createEntityLines(location, linesOfText);

        if (saveToFile) {
            final HologramRegistry registry = HologramRegistry.createHologram(id, location);
            registry.setLines(linesOfText);
            registry.setHologram(this);
            registry.setPermission(permission);
            this.displayRange = registry.getDisplayRange();
            this.savedToFile = true;
        }

        return this;
    }

    /**
     * Initializes a hologram from a saved state in the HologramRegistry.
     * This method is typically used to restore holograms from persistent storage, applying
     * the properties stored in the registry to recreate the hologram in the game world.
     *
     * @param registry The registry entry containing the hologram's saved properties.
     */
    public void createFromRegistry(final HologramRegistry registry) {
        this.location = registry.getLocation();
        this.id = registry.getName();
        this.permission = registry.getPermission();
        this.lines = registry.getLines();
        this.displayRange = registry.getDisplayRange();

        createEntityLines(registry.getLocation(), registry.getLines());
        registry.setHologram(this);
    }

    /**
     * Determines whether the hologram can be shown to a specific player.
     * This decision is based on whether the player is within the display range and,
     * if a permission is set, whether the player has the required permission.
     *
     * @param player The player to check visibility for.
     * @return true if the hologram can be shown to the player, false otherwise.
     */
    public boolean canShow(final Player player) {
        return (this.permission == null || player.hasPermission(this.permission)) && isInDisplayRange(player);
    }

    /**
     * Checks if a player is currently marked as a viewer of the hologram.
     *
     * @param player The player to check.
     * @return true if the player is in the set of viewers, false otherwise.
     */
    public boolean isViewer(final Player player) {
        return this.viewers.contains(player.getUniqueId());
    }

    /**
     * Determines if a player is within the display range of the hologram.
     *
     * @param player The player to check.
     * @return true if the player is within the range specified by displayRange, relative to the hologram's location.
     */
    public boolean isInDisplayRange(final Player player) {
        final Location location = this.location;
        final double range = this.displayRange;

        if (player.getWorld().equals(location.getWorld())) {
            return player.getLocation().distanceSquared(location) <= range * range;
        }

        return false;
    }

    /**
     * Updates the visibility of the hologram for a specific player based on their proximity
     * and viewing permissions. This method decides whether to show or hide the hologram to the player.
     *
     * @param player The player for whom to update the hologram's visibility.
     */

    public void updateVisibility(final Player player) {
        if (!isInDisplayRange(player) && isViewer(player))
            hide(player);
        else if (isInDisplayRange(player) && !isViewer(player)) {
            show(player);
        }
    }

    /**
     * Refreshes the hologram for all current viewers. This method may be used to re-send
     * the hologram entities to all viewers, typically after an update or when visibility needs
     * to be refreshed due to environmental changes.
     */

    protected void refreshHologramForAllViewers() {
        this.viewers.forEach(uuid -> {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                hide(player);
                show(player);
            }
        });
    }

    /**
     * Adds new text lines to the hologram at a specified index.
     * This method allows for dynamic addition of text lines, inserting them into the hologram
     * at the position specified by the index.
     *
     * @param index    The position at which to insert the new lines. Must be within the current size bounds.
     * @param newLines The new lines of text to be added.
     */

    public void addLines(int index, final String... newLines) {
        this.lines = new ArrayList<>(this.lines);

        if (index < 0) {
            index = 0;
        } else if (index > this.lines.size()) {
            index = this.lines.size();
        }

        final List<String> newLinesList = Arrays.asList(newLines);
        this.lines.addAll(index, newLinesList);

        updateLines(this.lines.toArray(new String[0]));
    }

    /**
     * Removes specified lines from the hologram based on their indices.
     *
     * @param indices The indices of the lines to remove. Each must be a valid index within the current lines list.
     */

    public void removeLines(final Integer... indices) {
        this.lines = new ArrayList<>(this.lines);

        for (final int index : indices)
            this.lines.remove(index);

        updateLines(this.lines.toArray(new String[0]));
    }

    /**
     * Sets the text lines of the hologram to the specified list of strings.
     * This method updates the hologram's current text to the new set of lines provided,
     * and if the hologram is marked as saved to file, updates the persistent storage accordingly.
     *
     * @param lines The new list of text lines for the hologram.
     */

    public void setLines(final List<String> lines) {
        if (savedToFile)
            HologramRegistry.findById(id).setLines(lines);

        this.lines = lines;
    }

    /**
     * Sets the permission required for players to view the hologram.
     * If a permission is specified, only players with this permission will be able to see the hologram.
     * Setting this to null will make the hologram visible to all players within the display range.
     * If the hologram is saved to file, this change will be reflected in the persistent storage.
     *
     * @param permission The permission string required to view the hologram, or null to make it visible to all.
     */

    public void setPermission(final String permission) {
        if (savedToFile)
            HologramRegistry.findById(id).setPermission(permission);

        this.permission = permission;
    }

    /**
     * Sets the display range of the hologram.
     * The display range determines how close a player needs to be (in blocks) to the hologram's location
     * to be able to see it. Changes to the display range will affect visibility checks but will not
     * automatically update visibility for current viewers. If the hologram is saved to file,
     * this change will be updated in the persistent storage.
     *
     * @param displayRange The new display range in blocks.
     */

    public void setDisplayRange(final double displayRange) {
        if (savedToFile)
            HologramRegistry.findById(id).setDisplayRange(displayRange);

        this.displayRange = displayRange;
    }

    /**
     * Adds one or more new lines of text to the end of the hologram's current text.
     * This is a convenience method that appends the given lines to the hologram, effectively
     * increasing its size by the number of lines added. The hologram will be updated to
     * display these new lines accordingly.
     *
     * @param lines The lines of text to add to the hologram.
     */

    public void addLines(final String... lines) {
        this.addLines(this.lines.size(), lines);
    }

    /**
     * Retrieves the world in which the hologram is located.
     * This utility method provides access to the Bukkit World object associated with the hologram's
     * current location, facilitating interactions that require knowledge of the hologram's environment.
     *
     * @return The World object representing the world where the hologram exists.
     */

    public World getWorld() {
        return this.location.getWorld();
    }
}
