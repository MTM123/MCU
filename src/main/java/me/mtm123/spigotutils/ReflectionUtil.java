package me.mtm123.spigotutils;

import org.bukkit.Bukkit;

public final class ReflectionUtil {

    private ReflectionUtil(){}

    public static Class<?> getClass(final Package pckg, final String name) throws ClassNotFoundException {
        return Class.forName(pckg.value() + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
    }
    
    public enum Package {
        MINECRAFT("net.minecraft.server."), 
        CRAFTBUKKIT("org.bukkit.craftbukkit.");
        
        private final String pckge;
        
        Package(final String pckge) {
            this.pckge = pckge;
        }
        
        public String value() {
            return this.pckge;
        }
    }
}
