/*package games.coob.hologram.model;

import games.coob.nmsinterface.NMSHologramI;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.remain.Remain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class NMSHologram_v1_8 implements NMSHologramI {

	/**
	 * The spawned NMS entity
	 */
	/*private EntityArmorStand entityArmorStand;

	private List<String> lines;

	@Override
	public void createHologram(final String id, final Location location, final Player player, final String... linesOfText) {

	}

	@Override
	public void sendPackets(final Object nmsArmorStand, final Player... player) {

	}

	public Object createEntity(final Object nmsWorld, final Location location) {
		entityArmorStand = new EntityArmorStand((WorldServer) nmsWorld, location.getX(), location.getY(), location.getZ());

		//if (!HologramRegistryI.isRegistered(entityArmorStand.getUniqueID()))
		//HologramRegistryI.register(this);

		return entityArmorStand;
	}


	public void sendPackets(final Player player, final Object nmsArmorStand) {
		final EntityArmorStand nmsStand = (EntityArmorStand) nmsArmorStand;

		Remain.sendPacket(player, new PacketPlayOutSpawnEntityLiving(nmsStand));
		Remain.sendPacket(player, new PacketPlayOutEntityMetadata(nmsStand.getId(), nmsStand.getDataWatcher(), true));
	}

	@Override
	public UUID getUniqueId() {
		return this.entityArmorStand.getBukkitEntity().getUniqueId();
	}

	/**
	 * Convenience method to return the location of this NPC.
	 *
	 * @return
	 */
	/*@Override
	public Location getLocation() {
		Valid.checkBoolean(this.isCreated(), "Cannot call getLocation when " + this + " is not created");

		return this.entityArmorStand.getBukkitEntity().getLocation();
	}

	@Override
	public void updateLines(final String... lines) {

	}

	@Override
	public void addLines(final String... lines) {

	}

	@Override
	public void removeLines(final Integer... index) {

	}


	public void setLines(final List<String> lines) {
		this.lines = lines;
	}

	@Override
	public List<String> getLines() {
		return this.lines;
	}

	@Override
	public void remove() {

	}


	public void remove(final Player player) {

	}

	@Override
	public void hide(final Player player) {

	}

	@Override
	public void hideAll() {

	}

	@Override
	public void show(final Player player) {

	}

	@Override
	public void showAll() {

	}


	public boolean isShown(final Player player) {
		return false;
	}

	@Override
	public boolean isViewer(final Player player) {
		return false;
	}

	@Override
	public Set<UUID> getViewers() {
		return null;
	}

	/**
	 * Return if this hologram is spawned
	 *
	 * @return
	 */
	/*boolean isCreated() {
		return entityArmorStand.getBukkitEntity() != null;
	}

	@Override
	public SerializedMap serialize() {
		Valid.checkBoolean(this.isCreated(), "Cannot save non-created holograms");

		return SerializedMap.ofArray(
				"UUID", this.entityArmorStand.getUniqueID(),
				"Lines", this.lines,
				"Last_Location", this.getLocation());
	}

	/**
	 * Converts information saved in data.db file as a map into an NPC,
	 * also spawning it. After spawn this NPC will auto register in {@link NPCRegistry}
	 *
	 * @param map
	 * @return
	 */
	/*public static NMSHologram_v1_8 deserialize(final SerializedMap map) {
		final List<String> lines = map.getStringList("Lines");
		final Location lastLocation = map.getLocation("Last_Location");
		final Object nmsWorld = Remain.getHandleWorld(lastLocation.getWorld());
		final NMSHologram_v1_8 hologram = new NMSHologram_v1_8();

		hologram.createEntity(nmsWorld, lastLocation);
		hologram.setLines(lines);

		return hologram;
	}
}*/