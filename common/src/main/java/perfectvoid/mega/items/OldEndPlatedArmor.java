package perfectvoid.mega.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class OldEndPlatedArmor extends ArmorItem {
    public OldEndPlatedArmor(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && hasFullArmor(player))
            player.sendMessage(Text.literal("Tá com a armadura toda"));
    }

    private boolean hasFullArmor(PlayerEntity player) {

        for (ItemStack item : player.getInventory().armor)
            if (!(item.getItem() instanceof ArmorItem))
                return false;

        ArmorItem helmet = (ArmorItem)player.getInventory().getArmorStack(0).getItem();
        ArmorItem chestplate = (ArmorItem) player.getInventory().getArmorStack(1).getItem();
        ArmorItem leggings = (ArmorItem) player.getInventory().getArmorStack(2).getItem();
        ArmorItem boots = (ArmorItem) player.getInventory().getArmorStack(3).getItem();

        return helmet.getMaterial() == material && chestplate.getMaterial() == material && leggings.getMaterial() == material && boots.getMaterial() == material;
    }
}
