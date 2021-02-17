package lv.mtm123.mcu.nbt;

import lv.mtm123.mcu.ReflectionUtil;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.NoSuchElementException;

@Deprecated
public final class NBTUtil {

    private NBTUtil() {
    }

    public static ItemStack addNBTDataToItem(ItemStack item, Map<String, Object> data) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        Class<?> cnmsItemStack = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "ItemStack");
        Class<?> cCraftItemStack = ReflectionUtil.getClass(ReflectionUtil.Package.CB, "inventory.CraftItemStack");

        Class<?> cNBTTagCompound = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagCompound");

        Object nmsItemStack = cCraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class).invoke(null, item);

        Object tag;
        if ((boolean) cnmsItemStack.getDeclaredMethod("hasTag").invoke(nmsItemStack)) {
            tag = cnmsItemStack.getDeclaredMethod("getTag").invoke(nmsItemStack);
        } else {
            tag = cNBTTagCompound.newInstance();
        }

        for (Map.Entry<String, Object> e : data.entrySet()) {
            Object o;
            if (e.getValue() instanceof NBTTag) {
                o = ((NBTTag) e.getValue()).toNmsNBTTag();
            } else {
                o = wrapWithNBTTypeTag(e.getValue());
            }

            setNBTDataToTag(tag, e.getKey(), o);
        }

        Method setTag = cnmsItemStack.getDeclaredMethod("setTag", cNBTTagCompound);
        setTag.invoke(nmsItemStack, tag);


        return (ItemStack) cCraftItemStack.getDeclaredMethod("asBukkitCopy", cnmsItemStack).invoke(null, nmsItemStack);

    }

    public static NBTTag getNBTTagFromItemStack(ItemStack item) {

        NBTTag nbtTag = new NBTTag();

        return null;
    }

    public static Object getNmsNBTTagFromItemStack(ItemStack item, String key) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> cnmsItemStack = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "ItemStack");
        Class<?> cCraftItemStack = ReflectionUtil.getClass(ReflectionUtil.Package.CB, "inventory.CraftItemStack");

        Class<?> cNBTTagCompound = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagCompound");

        Object nmsItemStack = cCraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class).invoke(null, item);

        if ((boolean) cnmsItemStack.getDeclaredMethod("hasTag").invoke(nmsItemStack)) {
            Object tag = cnmsItemStack.getDeclaredMethod("getTag").invoke(nmsItemStack);
            if ((boolean) cNBTTagCompound.getDeclaredMethod("hasKey", String.class).invoke(tag, key)) {
                return cNBTTagCompound.getDeclaredMethod("get", String.class).invoke(tag, key);
            }
        }

        throw new NoSuchElementException("No such element found for that key!");

    }

    public static Object wrapWithNBTTypeTag(Object object) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<?> parameterType = null;
        Class<?> nbttypeclass = null;
        if (object.getClass() == Short.class) {
            nbttypeclass = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagShort");
            parameterType = short.class;
        } else if (object.getClass() == Integer.class) {
            nbttypeclass = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagInt");
            parameterType = int.class;
        } else if (object.getClass() == String.class) {
            nbttypeclass = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagString");
            parameterType = String.class;
        } else if (object.getClass() == int[].class) {
            nbttypeclass = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagIntArray");
            parameterType = int[].class;
        } else if (object.getClass() == Byte.class) {
            nbttypeclass = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagByte");
            parameterType = byte.class;
        } else if (object.getClass() == byte[].class) {
            nbttypeclass = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagByteArray");
            parameterType = byte[].class;
        } else if (object.getClass() == Float.class) {
            nbttypeclass = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagFloat");
            parameterType = float.class;
        } else if (object.getClass() == Double.class) {
            nbttypeclass = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagDouble");
            parameterType = double.class;
        }

        if (nbttypeclass != null) {
            Constructor cons = nbttypeclass.getConstructor(parameterType);
            object = cons.newInstance(object);

            return object;
        } else {
            throw new IllegalArgumentException("Can't wrap type: " + object.getClass().getCanonicalName()
                    + " with NBT Type Tag. Incorrect type provided!");
        }

    }

    public static void setNBTDataToTag(Object tag, String key, Object data) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> cNBTTagCompound = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagCompound");
        Class<?> cNBTBase = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTBase");

        Method set = cNBTTagCompound.getDeclaredMethod("set", String.class, cNBTBase);
        if (data instanceof NBTTag) {
            data = ((NBTTag) data).toNmsNBTTag();
        } else {
            if (!cNBTBase.isInstance(data)) {
                data = wrapWithNBTTypeTag(data);
            }
        }

        set.invoke(tag, key, data);

    }

    public static void removeNBTDataFromTag(Object tag, String key) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class<?> cNBTTagCompound = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagCompound");

        Method remove = cNBTTagCompound.getDeclaredMethod("remove", String.class);
        remove.invoke(tag, key);
    }

    public static String itemToJSONString(ItemStack item) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> cnmsItemStack = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "ItemStack");
        Class<?> cCraftItemStack = ReflectionUtil.getClass(ReflectionUtil.Package.CB, "inventory.CraftItemStack");

        Class<?> cNBTTagCompound = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTTagCompound");

        Method asNMSCopy = cCraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
        Method save = cnmsItemStack.getDeclaredMethod("save", cNBTTagCompound);

        Object nmsItemStack = asNMSCopy.invoke(null, item);
        Object nbtTagCompound = cNBTTagCompound.newInstance();

        save.invoke(nmsItemStack, nbtTagCompound);

        return nbtTagCompound.toString();
    }
}
