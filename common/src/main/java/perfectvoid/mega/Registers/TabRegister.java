package perfectvoid.mega.Registers;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import perfectvoid.mega.old_as_end;


public class TabRegister {
    public static void init(){
        tab.register();
    }

    public static final DeferredRegister<ItemGroup> tab = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.ITEM_GROUP);

    public static final RegistrySupplier<ItemGroup> OLD_AS_END_TAB = tab.register("old_end_tab",
            () -> ItemGroup.create(ItemGroup.Row.TOP, 1).displayName(Text.translatable("old_as_end.tab")).icon(() ->
                    new ItemStack(BlocksAndItemsRegister.OLD_END_PLATE.get())).entries(((displayContext, entries) -> {
                        entries.add(ArsenalRegister.SHELL_HAMMER.get());
                        entries.add(BlocksAndItemsRegister.CHORUS_HONEY_BOTTLE.get());
                        entries.add(BlocksAndItemsRegister.GRAVITATIONAL_MUCUS_BOTTLE.get());
                        entries.add(BlocksAndItemsRegister.HAMMER_SNAIL_SHELL.get());
                        entries.add(BlocksAndItemsRegister.OLD_END_PLATE.get());
                        entries.add(BlocksAndItemsRegister.OLD_END_STICK.get());
                        entries.add(BlocksAndItemsRegister.OLD_END_ORE_ITEM.get());
                        entries.add(BlocksAndItemsRegister.OLD_END_ALLOY_BLOCK_ITEM.get());
                        entries.add(BlocksAndItemsRegister.ENDBEE_HIVE_ITEM.get());
                        entries.add(BlocksAndItemsRegister.CRYSTALLIZED_END_STONE_ITEM.get());
                        entries.add(BlocksAndItemsRegister.CRYSTALLIZED_END_STONE_PUPA_ITEM.get());
                        entries.add(BlocksAndItemsRegister.CRYSTALLIZED_END_STONE_BRICKS_ITEM.get());
                        entries.add(BlocksAndItemsRegister.CRYSTALLIZED_END_STONE_BRICKS_SLAB_ITEM.get());
                        entries.add(BlocksAndItemsRegister.CRYSTALLIZED_END_STONE_BRICKS_STAIRS_ITEM.get());
                        entries.add(BlocksAndItemsRegister.CRYSTALLIZED_END_STONE_BRICKS_WALL_ITEM.get());
                        entries.add(BlocksAndItemsRegister.CHORUS_HONEY_BLOCK_ITEM.get());
                        entries.add(BlocksAndItemsRegister.GRAVITATIONAL_MUCUS_BLOCK_ITEM.get());
                        entries.add(BlocksAndItemsRegister.STRANGE_MUCUS_ITEM.get());
                        entries.add(BlocksAndItemsRegister.POWERED_STRANGE_MUCUS_ITEM.get());
                        entries.add(BlocksAndItemsRegister.ULTRA_POWERED_STRANGE_MUCUS_ITEM.get());
                        entries.add(BlocksAndItemsRegister.LIMITER_REMOVER.get());
                        entries.add(ArsenalRegister.OLD_END_PLATED_HELMET.get());
                        entries.add(ArsenalRegister.OLD_END_PLATED_CHESTPLATE.get());
                        entries.add(ArsenalRegister.OLD_END_PLATED_LEGGINGS.get());
                        entries.add(ArsenalRegister.OLD_END_PLATED_B0OTS.get());
                        entries.add(BlocksAndItemsRegister.END_MUSSEL_SPAWN_EGG.get());
                        entries.add(BlocksAndItemsRegister.END_MUSSEL_BUCKET.get());
                        entries.add(BlocksAndItemsRegister.SHELL_HAMMER_SNAIL_SPAWN_EGG.get());
                        entries.add(BlocksAndItemsRegister.END_BEE_SPAWN_EGG.get());
                    })
            ).build());
}
