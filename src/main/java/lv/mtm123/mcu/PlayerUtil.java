package lv.mtm123.mcu;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public final class PlayerUtil {

    private PlayerUtil() {
    }

/*    public static Player getOfflinePlayer(){

    }*/

    public static Axis getAxisAlongPlayerDirection(Player player) {

        switch (getFacing(player.getLocation())) {

            case EAST:
            case WEST:
                return Axis.X;
            case UP:
            case DOWN:
                return Axis.Y;
            case SOUTH:
            case NORTH:
                return Axis.Z;
            default:
                return Axis.OTHER;
        }

    }

    //Based on DarkSeraphim's gist: https://gist.github.com/DarkSeraphim/33a644bde86a232104d9
    public static BlockFace getFacing(Location loc) {

        float pitch = loc.getPitch();

        if (pitch < 0)
            pitch += 360F;

        pitch %= 360F;

        int pitchdir = Math.round(pitch / 90F) % 4;

        switch (pitchdir) {
            case 1:
                return BlockFace.UP;
            case 3:
                return BlockFace.DOWN;
            default:
                break;
        }


        float yaw = loc.getYaw();

        if (yaw < 0)
            yaw += 360F;

        yaw %= 360F;

        int yawdir = Math.round(yaw / 90F) % 4;

        switch (yawdir) {
            case 0:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.EAST;
        }

        return BlockFace.SELF;
    }
}
