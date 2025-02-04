package ne.fnfal113.fnamplifications.gems.implementation;

import lombok.Getter;
import ne.fnfal113.fnamplifications.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * @author FN_FAL113
 */
public class ReturnWeaponTask extends BukkitRunnable {

    @Getter
    private final ItemStack itemStack;
    @Getter
    private final ArmorStand armorStand;
    @Getter
    private final Player player;

    public ReturnWeaponTask(ItemStack itemStack, ArmorStand armorStand, Player player){
        this.itemStack = itemStack;
        this.armorStand = armorStand;
        this.player = player;
    }

    @Override
    public void run() {
        Location asLocation = getArmorStand().getLocation();
        Location pLocation = getPlayer().getLocation();
        Vector asVector = asLocation.toVector();
        Vector pVector = pLocation.toVector();

        // player vector subtracted to armorstand vector then normalize the
        // vector to be subtracted to armorstand location
        /*  This is my example to remind myself how I came up with this logic for future reference
            initial vector of the armorstand is the last hit location

            player location =
            x 15 y 4 z 4

            as location to vector =
            x 20 y 4 z 4

            player location to vector =
            x 15 y 4 z 4

            as location - (as location to vector - player location to vector)
            x 20 y 4 z 4
            x  5 y 0 z 0
            is going to equal to
            ↓
            player location

            this is an instant teleport to player but if (as to vector - player location to vector)
            value is converted to unit vector(normalized) then we are slowly decrementing the as location
            until it equals to player location
         */
        Location asLocationNormalized = asLocation.subtract(asVector.subtract(pVector).normalize()).setDirection(pLocation.getDirection());
        getArmorStand().teleport(asLocationNormalized);

        // if player is not online, drop the throwable immediately
        if(!getPlayer().isOnline()){
            dropItem(asLocation);
            stopTask();
        }

        // drop the item if the distance between player and throwable is 150 blocks away
        if(distanceBetween(asLocation, pLocation) > 150){
            Location dropLoc = dropItem(asLocation);
            getPlayer().sendMessage(Utils.colorTranslator("&c武器未能退还, 因为你离得太远了!"));
            getPlayer().sendMessage(Utils.colorTranslator("&c它掉落在了坐标 &e" +
                    "[" + (int) dropLoc.getX() + ", " + (int) dropLoc.getY() + ", " + (int) dropLoc.getZ()) + "]");

            stopTask();
        }

        if(distanceBetween(asLocation, pLocation) < 0.5){
            if(getPlayer().getInventory().firstEmpty() == -1){
                getPlayer().sendMessage(Utils.colorTranslator("&e你的背包已满! 武器将会掉落在地上"));
                dropItem(pLocation);
            } else {
                getPlayer().getInventory().addItem(getItemStack().clone());
            }

            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
            stopTask();
        }
    }

    public Location dropItem(Location location){ // drop the throwable weapon if player inventory is full
        Item droppedItem = getPlayer().getWorld().dropItem(location, getItemStack().clone());
        droppedItem.setOwner(getPlayer().getUniqueId());
        droppedItem.setGlowing(true);

        return droppedItem.getLocation();
    }

    // get the distance between two locations and return the square root of the distance
    public double distanceBetween(Location asLoc, Location pLoc){
        return asLoc.distance(pLoc);
    }

    public void stopTask(){ // stop the task once task has been completed
        getArmorStand().remove();

        this.cancel();
    }
}
