package lv.mtm123.spigotutils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    public static Class<?> getClass(final Package pckg, final String name) throws ClassNotFoundException {
        return Class.forName(pckg.value() + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
    }

    public static void sendPacket(Player player, Object packet) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {

        Class<?> cCraftPlayer = ReflectionUtil.getClass(ReflectionUtil.Package.CB, "entity.CraftPlayer");
        Method getHandle = cCraftPlayer.getMethod("getHandle");
        Class<?> nEntityPlayer = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "EntityPlayer");

        Field playerConn = nEntityPlayer.getField("playerConnection");

        Class<?> nPacket = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "Packet");

        Object nmsPlayer = getHandle.invoke(cCraftPlayer.cast(player));
        Method sendPacket = playerConn.get(nmsPlayer).getClass().getMethod("sendPacket", nPacket);
        sendPacket.invoke(playerConn.get(nmsPlayer), packet);

    }

    public enum Package {
        NMS("net.minecraft.server."),
        CB("org.bukkit.craftbukkit.");

        private final String pckge;

        Package(final String pckge) {
            this.pckge = pckge;
        }

        public String value() {
            return this.pckge;
        }
    }

}
