package games.coob.nmsinterface;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.mineacademy.fo.SerializeUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class HologramRegistry extends YamlConfig {

    private static final String FOLDER = "holograms";

    private static final ConfigItems<HologramRegistry> loadedFiles = ConfigItems.fromFolder(FOLDER, HologramRegistry.class);

    private List<String> lines = new ArrayList<>();

    private Hologram hologram;

    private org.bukkit.Location location;

    private String permission;

    private double displayRange;

    private HologramRegistry(final String id) {
        this(id, null);
    }

    private HologramRegistry(final String id, final Location location) {
        if (location != null)
            this.location = location;

        this.loadConfiguration(NO_DEFAULT, FOLDER + "/" + id + ".yml");
    }

    @Override
    protected void onLoad() {
        if (this.location == null) {
            Valid.checkBoolean(isSet("Location"), "Corrupted hologram file: " + this.getFileName() + ", lacks the 'Location' key to determine the location of the hologram.");

            this.location = SerializeUtil.deserializeLocation(this.getString("Location"));
        }

        this.lines = this.getStringList("Lines");
        this.displayRange = this.getDouble("Display_Range", 48.0);
        this.permission = this.getString("Permission", null);

        this.save();
    }

    @Override
    protected void onSave() {
        this.set("Lines", this.lines);
        this.set("Location", this.location);
        this.set("Location", this.location);
        this.set("Display_Range", this.displayRange);
        this.set("Permission", this.permission);
    }

    public void setLines(final List<String> lines) {
        this.lines = lines;

        this.save();
    }

    public void setHologram(final Hologram hologram) {
        this.hologram = hologram;

        this.save();
    }

    public void setLocation(final Location location) {
        this.location = location;

        this.save();
    }

    public void setDisplayRange(final double displayRange) {
        this.displayRange = displayRange;

        this.save();
    }

    public void setPermission(final String permission) {
        this.permission = permission;

        this.save();
    }

    // -----------------------------------------------------------------
    // Static
    // -----------------------------------------------------------------

    /**
     * @see ConfigItems#getItemNames()
     */
    public static Set<String> getHologramIDs() {
        return loadedFiles.getItemNames();
    }

    public static List<? extends HologramRegistry> getHolograms() {
        return loadedFiles.getItems();
    }

    public static HologramRegistry createHologram(@NonNull final String hologramId, final Location location) {
        return loadedFiles.loadOrCreateItem(hologramId, () -> new HologramRegistry(hologramId, location));
    }

    /**
     * @see ConfigItems#loadItems()
     */
    public static void loadHolograms() {
        loadedFiles.loadItems();
    }

    public static void removeHologram(final String hologramId) {
        loadedFiles.removeItemByName(hologramId);
    }

    /**
     * @see ConfigItems#isItemLoaded(String)
     */
    public static boolean isHologramLoaded(final String id) {
        return loadedFiles.isItemLoaded(id);
    }

    /**
     * @return
     * @see ConfigItems#findItem(String)
     */
    public static HologramRegistry findById(@NonNull final String id) {
        return loadedFiles.findItem(id);
    }
}
