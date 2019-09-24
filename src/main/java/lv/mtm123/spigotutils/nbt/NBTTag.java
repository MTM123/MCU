package lv.mtm123.spigotutils.nbt;

import lv.mtm123.spigotutils.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NBTTag {

    private final Map<String, Object> data;

    public NBTTag() {
        data = new HashMap<>();
    }

    public NBTTag(Map<String, Object> data) {

        this.data = data;
    }

    public void set(String key, Object obj) {
        data.put(key, obj);
    }

    public void remove(String key, Object obj) {
        data.remove(key, obj);
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Object toNmsNBTTag() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> cNBTTagCompound = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagCompound");
        Class<?> cNBTBase = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTBase");

        Method mNBTTagCompoundSet = cNBTTagCompound.getDeclaredMethod("set", String.class, cNBTBase);

        Object tag = cNBTTagCompound.newInstance();
        for (Map.Entry<String, Object> e : data.entrySet()) {
            Object o = NBTUtil.wrapWithNBTTypeTag(e.getValue());
            mNBTTagCompoundSet.invoke(tag, e.getKey(), o);
        }

        return tag;
    }

}
