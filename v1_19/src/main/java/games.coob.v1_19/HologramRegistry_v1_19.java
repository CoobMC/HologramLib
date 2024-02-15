//package games.coob.v1_19;
//
//import games.coob.nmsinterface.HologramAPI;
//import games.coob.nmsinterface.HologramRegistryO;
//import lombok.NonNull;
//import org.mineacademy.fo.settings.ConfigItems;
//
//import java.util.List;
//import java.util.Set;
//
//public class HologramRegistry_v1_19 extends HologramRegistryO {
//
//    private static final String FOLDER = "holograms";
//
//    private static final ConfigItems<HologramRegistry_v1_19> loadedFiles = ConfigItems.fromFolder(FOLDER, HologramRegistry_v1_19.class);
//
//    protected HologramRegistry_v1_19(final String id) {
//        super(id);
//    }
//
//	/*protected Hologram_v1_19(final String id, @Nullable final Location location) {
//		super(id, location);
//	}*/
//
//    public HologramAPI loadHologramLines() {
//        return this.get("Hologram", Hologram_v1_19.class);
//    }
//
//	/*public HologramData createHologram(final String id, Location location, final Player player, final String... linesOfText) {
//		if (getHologramIDs().contains(id)) {
//			Messenger.error(player, "Unable to create hologram because it already exists!");
//			return null;
//		}
//
//		final World world = location.getWorld();
//
//		if (world == null)
//			return null;
//
//		final Object nmsWorld = Remain.getHandleWorld(location.getWorld());
//
//		this.setLines(Arrays.asList(linesOfText));
//
//		for (final String line : linesOfText) {
//			final ArmorStand nmsArmorStand = this.createEntity(nmsWorld, location, line);
//			//final org.bukkit.entity.ArmorStand armorStand = ReflectionUtil.invoke("getBukkitEntity", nmsArmorStand);
//
//			this.sendPackets(nmsArmorStand, player);
//			location = location.subtract(0, 0.26, 0);
//
//			this.entityLinesList.add(nmsArmorStand);
//		}
//
//		this.viewers.add(player.getUniqueId());
//		//player.setMetadata(getUniqueId().toString(), new FixedMetadataValue(SimplePlugin.getInstance(), ""));
//		return Hologram_v1_19.createInstance(id, location);
//	}*/
//
//	/*@Override
//	public void show(final Player player) {
//		if (!canShow(player))
//			return;
//
//		this.entityLinesList.forEach(armorStand -> {
//			armorStand.valid = true;
//			sendPackets(armorStand, player);
//		});
//
//		this.viewers.add(player.getUniqueId());
//		//player.setMetadata(getUniqueId().toString(), new FixedMetadataValue(SimplePlugin.getInstance(), ""));
//	}
//
//	@Override
//	public void hide(final Player player) {
//		if (!isViewer(player))
//			return;
//
//		this.entityLinesList.forEach(armorStand -> {
//			armorStand.valid = false;
//			//armorStand.remove(Entity.RemovalReason.DISCARDED); TODO might need to enable this
//			Remain.sendPacket(player, new ClientboundRemoveEntitiesPacket(armorStand.getId()));
//		});
//
//		this.viewers.remove(player.getUniqueId());
//		//player.removeMetadata(getUniqueId().toString(), SimplePlugin.getInstance());
//	}
//
//	@Override
//	public void remove() {
//		for (final UUID uuid : this.viewers) {
//			final Player player = Remain.getPlayerByUUID(uuid);
//			this.entityLinesList.forEach(armorStand -> Remain.sendPacket(player, new ClientboundRemoveEntitiesPacket(armorStand.getId())));
//			//player.removeMetadata(getUniqueId().toString(), SimplePlugin.getInstance());
//		}
//
//		if (isHologramLoaded(this.getId()))
//			removeHologram(this.getId());
//	}
//
//	@Override
//	public void updateVisibility(final Player player) {
//		if (!isInDisplayRange(player))
//			hide(player);
//		else if (isInDisplayRange(player) && !isViewer(player))
//			show(player);
//	}
//
//	@Override
//	public void updateLines(final String... lines) {
//		this.setLines(Arrays.asList(lines));
//
//		for (int i = 0; i < this.entityLinesList.size() && i < lines.length; i++) {
//			final ArmorStand armorStand = this.entityLinesList.get(i);
//			Remain.setCustomName(armorStand.getBukkitEntity(), lines[i]);
//		}
//
//		for (int i = this.entityLinesList.size() - 1; i >= lines.length; i--) {
//			final ArmorStand armorStand = this.entityLinesList.remove(i);
//			armorStand.remove(Entity.RemovalReason.DISCARDED);
//		}
//
//		for (int i = this.entityLinesList.size(); i < lines.length; i++) {
//			final ArmorStand newArmorStand = this.addEntity(lines[i]); // Assumes addEntity method adds a new ArmorStand with the given line
//			this.entityLinesList.add(newArmorStand);
//			final Player[] playerArray = this.viewers.stream().map(Bukkit::getPlayer).toArray(Player[]::new);
//			this.sendPackets(newArmorStand, playerArray);
//		}
//	}
//
//	@Override
//	public void addLines(final String... lines) {
//		final List<String> newLines = Arrays.asList(lines);
//		this.getLines().addAll(newLines);
//
//		for (final String line : lines) {
//			final ArmorStand nmsArmorStand = this.addEntity(line);
//			this.entityLinesList.add(nmsArmorStand);
//			final Player[] playerArray = this.viewers.stream().map(Bukkit::getPlayer).toArray(Player[]::new);
//			this.sendPackets(nmsArmorStand, playerArray);
//		}
//	}
//
//	@Override
//	public void removeLines(final Integer... indices) {
//		Arrays.sort(indices, Collections.reverseOrder());
//		for (final int index : indices) {
//			if (index >= 0 && index < this.getLines().size()) {
//				this.getLines().remove(index);
//			}
//		}
//
//		updateLines(this.getLines().toArray(new String[0]));
//	}
//
//	@Override
//	public boolean canShow(final Player player) {
//		if (this.getPermission() == null || isInDisplayRange(player))
//			return true;
//
//		return player != null && player.hasPermission(this.getPermission());
//	}
//
//	@Override
//	public boolean isViewer(final Player player) {
//		return this.viewers.contains(player.getUniqueId());
//	}
//
//	@Override
//	public boolean isInDisplayRange(final Player player) {
//		final Location location = this.getLocation();
//		final double range = this.getDisplayRange();
//
//		try {
//			if (player.getWorld().equals(location.getWorld())) {
//				return player.getLocation().distanceSquared(location) <= range * range;
//			}
//		} catch (final Exception ignored) {
//			// Ignored
//		}
//		return false;
//	}
//
//	@Override
//	public Set<UUID> getViewers() {
//		return this.viewers;
//	}
//
//	private ArmorStand createEntity(final Object nmsWorld, final Location location, final String line) {
//		this.entityArmorStand = new ArmorStand((ServerLevel) nmsWorld, location.getX(), location.getY(), location.getZ());
//
//		this.entityArmorStand.setMarker(true);
//		this.entityArmorStand.setSmall(true);
//		this.entityArmorStand.valid = true; // TODO if a player is to far it becomes invalid | test this
//		this.entityArmorStand.setInvisible(true);
//		this.entityArmorStand.collides = false;
//		Remain.setCustomName(this.entityArmorStand.getBukkitEntity(), line);
//
//		return this.entityArmorStand;
//	}
//
//	private ArmorStand addEntity(final String line) {
//		this.entityArmorStand = new ArmorStand((ServerLevel) getWorld(), getLocation().getX(), getLocation().getY() - 0.26, getLocation().getZ());
//
//		this.entityArmorStand.setMarker(true);
//		this.entityArmorStand.setSmall(true);
//		this.entityArmorStand.valid = true;
//		this.entityArmorStand.setInvisible(true);
//		this.entityArmorStand.collides = false;
//		Remain.setCustomName(this.entityArmorStand.getBukkitEntity(), line);
//
//		return this.entityArmorStand;
//	}
//
//	public void sendPackets(final Object nmsArmorStand, final Player... players) {
//		final ArmorStand nmsStand = (ArmorStand) nmsArmorStand;
//
//		for (final Player player : players) {
//			Remain.sendPacket(player, new ClientboundAddEntityPacket(nmsStand));
//			Remain.sendPacket(player, new ClientboundSetEntityDataPacket(nmsStand.getId(), nmsStand.getEntityData().getNonDefaultValues()));
//
//			//player.setMetadata(getUniqueId().toString(), new FixedMetadataValue(SimplePlugin.getInstance(), ""));
//		}
//	}
//
//	public World getWorld() {
//		return this.entityArmorStand.getBukkitEntity().getWorld();
//	}*/
//
//    // -----------------------------------------------------------------
//    // Static
//    // -----------------------------------------------------------------
//
//	/*public static HologramData createHologram(final String id, Location location, final Player player, final boolean saveToFile, final String... linesOfText) {
//		if (getHologramIDs().contains(id)) {
//			Messenger.error(player, "Unable to create hologram because it already exists!");
//			return null;
//		}
//
//		// Create an instance of Hologram_v1_19
//		final Hologram_v1_19 instance = saveToFile ? createInstance(id, location) : new Hologram_v1_19(id, location);
//
//		final World world = location.getWorld();
//
//		if (world == null)
//			return null;
//
//		final Object nmsWorld = Remain.getHandleWorld(location.getWorld());
//
//		instance.setLines(Arrays.asList(linesOfText));
//
//		for (final String line : linesOfText) {
//			final ArmorStand nmsArmorStand = instance.createEntity(nmsWorld, location, line);
//
//			instance.sendPackets(nmsArmorStand, player);
//			location = location.subtract(0, 0.26, 0);
//
//			instance.entityLinesList.add(nmsArmorStand);
//		}
//
//		instance.viewers.add(player.getUniqueId());
//		return instance;
//	}*/
//
//    public static void createInstance(@NonNull final String id) {
//        loadedFiles.loadOrCreateItem(id, () -> new HologramRegistry_v1_19(id));
//    }
//
//    public static List<HologramRegistry_v1_19> getHolograms() {
//        return loadedFiles.getItems();
//    }
//
//    public static void loadHolograms() {
//        loadedFiles.loadItems();
//    }
//
//    public static Set<String> getHologramIDs() {
//        return loadedFiles.getItemNames();
//    }
//
//    public static void removeHologram(final String id) {
//        loadedFiles.removeItemByName(id);
//    }
//
//    public static boolean isHologramLoaded(final String id) {
//        return loadedFiles.isItemLoaded(id);
//    }
//
//    public static HologramRegistry_v1_19 findById(@NonNull final String id) {
//        return loadedFiles.findItem(id);
//    }
//}
