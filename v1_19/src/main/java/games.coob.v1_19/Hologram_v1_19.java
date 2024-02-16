package games.coob.v1_19;

import games.coob.nmsinterface.HologramAPI;
import games.coob.nmsinterface.HologramRegistry;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
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

    private boolean savedToFile;


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
            this.savedToFile = true;
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
        this.setDisplayRange(registry.getDisplayRange());
        this.id = registry.getName();
        this.permission = registry.getPermission();

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
        // Assume lines is not null and contains the new lines to display
        int currentSize = this.entityLinesList.size();
        final int newSize = lines.length;

        if (this.savedToFile)
            HologramRegistry.findById(this.id).setLines(this.lines);

        // Update existing and remove excess lines
        for (int i = 0; i < currentSize; i++) {
            if (i < newSize) {
                // Update existing line
                final ArmorStand armorStand = this.entityLinesList.get(i);
                Remain.setCustomName(armorStand.getBukkitEntity(), lines[i]);
            } else {
                // Remove excess line
                final ArmorStand armorStand = this.entityLinesList.remove(i--);

                for (final UUID viewer : viewers)
                    Remain.sendPacket(Remain.getPlayerByUUID(viewer), new ClientboundRemoveEntitiesPacket(armorStand.getId()));

                armorStand.discard();
                currentSize--;
            }
        }

        // Add new lines if necessary
        for (int i = currentSize; i < newSize; i++) {
            final ArmorStand newArmorStand = this.addEntity(lines[i]);
            this.entityLinesList.add(newArmorStand);
        }

        refreshHologramForAllViewers();
    }

    private void refreshHologramForAllViewers() {
        this.viewers.forEach(uuid -> {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                hide(player);
                show(player);
            }
        });
    }

    @Override
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


    @Override
    public void addLines(final String... lines) {
        this.addLines(this.lines.size(), lines);
    }

    @Override
    public void removeLines(final Integer... indices) {
        this.lines = new ArrayList<>(this.lines);

        for (final int index : indices)
            this.lines.remove(index);

        updateLines(this.lines.toArray(new String[0]));
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
        armorStand.setInvisible(true);
        armorStand.persistentInvisibility = true;
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
}