package lv.mtm123.spigotutils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class WorldUtil {

    private WorldUtil(){}

    //Based on nms Explosion
    public static Set<Block> generateExplosion(Location loc, float power){

        World world = loc.getWorld();

        double posX = loc.getX();
        double posY = loc.getY();
        double posZ = loc.getZ();

        HashSet<Block> affectedBlocks = new HashSet<>();

        if (power < 0.1F) {
            return affectedBlocks;
        }

        for (int k = 0; k < 16; ++k) {
            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    if (k == 0 || k == 15 || i == 0 || i == 15 || j == 0 || j == 15) {
                        double d0 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                        double d1 = (double) ((float) i / 15.0F * 2.0F - 1.0F);
                        double d2 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = power * (0.7F + ThreadLocalRandom.current().nextFloat() * 0.6F);
                        double d4 = posX;
                        double d5 = posY;
                        double d6 = posZ;

                        while (f > 0.0F){
                            Block blockposition = world.getBlockAt(new Location(world, d4, d5, d6));
                            Material mat = blockposition.getType();

                            if (mat != Material.AIR) {
                                float f2 =  getBlockDurability(blockposition) / 5.0F;
                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && blockposition.getY() < 256 && blockposition.getY() >= 0) {
                                affectedBlocks.add(blockposition);
                            }

                            d4 += d0 * 0.30000001192092896D;
                            d5 += d1 * 0.30000001192092896D;
                            d6 += d2 * 0.30000001192092896D;

                            f -= 0.22500001F;
                        }
                    }
                }
            }
        }


        return affectedBlocks;
    }

    public static Set<Block> getBlocksInPlane(Axis axis, Block c, int radius){

        radius = Math.abs(radius);

        Set<Block> blocks = new HashSet<>();

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {

                int x = 0;
                int y = 0;
                int z = 0;

                if (axis == Axis.X) {
                    y = i;
                    z = j;
                } else if (axis == Axis.Y) {
                    x = i;
                    z = j;
                } else if (axis == Axis.Z) {
                    x = i;
                    y = j;
                }

                blocks.add(c.getRelative(x, y, z));
            }
        }

        return blocks;


    }

    public static float getBlockDurability(Block block){
        try {

            Class<?> cCraftMagicNumbers = ReflectionUtil.getClass(ReflectionUtil.Package.CB, "util.CraftMagicNumbers");
            Method getBlock = cCraftMagicNumbers.getMethod("getBlock", Material.class);

            Class<?> cNMSBlock = ReflectionUtil.getClass(ReflectionUtil.Package.NMS, "Block");


            Field durabilityField = cNMSBlock.getDeclaredField("durability");

            Object nmsblock = getBlock.invoke(null, block.getType());

            durabilityField.setAccessible(true);

            float dur = (float) durabilityField.get(nmsblock);

            durabilityField.setAccessible(false);

            return dur;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    public static Set<Block> getAdjacentBlocks(Block c){

        Set<Block> blocks = new HashSet<>();

        blocks.add(c.getRelative(1, 0, 0));
        blocks.add(c.getRelative(-1, 0, 0));
        blocks.add(c.getRelative(0, 1, 0));
        blocks.add(c.getRelative(0, -1, 0));
        blocks.add(c.getRelative(0, 0, 1));
        blocks.add(c.getRelative(0, 0, -1));

        return blocks;

    }

    public static BlockFace invertFace(BlockFace bf){
        switch (bf){
            case WEST:
                return BlockFace.EAST;
            case EAST:
                return BlockFace.WEST;
            case UP:
                return BlockFace.DOWN;
            case DOWN:
                return BlockFace.UP;
            case NORTH:
                return BlockFace.SOUTH;
            case SOUTH:
                return BlockFace.NORTH;
            case NORTH_WEST:
                return BlockFace.SOUTH_EAST;
            case NORTH_EAST:
                return BlockFace.SOUTH_WEST;
            case SOUTH_WEST:
                return BlockFace.NORTH_EAST;
            case SOUTH_EAST:
                return BlockFace.NORTH_WEST;

                default:
                    return BlockFace.SELF;
        }
    }
}
