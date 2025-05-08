package perfectvoid.mega.Registers;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import perfectvoid.mega.items.OldEndPlatedArmor;
import perfectvoid.mega.items.ShellHammer;
import perfectvoid.mega.old_as_end;
import perfectvoid.mega.others.ArmorMaterials;
import perfectvoid.mega.others.Materials;

public class ArsenalRegister {

    public static void init(){
        armor.register();
        weapon.register();
    }

    public static final DeferredRegister<Item> armor = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.ITEM);
    public static final DeferredRegister<Item> weapon = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.ITEM);


    public static final RegistrySupplier<Item> SHELL_HAMMER = weapon.register("shell_hammer", ()->
            new ShellHammer(3, -3.3f, 1.5f, new Item.Settings(), Materials.Shell));


    public static final RegistrySupplier<Item> OLD_END_PLATED_HELMET = armor.register("old_end_plated_helmet", ()->
            new OldEndPlatedArmor(ArmorMaterials.OLD_END_PLATE, ArmorItem.Type.HELMET, new Item.Settings()));
    public static final RegistrySupplier<Item> OLD_END_PLATED_CHESTPLATE = armor.register("old_end_plated_chestplate", ()->
            new OldEndPlatedArmor(ArmorMaterials.OLD_END_PLATE, ArmorItem.Type.CHESTPLATE, new Item.Settings()));
    public static final RegistrySupplier<Item> OLD_END_PLATED_LEGGINGS = armor.register("old_end_plated_leggings", ()->
            new OldEndPlatedArmor(ArmorMaterials.OLD_END_PLATE, ArmorItem.Type.LEGGINGS, new Item.Settings()));
    public static final RegistrySupplier<Item> OLD_END_PLATED_B0OTS = armor.register("old_end_plated_boots", ()->
            new OldEndPlatedArmor(ArmorMaterials.OLD_END_PLATE, ArmorItem.Type.BOOTS, new Item.Settings()));

}
