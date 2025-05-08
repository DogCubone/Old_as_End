package perfectvoid.mega.others;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;
import perfectvoid.mega.old_as_end;

public enum ArmorMaterials implements ArmorMaterial {

    OLD_END_PLATE("old_end_plated", 15, new int[] {0, 4, 3, 1}, 9, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            Ingredient.ofItems(BlocksAndItemsRegister.OLD_END_PLATE.get()));

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Ingredient repairIngredient;

    ArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound,
                   float toughness, float knockbackResistance, Ingredient repairIngredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredientSupplier;
    }

    private static final int[] baseDurability = {10, 15, 13, 10};

    public int getDurability(ArmorItem.Type type) {
        return baseDurability[type.ordinal()] * this.durabilityMultiplier;
    }

    public int getProtection(ArmorItem.Type type) {
        return this.protectionAmounts[type.ordinal()];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }

    public String getName() {
        return old_as_end.MOD_ID + ':' + this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public String asString() {
        return this.name;
    }
}
