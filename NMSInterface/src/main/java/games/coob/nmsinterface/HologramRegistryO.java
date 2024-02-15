package games.coob.nmsinterface;

import lombok.Getter;
import lombok.NonNull;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;
import java.util.Set;

@Getter
public class HologramRegistryO extends YamlConfig {

    private static final String FOLDER = "holograms";

    private static final ConfigItems<HologramRegistryO> loadedFiles = ConfigItems.fromFolder(FOLDER, HologramRegistryO.class);

    private final String id;

    private HologramAPI hologram;

    private boolean enabled;

    protected HologramRegistryO(final String id) {
        this.id = id;

        this.loadConfiguration(NO_DEFAULT, FOLDER + "/" + id + ".yml");
    }

	/*protected HologramData(final String id, @Nullable final Location location) {
		this.id = id;

		//if (location != null)
		//this.location = location;

		this.loadConfiguration(NO_DEFAULT, FOLDER + "/" + id + ".yml");
	}*/

    @Override
    protected void onLoad() {
        //this.hologram = loadHologramLines();
		/*this.location = this.getLocation("Location");
		this.displayRange = this.getDouble("Display_Range", 48.0);
		this.permission = this.getString("Permission", null);*/
        this.enabled = this.getBoolean("Enabled", true);

        this.save();
    }

    @Override
    protected void onSave() {
        this.set("Hologram", this.hologram);
		/*this.set("Location", this.location);
		this.set("Display_Range", this.displayRange);
		this.set("Permission", this.permission);*/
        this.set("Enabled", this.enabled);
    }

    private void createHologram(final List<String> lines) {

    }

    public void setHologramLines(final HologramAPI hologramLines) {
        this.hologram = hologramLines;

        this.save();
    }

    public void setPermission(final String permission) {
        this.hologram.setPermission(permission);

        this.save();
    }

    public void setDisplayRange(final double displayRange) {
        this.hologram.setDisplayRange(displayRange);

        this.save();
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;

        this.save();
    }

    //public abstract HologramData createHologram(String id, Location location, final Player player, final String... linesOfText);

    //public abstract HologramAPI loadHologramLines(); TODO

	/*public abstract void show(final Player player);

	public abstract void hide(Player player);

	public abstract void remove();

	public abstract void updateVisibility(Player player);

	public abstract void updateLines(String... lines);

	public abstract void addLines(String... lines);

	public abstract void removeLines(Integer... indices);

	public abstract boolean canShow(Player player);

	public abstract boolean isViewer(Player player);

	public abstract boolean isInDisplayRange(Player player);

	public abstract Set<UUID> getViewers();*/

    // -----------------------------------------------------------------
    // Static
    // -----------------------------------------------------------------


    /**
     * @return
     * @see ConfigItems#getItemNames()
     */
    public static Set<String> getHologramIDs() {
        return loadedFiles.getItemNames();
    }

    public static HologramRegistryO createHologram(@NonNull final String hologramId) {
        return loadedFiles.loadOrCreateItem(hologramId, () -> new HologramRegistryO(hologramId));
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
    public static HologramRegistryO findById(@NonNull final String id) {
        return loadedFiles.findItem(id);
    }
}
