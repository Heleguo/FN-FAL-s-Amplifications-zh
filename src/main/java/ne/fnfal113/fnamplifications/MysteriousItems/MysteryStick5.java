package ne.fnfal113.fnamplifications.MysteriousItems;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import ne.fnfal113.fnamplifications.FNAmplifications;
import ne.fnfal113.fnamplifications.Items.FNAmpItems;
import ne.fnfal113.fnamplifications.Multiblock.FnMysteryStickAltar;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ConstantConditions")
public class MysteryStick5 extends SlimefunItem {

    private static final SlimefunAddon plugin = FNAmplifications.getInstance();

    private final NamespacedKey defaultUsageKey;
    private final NamespacedKey defaultUsageKey2;

    public final MainStick mainStick;

    @ParametersAreNonnullByDefault
    public MysteryStick5(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        this.defaultUsageKey = new NamespacedKey(FNAmplifications.getInstance(), "axeexp_xp");
        this.defaultUsageKey2 = new NamespacedKey(FNAmplifications.getInstance(), "axeexpdamage_damage");
        this.mainStick = new MainStick(getStorageKey(), getStorageKey2(), enchantments(), weaponLore(), stickLore());
    }

    protected @Nonnull
    NamespacedKey getStorageKey() {
        return defaultUsageKey;
    }

    protected @Nonnull
    NamespacedKey getStorageKey2() {
        return defaultUsageKey2;
    }

    public Map<Enchantment, Integer> enchantments(){
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        enchantments.put(Enchantment.DAMAGE_ARTHROPODS, 6);
        enchantments.put(Enchantment.DAMAGE_ALL, 5);
        enchantments.put(Enchantment.DAMAGE_UNDEAD, 5);
        enchantments.put(Enchantment.KNOCKBACK, 3);

        return enchantments;
    }

    public String weaponLore(){
        return ChatColor.GOLD + "Another stick of somewhat reckoning";
    }

    public String stickLore(){
        return ChatColor.WHITE + "I know you are tired of this stick thing";
    }

    public void interact(PlayerInteractEvent e) {
        mainStick.onInteract(e, Material.DIAMOND_AXE, false);
    }

    public void onSwing(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player)){
            return;
        }
        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() != Material.DIAMOND_AXE){
            return;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> lore2 = meta.getLore();

        PersistentDataContainer expUsed = meta.getPersistentDataContainer();
        PersistentDataContainer damage = meta.getPersistentDataContainer();
        int damageamount = damage.getOrDefault(getStorageKey2(), PersistentDataType.INTEGER, 0);
        int get_Damage = (int) event.getDamage() + damageamount;
        int xpamount = expUsed.getOrDefault(getStorageKey(), PersistentDataType.INTEGER, 0);
        damage.set(getStorageKey2(), PersistentDataType.INTEGER, get_Damage);

        meta.setLore(mainStick.loreUpdate(lore2, get_Damage, xpamount, weaponLore(), false));
        item.setItemMeta(meta);

        if(player.getLevel() >= 15)  {
            if(ThreadLocalRandom.current().nextInt(100) < 30) {
                player.setLevel(player.getLevel() - 2);
            }
        } else{
            mainStick.darkenVision(player, 15);
            mainStick.transformWeapon(player, item, FNAmpItems.FN_STICK_5, 15, stickLore(), 1);
        }

    }

    public void LevelChange(PlayerLevelChangeEvent event){
        mainStick.levelChange(event, FNAmpItems.FN_STICK_5, 15, 1);
    }

    @Override
    public boolean isEnchantable() {
        return false;
    }

    @Override
    public boolean isDisenchantable() {
        return false;
    }

    @Override
    public boolean isUseableInWorkbench() {
        return false;
    }

    public static void setup(){
        new MysteryStick5(FNAmpItems.MYSTERY_STICKS, FNAmpItems.FN_STICK_5, FnMysteryStickAltar.RECIPE_TYPE, new ItemStack[]{
                new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_3, 18), new SlimefunItemStack(SlimefunItems.ESSENCE_OF_AFTERLIFE, 2), new SlimefunItemStack(SlimefunItems.ENDER_LUMP_2, 18),
                new SlimefunItemStack(SlimefunItems.AIR_RUNE, 3), FNAmpItems.FN_STICK_2, new SlimefunItemStack(SlimefunItems.FIRE_RUNE, 3),
                new SlimefunItemStack(SlimefunItems.ENDER_LUMP_3, 18), new SlimefunItemStack(SlimefunItems.ESSENCE_OF_AFTERLIFE, 2), new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_2, 18)})
                .register(plugin);
    }
}