package games.coob.v1_19;

import games.coob.commons.HologramAPI;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.remain.Remain;

import java.util.*;

public class Hologram_v1_19 implements HologramAPI {

    private ArmorStand entityArmorStand;

    @Setter
    private List<ArmorStand> entityLinesList = new ArrayList<>();

    @Setter
    @Getter
    private List<String> lines = new ArrayList<>();

    private final Set<UUID> viewers = new HashSet<>();

    private String permission = null;

    private double displayRange = 48.0;


    @Override
    public HologramAPI createHologram(final String id, Location location, final Player player, final boolean saveToFile, final String... linesOfText) { // TODO create for general purposes not player specific
        if (HologramRegistry_v1_19.getHologramIDs().contains(id)) {
            Messenger.error(player, "Unable to create hologram because it already exists!");
            return null;
        }

        final World world = location.getWorld();

        if (world == null)
            return null;

        final Object nmsWorld = Remain.getHandleWorld(location.getWorld());

        this.setLines(Arrays.asList(linesOfText));

        for (final String line : linesOfText) {
            final ArmorStand nmsArmorStand = this.createEntity(nmsWorld, location, line);
            //final org.bukkit.entity.ArmorStand armorStand = ReflectionUtil.invoke("getBukkitEntity", nmsArmorStand);

            this.sendPackets(nmsArmorStand, player);
            location = location.subtract(0, 0.26, 0);

            this.entityLinesList.add(nmsArmorStand);
        }

        this.viewers.add(player.getUniqueId());
        //player.setMetadata(getUniqueId().toString(), new FixedMetadataValue(SimplePlugin.getInstance(), ""));

        if (saveToFile)
            Common.runLater(() -> HologramRegistry_v1_19.createInstance(id));

        return this;
    }

    @Override
    public void show(final Player player) {
        if (!canShow(player))
            return;

        this.entityLinesList.forEach(armorStand -> {
            armorStand.valid = true;
            sendPackets(armorStand, player);
        });

        this.viewers.add(player.getUniqueId());
        //player.setMetadata(getUniqueId().toString(), new FixedMetadataValue(SimplePlugin.getInstance(), ""));
    }

    @Override
    public void hide(final Player player) {
        if (!isViewer(player))
            return;

        this.entityLinesList.forEach(armorStand -> {
            armorStand.valid = false;
            //armorStand.remove(Entity.RemovalReason.DISCARDED); TODO might need to enable this
            Remain.sendPacket(player, new ClientboundRemoveEntitiesPacket(armorStand.getId()));
        });

        this.viewers.remove(player.getUniqueId());
        //player.removeMetadata(getUniqueId().toString(), SimplePlugin.getInstance());
    }

    @Override
    public void remove() {
        for (final UUID uuid : this.viewers) {
            final Player player = Remain.getPlayerByUUID(uuid);
            this.entityLinesList.forEach(armorStand -> Remain.sendPacket(player, new ClientboundRemoveEntitiesPacket(armorStand.getId())));
            //player.removeMetadata(getUniqueId().toString(), SimplePlugin.getInstance());
        }

        //if (Hologram_v1_19.isHologramLoaded(this.getId())) // TODO
        //removeHologram(this.getId());
    }

    @Override
    public void updateVisibility(final Player player) {
        if (!isInDisplayRange(player))
            hide(player);
        else if (isInDisplayRange(player) && !isViewer(player))
            show(player);
    }

    @Override
    public void updateLines(final String... lines) {
        this.setLines(Arrays.asList(lines));

        for (int i = 0; i < this.entityLinesList.size() && i < lines.length; i++) {
            final ArmorStand armorStand = this.entityLinesList.get(i);
            Remain.setCustomName(armorStand.getBukkitEntity(), lines[i]);
        }

        for (int i = this.entityLinesList.size() - 1; i >= lines.length; i--) {
            final ArmorStand armorStand = this.entityLinesList.remove(i);
            armorStand.remove(Entity.RemovalReason.DISCARDED);
        }

        for (int i = this.entityLinesList.size(); i < lines.length; i++) {
            final ArmorStand newArmorStand = this.addEntity(lines[i]); // Assumes addEntity method adds a new ArmorStand with the given line
            this.entityLinesList.add(newArmorStand);
            final Player[] playerArray = this.viewers.stream().map(Bukkit::getPlayer).toArray(Player[]::new);
            this.sendPackets(newArmorStand, playerArray);
        }
    }

    @Override
    public void addLines(final String... lines) {
        final List<String> newLines = Arrays.asList(lines);
        this.getLines().addAll(newLines);

        for (final String line : lines) {
            final ArmorStand nmsArmorStand = this.addEntity(line);
            this.entityLinesList.add(nmsArmorStand);
            final Player[] playerArray = this.viewers.stream().map(Bukkit::getPlayer).toArray(Player[]::new);
            this.sendPackets(nmsArmorStand, playerArray);
        }
    }

    @Override
    public void removeLines(final Integer... indices) {
        Arrays.sort(indices, Collections.reverseOrder());
        for (final int index : indices) {
            if (index >= 0 && index < this.getLines().size()) {
                this.getLines().remove(index);
            }
        }

        updateLines(this.getLines().toArray(new String[0]));
    }

    @Override
    public boolean canShow(final Player player) {
        if (this.permission == null && isInDisplayRange(player)) // TODO check if enabled
            return true;

        return player.hasPermission(this.permission);
    }

    @Override
    public boolean isViewer(final Player player) {
        return this.viewers.contains(player.getUniqueId());
    }

    @Override
    public boolean isInDisplayRange(final Player player) {
        final Location location = this.getLocation();
        final double range = this.displayRange;

        try {
            if (player.getWorld().equals(location.getWorld())) {
                return player.getLocation().distanceSquared(location) <= range * range;
            }
        } catch (final Exception ignored) {
            // Ignored
        }
        return false;
    }

    @Override
    public Set<UUID> getViewers() {
        return this.viewers;
    }

    private ArmorStand createEntity(final Object nmsWorld, final Location location, final String line) {
        this.entityArmorStand = new ArmorStand((ServerLevel) nmsWorld, location.getX(), location.getY(), location.getZ());

        this.entityArmorStand.setMarker(true);
        this.entityArmorStand.setSmall(true);
        this.entityArmorStand.valid = true; // TODO if a player is to far it becomes invalid | test this
        this.entityArmorStand.setInvisible(true);
        this.entityArmorStand.collides = false;
        Remain.setCustomName(this.entityArmorStand.getBukkitEntity(), line);

        return this.entityArmorStand;
    }

    private ArmorStand addEntity(final String line) {
        this.entityArmorStand = new ArmorStand((ServerLevel) getWorld(), getLocation().getX(), getLocation().getY() - 0.26, getLocation().getZ());

        this.entityArmorStand.setMarker(true);
        this.entityArmorStand.setSmall(true);
        this.entityArmorStand.valid = true;
        this.entityArmorStand.setInvisible(true);
        this.entityArmorStand.collides = false;
        Remain.setCustomName(this.entityArmorStand.getBukkitEntity(), line);

        return this.entityArmorStand;
    }

    public void sendPackets(final Object nmsArmorStand, final Player... players) {
        final ArmorStand nmsStand = (ArmorStand) nmsArmorStand;

        for (final Player player : players) {
            Remain.sendPacket(player, new ClientboundAddEntityPacket(nmsStand));
            Remain.sendPacket(player, new ClientboundSetEntityDataPacket(nmsStand.getId(), nmsStand.getEntityData().getNonDefaultValues()));

            //player.setMetadata(getUniqueId().toString(), new FixedMetadataValue(SimplePlugin.getInstance(), ""));
        }
    }

    @Override
    public void setPermission(final String permission) {
        this.permission = permission;
    }

    @Override
    public void setDisplayRange(final double displayRange) {
        this.displayRange = displayRange;
    }

    @Override
    public Location getLocation() {
        return this.entityArmorStand.getBukkitEntity().getLocation();
    }

    public World getWorld() {
        return this.entityArmorStand.getBukkitEntity().getWorld();
    }

    @Override
    public SerializedMap serialize() {
        return SerializedMap.ofArray(
                "Lines", this.lines,
                "Location", this.getLocation());
    }

    public static Hologram_v1_19 deserialize(final SerializedMap map) {
        final List<String> lines = map.getStringList("Lines");
        Location lastLocation = map.getLocation("Location");

        final World world = lastLocation.getWorld();

        if (world == null)
            return null;

        final Object nmsWorld = Remain.getHandleWorld(lastLocation.getWorld());
        final Hologram_v1_19 hologram = new Hologram_v1_19();
        final List<ArmorStand> armorStandList = new ArrayList<>();

        hologram.setLines(lines);

        for (final String line : lines) {
            armorStandList.add(hologram.createEntity(nmsWorld, lastLocation, line));
            lastLocation = lastLocation.subtract(0, 0.26, 0);
        }

        hologram.setEntityLinesList(armorStandList);

        return hologram;
    }
}