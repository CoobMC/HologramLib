package games.coob.v1_19;

import games.coob.nmsinterface.HologramAPI;
import games.coob.nmsinterface.HologramRegistry;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;

import java.util.*;

public class Hologram_v1_19 implements HologramAPI {

    private final List<ArmorStand> entityLinesList = new ArrayList<>();

    @Setter
    @Getter
    private List<String> lines = new ArrayList<>();

    private final Set<UUID> viewers = new HashSet<>();

    @Setter
    @Getter
    private String id;

    private String permission;

    private double displayRange = 48.0;


    @Override
    public HologramAPI createHologram(final String id, Location location, final boolean saveToFile, final List<String> linesOfText, @Nullable final String permission) {
        if (HologramRegistry.getHologramIDs().contains(id)) {
            Common.log("Unable to create hologram because it already exists!");
            return null;
        }

        this.id = id;
        this.permission = permission;
        final World world = location.getWorld();

        if (world == null)
            return null;

        final Object nmsWorld = Remain.getHandleWorld(location.getWorld());

        this.setLines(linesOfText);

        for (final String line : linesOfText) {
            final ArmorStand nmsArmorStand = this.createEntity(nmsWorld, location, line);
            location = location.subtract(0, 0.26, 0);
            this.entityLinesList.add(nmsArmorStand);
        }

        if (saveToFile) {
            final HologramRegistry registry = HologramRegistry.createHologram(id, location);
            registry.setLines(linesOfText);
            registry.setHologram(this);
            registry.setPermission(permission);
            this.setDisplayRange(registry.getDisplayRange());
        }

        return this;
    }

    @Override
    public void createFromRegistry(final HologramRegistry registry) {
        final World world = registry.getLocation().getWorld();

        if (world == null)
            return;

        final Object nmsWorld = Remain.getHandleWorld(world);

        this.setLines(registry.getLines());

        for (final String line : registry.getLines()) {
            final ArmorStand nmsArmorStand = this.createEntity(nmsWorld, registry.getLocation(), line);

            registry.setLocation(registry.getLocation().subtract(0, 0.26, 0));
            this.entityLinesList.add(nmsArmorStand);
        }

        registry.setHologram(this);
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
    }

    @Override
    public void hide(final Player player) {
        if (!isViewer(player))
            return;

        this.entityLinesList.forEach(armorStand -> {
            armorStand.valid = false;
            Remain.sendPacket(player, new ClientboundRemoveEntitiesPacket(armorStand.getId()));
        });

        this.viewers.remove(player.getUniqueId());
    }

    @Override
    public void remove() {
        for (final UUID uuid : this.viewers) {
            final Player player = Remain.getPlayerByUUID(uuid);
            this.entityLinesList.forEach(armorStand -> {
                Remain.sendPacket(player, new ClientboundRemoveEntitiesPacket(armorStand.getId()));

            });
        }

        HologramRegistry.removeHologram(this.id);
    }

    @Override
    public void updateVisibility(final Player player) {
        if (!isInDisplayRange(player) && isViewer(player))
            hide(player);
        else if (isInDisplayRange(player) && !isViewer(player)) {
            show(player);
        }
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
            final ArmorStand newArmorStand = this.addEntity(lines[i]);

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

    public void addLines(int index, final String... lines) { // TODO
        if (index < 0 || index > this.lines.size()) {
            index = this.lines.size(); // Ensure the index is within bounds.
        }

        final List<String> newLines = Arrays.asList(lines);
        this.lines.addAll(index, newLines); // Insert new lines at the specified index.

        final Location baseLocation = getLocation().clone().add(0, 0.26 * (this.lines.size() - index), 0); // Calculate the base location for new armor stands.
        for (int i = 0; i < lines.length; i++) {
            final ArmorStand nmsArmorStand = this.createEntity(((CraftWorld) getWorld()).getHandle(), baseLocation, lines[i]);
            this.entityLinesList.add(index + i, nmsArmorStand); // Insert new ArmorStand entities at the correct index.
            baseLocation.subtract(0, 0.26, 0); // Adjust for the next line.
        }

        // Update visibility for all viewers to reflect changes.
        this.viewers.forEach(uuid -> {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                this.show(player); // Re-show the entire hologram to update its appearance.
            }
        });
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
        return (this.permission == null || player.hasPermission(this.permission)) && isInDisplayRange(player);
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
        final ArmorStand armorStand = new ArmorStand((Level) nmsWorld, location.getX(), location.getY(), location.getZ());
        setupArmorStandEntity(armorStand, line);
        return armorStand;
    }

    private ArmorStand addEntity(final String line) {
        final ArmorStand armorStand = new ArmorStand(((CraftWorld) getWorld()).getHandle(), this.getLocation().getX(), this.getLocation().getY() - 0.26, this.getLocation().getZ());
        setupArmorStandEntity(armorStand, line);
        return armorStand;
    }

    private void setupArmorStandEntity(final ArmorStand armorStand, final String line) {
        armorStand.setMarker(true);
        armorStand.setSmall(true);
        //armorStand.setInvisible(true);
        armorStand.persistentInvisibility = true; // TODO
        armorStand.setNoGravity(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(CraftChatMessage.fromStringOrNull(line));
    }

    private void sendPackets(final ArmorStand armorStand, final Player... players) {
        for (final Player player : players) {
            Remain.sendPacket(player, new ClientboundAddEntityPacket(armorStand));
            Remain.sendPacket(player, new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData().getNonDefaultValues()));
        }
    }

    @Override
    public void setPermission(final String permission) {
        this.permission = permission;
    }

    @Override
    public double getDisplayRange() {
        return this.displayRange;
    }

    @Override
    public void setDisplayRange(final double displayRange) {
        this.displayRange = displayRange;
    }

    @Override
    public Location getLocation() {
        return this.entityLinesList.get(entityLinesList.size() - 1).getBukkitEntity().getLocation();
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    public World getWorld() {
        return this.entityLinesList.get(entityLinesList.size() - 1).getBukkitEntity().getWorld();
    }

    /*@Override
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
    }*/
}