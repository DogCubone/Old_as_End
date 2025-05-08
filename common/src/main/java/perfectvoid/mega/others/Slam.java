package perfectvoid.mega.others;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import perfectvoid.mega.items.ShellHammer;

public class Slam extends Enchantment {

    public Slam(Rarity weight, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.WEAPON, slotTypes);
    }

    public int getMinPower(int level) {
        return 5 + (level - 1) * 8;
    } //I still need to change this later.

    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    public int getMaxLevel() {
        return 4;
    }

    public boolean isAcceptableItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof ShellHammer;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return !other.equals(Enchantments.KNOCKBACK);
    }
}
