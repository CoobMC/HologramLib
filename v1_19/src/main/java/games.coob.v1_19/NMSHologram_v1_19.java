package games.coob.v1_19;

import games.coob.nmsinterface.NMSHologramI;
import lombok.Getter;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NMSHologram_v1_19 implements NMSHologramI {

	/**
	 * The spawned NMS entity
	 */
	@Getter
	private ArmorStand entityArmorStand;

	private final List<ArmorStand> entityLinesList = new ArrayList<>();

	private List<String> lines;

	public Object createEntity(final Object nmsWorld, final Location location) {
		entityArmorStand = new ArmorStand((ServerLevel) nmsWorld, location.getX(), location.getY(), location.getZ());
		return entityArmorStand;
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
	@Override
	public Location getLocation() {
		Valid.checkBoolean(this.isCreated(), "Cannot call getLocation when " + this + " is not created");

		return this.entityArmorStand.getBukkitEntity().getLocation();
	}

	@Override
	public void sendPackets(final Player player, final Object nmsArmorStand) {
		final ArmorStand nmsStand = (ArmorStand) nmsArmorStand;

		System.out.println("sending packet");

		Remain.sendPacket(player, new ClientboundAddEntityPacket(nmsStand));
		Remain.sendPacket(player, new ClientboundSetEntityDataPacket(nmsStand.getId(), nmsStand.getEntityData().packDirty()));
		player.setMetadata(getUniqueId().toString(), new FixedMetadataValue(SimplePlugin.getInstance(), ""));
	}

	@Override
	public void setLines(final List<String> lines) {
		this.lines = lines;
	}

	@Override
	public List<String> getLines() {
		return this.lines;
	}

	@Override
	public void remove(final Player player) {
		for (final ArmorStand armorStand : this.entityLinesList)
			Remain.sendPacket(player, new ClientboundRemoveEntitiesPacket(armorStand.getId()));

		HologramRegistry_v1_19.getInstance().unregister(this);
		player.removeMetadata(getUniqueId().toString(), SimplePlugin.getInstance());
		System.out.println("removed");
	}

	@Override
	public void hide(final Player player) {
		for (final ArmorStand armorStand : this.entityLinesList)
			Remain.sendPacket(player, new ClientboundRemoveEntitiesPacket(armorStand.getId()));

		player.removeMetadata(getUniqueId().toString(), SimplePlugin.getInstance());
		System.out.println("hiding");
	}

	@Override
	public void show(final Location location, final Player player, final String... linesOfText) {
		System.out.println("showing");

		for (final ArmorStand armor : this.entityLinesList) { // TODO
			final String line1 = armor.getName().getString();
			System.out.println("Name: " + line1);
		}

		for (int i = 0; i < this.lines.size(); i++) {
			System.out.println("count: " + i);
			final String line = this.lines.get(i);
			final ArmorStand nmsArmorStand = this.entityLinesList.get(i);

			System.out.println("Line: " + line);
			System.out.println("Hologram: " + nmsArmorStand);

			final org.bukkit.entity.ArmorStand armorStand = ReflectionUtil.invoke("getBukkitEntity", nmsArmorStand);

			Remain.setCustomName(armorStand, line);
			this.sendPackets(player, nmsArmorStand);
		}

		player.setMetadata(getUniqueId().toString(), new FixedMetadataValue(SimplePlugin.getInstance(), ""));
	}

	@Override
	public void createHologram(Location location, final Player player, final String... linesOfText) {
		final World world = location.getWorld();

		if (world == null)
			return;

		System.out.println("creating");

		final Object nmsWorld = Remain.getHandleWorld(location.getWorld());

		setLines(Arrays.asList(linesOfText));

		for (final String line : linesOfText) {
			final Object nmsArmorStand = this.createEntity(nmsWorld, location);
			final org.bukkit.entity.ArmorStand armorStand = ReflectionUtil.invoke("getBukkitEntity", nmsArmorStand);

			armorStand.setVisible(false);
			Remain.setCustomName(armorStand, line);
			this.sendPackets(player, nmsArmorStand);
			location = location.subtract(0, 0.26, 0);
			this.entityLinesList.add((ArmorStand) nmsArmorStand);
		}

		player.setMetadata(getUniqueId().toString(), new FixedMetadataValue(SimplePlugin.getInstance(), ""));
		HologramRegistry_v1_19.getInstance().register(this);
	}

	/**
	 * Return if this hologram is spawned
	 *
	 * @return
	 */
	private boolean isCreated() { // TODO test this
		return entityArmorStand != null;
	}

	@Override
	public boolean isHidden() { // TODO check if it works
		return this.entityArmorStand.isRemoved();
	}

	@Override
	public SerializedMap serialize() {
		Valid.checkBoolean(this.isCreated(), "Cannot save non-created holograms");

		return SerializedMap.ofArray(
				"UUID", this.entityArmorStand.getBukkitEntity().getUniqueId(),
				"Lines", this.lines,
				"Last_Location", this.getLocation());
	}

	public static NMSHologram_v1_19 deserialize(final SerializedMap map) { // TODO
		final List<String> lines = map.getStringList("Lines");
		final Location lastLocation = map.getLocation("Last_Location");
		final Object nmsWorld = Remain.getHandleWorld(lastLocation.getWorld());
		final NMSHologram_v1_19 hologram = new NMSHologram_v1_19();

		hologram.createEntity(nmsWorld, lastLocation);
		hologram.setLines(lines);

		return hologram;
	}
}