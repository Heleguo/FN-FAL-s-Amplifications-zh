package ne.fnfal113.fnamplifications.gems.unbinder;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import ne.fnfal113.fnamplifications.FNAmplifications;
import ne.fnfal113.fnamplifications.gems.abstracts.AbstractGemUnbinder;
import ne.fnfal113.fnamplifications.gems.implementation.GemUnbinderTask;
import ne.fnfal113.fnamplifications.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BlemishedUnbindGem extends AbstractGemUnbinder {

    public BlemishedUnbindGem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 45);
    }

    @Override
    public int onRightClick(PlayerInteractEvent event, Player player) {
        if(player.getInventory().getItemInOffHand().getType() == Material.AIR){
            player.sendMessage(Utils.colorTranslator("&cYou have no item in your offhand to unbind gems at!"));
            return 0;
        }
        new GemUnbinderTask(player, player.getInventory().getItemInOffHand()).getGemsFromItem();

        return FNAmplifications.getInstance().getConfigManager().getValueById(this.getId() + "-unbind-chance");
    }

}