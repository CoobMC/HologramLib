//package games.coob.hologram.model;
//
//import games.coob.nmsinterface.HologramRegistryO;
//import games.coob.v1_19.HologramRegistry_v1_19;
//import org.mineacademy.fo.MinecraftVersion;
//import org.mineacademy.fo.exception.FoException;
//
//import java.util.List;
//import java.util.Set;
//
//public final class HologramRegistryProvider {
//
//    public static void loadHolograms() {
//        //if (MinecraftVersion.equals(MinecraftVersion.V.v1_19))
//         //   HologramRegistry_v1_19.loadHolograms();
//		/*else if (MinecraftVersion.equals(MinecraftVersion.V.v1_18))
//			return HologramRegistry_v1_18.getInstance();
//		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_17))
//			return HologramRegistry_v1_17.getInstance();
//		else if (MinecraftVersion.olderThan(MinecraftVersion.V.v1_17))
//			return HologramRegistry.getInstance();*/
//       // else
//         //   throw new FoException("Unsupported Minecraft version " + MinecraftVersion.getServerVersion());
//    }
//
//    public static List<? extends HologramRegistryO> getHolograms() {
//        if (MinecraftVersion.equals(MinecraftVersion.V.v1_19))
//            return HologramRegistry_v1_19.getHolograms();
//		/*else if (MinecraftVersion.equals(MinecraftVersion.V.v1_18))
//			return HologramRegistry_v1_18.getInstance();
//		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_17))
//			return HologramRegistry_v1_17.getInstance();
//		else if (MinecraftVersion.olderThan(MinecraftVersion.V.v1_17))
//			return HologramRegistry.getInstance();*/
//        else throw new FoException("Unsupported Minecraft version " + MinecraftVersion.getServerVersion());
//    }
//
//    public static Set<String> getHologramIDs() {
//        if (MinecraftVersion.equals(MinecraftVersion.V.v1_19))
//            return HologramRegistry_v1_19.getHologramIDs();
//		/*else if (MinecraftVersion.equals(MinecraftVersion.V.v1_18))
//			return HologramRegistry_v1_18.getInstance();
//		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_17))
//			return HologramRegistry_v1_17.getInstance();
//		else if (MinecraftVersion.olderThan(MinecraftVersion.V.v1_17))
//			return HologramRegistry.getInstance();*/
//        else throw new FoException("Unsupported Minecraft version " + MinecraftVersion.getServerVersion());
//    }
//
//    public static boolean isHologramLoaded(final String id) {
//        if (MinecraftVersion.equals(MinecraftVersion.V.v1_19))
//            return HologramRegistry_v1_19.isHologramLoaded(id);
//		/*else if (MinecraftVersion.equals(MinecraftVersion.V.v1_18))
//			return HologramRegistry_v1_18.getInstance();
//		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_17))
//			return HologramRegistry_v1_17.getInstance();
//		else if (MinecraftVersion.olderThan(MinecraftVersion.V.v1_17))
//			return HologramRegistry.getInstance();*/
//        else throw new FoException("Unsupported Minecraft version " + MinecraftVersion.getServerVersion());
//    }
//
//    public static HologramRegistryO findById(final String id) {
//        if (MinecraftVersion.equals(MinecraftVersion.V.v1_19))
//            return HologramRegistry_v1_19.findById(id);
//		/*else if (MinecraftVersion.equals(MinecraftVersion.V.v1_18))
//			return HologramRegistry_v1_18.getInstance();
//		else if (MinecraftVersion.equals(MinecraftVersion.V.v1_17))
//			return HologramRegistry_v1_17.getInstance();
//		else if (MinecraftVersion.olderThan(MinecraftVersion.V.v1_17))
//			return HologramRegistry.getInstance();*/
//        else throw new FoException("Unsupported Minecraft version " + MinecraftVersion.getServerVersion());
//    }
//}