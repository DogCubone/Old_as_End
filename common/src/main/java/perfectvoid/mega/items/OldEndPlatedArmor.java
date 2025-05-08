package perfectvoid.mega.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class OldEndPlatedArmor extends ArmorItem {
    public OldEndPlatedArmor(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && hasFullArmor(player))
            return; //I'll add something here later
            //player.sendMessage(Text.literal("Tá com a armadura toda")); //just to see if it was working
    }

    private boolean hasFullArmor(PlayerEntity player) {
        for (ItemStack item : player.getInventory().armor)
            if (!(item.getItem() instanceof OldEndPlatedArmor))
                return false;

        return true;
    }
}
