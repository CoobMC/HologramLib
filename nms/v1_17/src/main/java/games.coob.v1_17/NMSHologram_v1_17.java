/*package games.coob.v1_17;

import games.coob.nmsinterface.NMSHologramI;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class NMSHologram_v1_17 implements NMSHologramI {

	/**
	 * The spawned NMS entity
	 */
	/*private ArmorStand entityArmorStand;

	private List<String> lines;

	public Object createEntity(final Object nmsWorld, final Location location) {
		entityArmorStand = new ArmorStand((ServerLevel) nmsWorld, location.getX(), location.getY(), location.getZ());

		/*if (!HologramRegistry_v1_17.getInstance().isRegistered(entityArmorStand.getBukkitEntity().getUniqueId()))
			HologramRegistry_v1_17.getInstance().register(this);*/

	/*	return entityArmorStand;
	}


	public void sendPackets(final Player player, final Object nmsArmorStand) {
		final ArmorStand nmsStand = (ArmorStand) nmsArmorStand;

		Remain.sendPacket(player, new ClientboundAddEntityPacket(nmsStand));
		Remain.sendPacket(player, new ClientboundSetEntityDataPacket(nmsStand.getId(), nmsStand.getEntityData(), true));
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
/*	@Override
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
		Remain.sendPacket(player, new ClientboundRemoveEntitiesPacket(this.entityArmorStand.getId()));
		//HologramRegistry_v1_17.getInstance().unregister(this);
		player.removeMetadata(getUniqueId().toString(), SimplePlugin.getInstance());
	}

	@Override
	public void hide(final Player player) {
		Remain.sendPacket(player, new ClientboundRemoveEntitiesPacket(this.entityArmorStand.getId()));
		player.removeMetadata(getUniqueId().toString(), SimplePlugin.getInstance());
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
	/*private boolean isCreated() {
		return entityArmorStand.getBukkitEntity().isValid();
	}

	@Override
	public SerializedMap serialize() {
		Valid.checkBoolean(this.isCreated(), "Cannot save non-created holograms");

		return SerializedMap.ofArray(
				"UUID", this.entityArmorStand.getBukkitEntity().getUniqueId(),
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
	/*public static NMSHologram_v1_17 deserialize(final SerializedMap map) {
		final List<String> lines = map.getStringList("Lines");
		final Location lastLocation = map.getLocation("Last_Location");
		final Object nmsWorld = Remain.getHandleWorld(lastLocation.getWorld());
		final NMSHologram_v1_17 hologram = new NMSHologram_v1_17();

		hologram.createEntity(nmsWorld, lastLocation);
		hologram.setLines(lines);

		return hologram;
	}

	@Override
	public void createHologram(final String id, Location location, final Player player, final String... linesOfText) {
		final World world = location.getWorld();

		if (world == null)
			return;

		final Object nmsWorld = Remain.getHandleWorld(location.getWorld());

		setLines(Arrays.asList(linesOfText));

		for (final String line : linesOfText) {
			final Object nmsArmorStand = this.createEntity(nmsWorld, location);
			final org.bukkit.entity.ArmorStand armorStand = ReflectionUtil.invoke("getBukkitEntity", nmsArmorStand);

			armorStand.setVisible(false);
			Remain.setCustomName(armorStand, line);
			this.sendPackets(player, nmsArmorStand);
			location = location.subtract(0, 0.26, 0);
		}

		player.setMetadata(getUniqueId().toString(), new FixedMetadataValue(SimplePlugin.getInstance(), ""));
	}

	@Override
	public void sendPackets(final Object nmsArmorStand, final Player... player) {

	}
}*/