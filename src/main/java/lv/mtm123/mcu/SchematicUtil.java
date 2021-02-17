package lv.mtm123.mcu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class SchematicUtil {

    private SchematicUtil() {
    }

    public static Schematic loadSchematic(final File file) throws ClassNotFoundException {

        final Class<?> NBTCompressedStreamTools = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "NBTCompressedStreamTools");

        try (FileInputStream in = new FileInputStream(file)) {
            Object nbtData = NBTCompressedStreamTools.getMethod("a", InputStream.class).invoke(null, in);

            Method getShort = nbtData.getClass().getMethod("getShort", String.class);
            Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);

            short width = (short) getShort.invoke(nbtData, "Width");
            short height = (short) getShort.invoke(nbtData, "Height");
            short length = (short) getShort.invoke(nbtData, "Length");
            byte[] blocks = (byte[]) getByteArray.invoke(nbtData, "Blocks");
            byte[] data = (byte[]) getByteArray.invoke(nbtData, "Data");

            return new Schematic(width, height, length, blocks, data);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class Schematic {
        private final short width;
        private final short height;
        private final short length;
        private final byte[] blocks;
        private final byte[] data;

        public Schematic(final short width, final short height, final short length, final byte[] blocks, final byte[] data) {
            this.width = width;
            this.height = height;
            this.length = length;
            this.blocks = blocks;
            this.data = data;
        }

        public byte[] getBlocks() {
            return this.blocks;
        }

        public short getHeight() {
            return this.height;
        }

        public byte[] getData() {
            return this.data;
        }

        public short getLength() {
            return this.length;
        }

        public short getWidth() {
            return this.width;
        }
    }
}
