package perfectvoid.mega.Registers;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import perfectvoid.mega.blocks.*;
import perfectvoid.mega.blocks.blockEntity.EndBeeHiveEntity;
import perfectvoid.mega.items.ChorusHoney;
import perfectvoid.mega.items.Mucus_Bottle;
import perfectvoid.mega.items.MusselEntityBucket;
import perfectvoid.mega.old_as_end;

import static net.minecraft.item.Items.GLASS_BOTTLE;

public class BlocksAndItemsRegister {

    public static void init(){
        blocks.register();
        blockEntity.register();
        items.register();
    }

    public static final DeferredRegister<Item> items = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.ITEM);
    public static final DeferredRegister<Block> blocks = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.BLOCK);
    public static final DeferredRegister<BlockEntityType<?>> blockEntity = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);

    //region Block item Register
    private static RegistrySupplier<Item> bottleBlock(RegistrySupplier<Block> block, Item.Settings settings){
        return items.register(block.getId().getPath(), () -> new Mucus_Bottle(block.get(), settings));
    }

    private static RegistrySupplier<Item> blockItem(RegistrySupplier<Block> block){
        return items.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Settings()));
    }
    //endregion Block item Register

    public static final RegistrySupplier<Item> END_BEE_SPAWN_EGG                           = items.register("end_bee_spawn_egg", ()-> new ArchitecturySpawnEggItem(EntityRegister.END_BEE, -4888876, -1124626, new Item.Settings()));
    public static final RegistrySupplier<Item> SHELL_HAMMER_SNAIL_SPAWN_EGG                = items.register("shell_hammer_snail_spawn_egg", ()-> new ArchitecturySpawnEggItem(EntityRegister.SHELL_HAMMER_SNAIL_ENTITY,-6854249, -1642323, new Item.Settings()));
    public static final RegistrySupplier<Item> END_MUSSEL_SPAWN_EGG                        = items.register("end_mussel_spawn_egg", ()-> new ArchitecturySpawnEggItem(EntityRegister.END_MUSSEL_ENTITY,-1642323, -5001359, new Item.Settings()));
    public static final RegistrySupplier<Item> END_MUSSEL_BUCKET                           = items.register("end_mussel_bucket", ()-> new MusselEntityBucket(EntityRegister.END_MUSSEL_ENTITY.get(), SoundEvents.ITEM_BUCKET_EMPTY, new Item.Settings().maxCount(1)));

    //region Items
    public static final RegistrySupplier<Item> CHORUS_HONEY_BOTTLE                         = items.register("chorus_honey_bottle", ()->
            new ChorusHoney(new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().hunger(6).alwaysEdible().build()).maxCount(16)));
    public static final RegistrySupplier<Item> OLD_END_PLATE                               = items.register("old_end_plate", ()-> new Item(new Item.Settings().fireproof()));
    public static final RegistrySupplier<Item> OLD_END_STICK                               = items.register("old_end_stick", ()-> new Item(new Item.Settings().fireproof()));
    public static final RegistrySupplier<Item> HAMMER_SNAIL_SHELL                          = items.register("hammer_snail_shell", ()-> new Item(new Item.Settings().fireproof()));
    //endregion Items

    //region Blocks
    public static final RegistrySupplier<Block> CRYSTALLIZED_END_STONE_HIVE                 = blocks.register("crystallized_end_stone_hive", ()-> new EndBeeHive(AbstractBlock.Settings.create()));
    public static final RegistrySupplier<Block> CRYSTALLIZED_END_STONE_PUPA                 = blocks.register("crystallized_end_stone_pupa", ()-> new EndBeePupa(AbstractBlock.Settings.copy(Blocks.END_STONE).mapColor(MapColor.PINK)));
    public static final RegistrySupplier<Block> CHORUS_HONEY_BLOCK                          = blocks.register("chorus_honey_block", ()-> new Block(AbstractBlock.Settings.create().nonOpaque().strength(0.6f).slipperiness(0.8f).mapColor(MapColor.PINK)));
    public static final RegistrySupplier<Block> SMALL_CHORUS_FLOWER                         = blocks.register("small_chorus_flower", ()-> new SmallChorusFlower(StatusEffects.LEVITATION, 20, AbstractBlock.Settings.create().noCollision().offset(AbstractBlock.OffsetType.XZ).sounds(BlockSoundGroup.GRASS)));
    public static final RegistrySupplier<Block> POTTED_SMALL_CHORUS_FLOWER                  = blocks.register("potted_small_chorus_flower", ()-> new FlowerPotBlock(SMALL_CHORUS_FLOWER.get(), AbstractBlock.Settings.copy(Blocks.POTTED_ALLIUM).nonOpaque()));

    //region Mucus Block
    public static final RegistrySupplier<Block> GRAVITATIONAL_MUCUS                         = blocks.register("gravitational_mucus", () -> new GravitationalMucus(AbstractBlock.Settings.create().noCollision().mapColor(MapColor.PURPLE).jumpVelocityMultiplier(0).sounds(BlockSoundGroup.SLIME).dropsNothing()));
    public static final RegistrySupplier<Block> GRAVITATIONAL_MUCUS_BLOCK                   = blocks.register("gravitational_mucus_block", ()-> new GravitationalMucusBlock(AbstractBlock.Settings.create().noCollision().mapColor(MapColor.PURPLE).jumpVelocityMultiplier(0).sounds(BlockSoundGroup.SLIME)));
    public static final RegistrySupplier<Block> STRANGE_MUCUS_BLOCK                         = blocks.register("strange_mucus_block", ()-> new StrangeMucusBlock(0.08f, 1.2f, (byte) 0, AbstractBlock.Settings.create().noCollision().mapColor(MapColor.PINK).sounds(BlockSoundGroup.HONEY)));
    public static final RegistrySupplier<Block> POWERED_STRANGE_MUCUS_BLOCK                 = blocks.register("powered_strange_mucus_block", ()-> new StrangeMucusBlock(0.18f, 7f, (byte) 4, AbstractBlock.Settings.create().noCollision().mapColor(MapColor.PALE_PURPLE).sounds(BlockSoundGroup.HONEY)));
    public static final RegistrySupplier<Block> ULTRA_POWERED_STRANGE_MUCUS_BLOCK           = blocks.register("ultra_powered_strange_mucus_block", ()-> new StrangeMucusBlock(0.26f, 15f, (byte) 8, AbstractBlock.Settings.create().noCollision().mapColor(MapColor.PURPLE).sounds(BlockSoundGroup.HONEY)));
    public static final RegistrySupplier<Block> LIMITER_REMOVER                             = blocks.register("limiter_remover", ()-> new SpeedLimitRemover(AbstractBlock.Settings.create()));
    //endregion Mucus block


    //region Crystallized End Stone
    public static final RegistrySupplier<Block> CRYSTALLIZED_END_STONE                      = blocks.register("crystallized_end_stone", ()-> new Block(AbstractBlock.Settings.copy(Blocks.END_STONE).mapColor(MapColor.PINK)));
    public static final RegistrySupplier<Block> CRYSTALLIZED_END_STONE_BRICKS               = blocks.register("crystallized_end_stone_bricks", ()-> new Block(AbstractBlock.Settings.create().instrument(Instrument.BASEDRUM).requiresTool().strength(3f, 10f)));
    public static final RegistrySupplier<Block> CRYSTALLIZED_END_STONE_BRICKS_SLAB          = blocks.register("crystallized_end_stone_bricks_slab", ()-> new SlabBlock(AbstractBlock.Settings.copy(Blocks.PURPUR_SLAB)));
    public static final RegistrySupplier<Block> CRYSTALLIZED_END_STONE_BRICKS_WALL          = blocks.register("crystallized_end_stone_bricks_wall", ()-> new WallBlock(AbstractBlock.Settings.copy(Blocks.END_STONE_BRICK_WALL)));
    public static final RegistrySupplier<Block> CRYSTALLIZED_END_STONE_BRICKS_STAIRS        = blocks.register("crystallized_end_stone_bricks_stairs", ()-> new StairsBlock(Blocks.PURPUR_STAIRS.getDefaultState(), AbstractBlock.Settings.copy(Blocks.PURPUR_STAIRS)));
    //endregion Crystallized End Stone

    public static final RegistrySupplier<Block> OLD_END_ALLOY_BLOCK                         = blocks.register("old_end_alloy_block", ()-> new Block(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK).requiresTool()));
    public static final RegistrySupplier<Block> OLD_END_ORE                                 = blocks.register("old_end_ore", ()-> new Block(AbstractBlock.Settings.copy(Blocks.END_STONE).requiresTool()));
    //endregion Blocks

    //region BlockEntity
    public static final RegistrySupplier<BlockEntityType<EndBeeHiveEntity>> END_BEE_HIVE_BLOCK_ENTITY = blockEntity.register("end_bee_hive_block_entity", ()-> BlockEntityType.Builder.create(EndBeeHiveEntity::new, CRYSTALLIZED_END_STONE_HIVE.get()).build(null));

    //endregion BlockEntity


    //region Block Item
    public static final RegistrySupplier<Item> GRAVITATIONAL_MUCUS_BOTTLE                   = bottleBlock(GRAVITATIONAL_MUCUS,
            new Item.Settings().food(new FoodComponent.Builder().hunger(0).saturationModifier(0.2f).build()).recipeRemainder(GLASS_BOTTLE).maxCount(16));

    public static final RegistrySupplier<Item> CHORUS_HONEY_BLOCK_ITEM                      = blockItem(CHORUS_HONEY_BLOCK);
    public static final RegistrySupplier<Item> OLD_END_ALLOY_BLOCK_ITEM                     = blockItem(OLD_END_ALLOY_BLOCK);
    public static final RegistrySupplier<Item> OLD_END_ORE_ITEM                             = blockItem(OLD_END_ORE);

    public static final RegistrySupplier<Item> SMALL_CHORUS_FLOWER_ITEM                     = blockItem(SMALL_CHORUS_FLOWER);

    //region Mucus Item
    public static final RegistrySupplier<Item> GRAVITATIONAL_MUCUS_BLOCK_ITEM               = blockItem(GRAVITATIONAL_MUCUS_BLOCK);
    public static final RegistrySupplier<Item> STRANGE_MUCUS_ITEM                           = blockItem(STRANGE_MUCUS_BLOCK);
    public static final RegistrySupplier<Item> POWERED_STRANGE_MUCUS_ITEM                   = blockItem(POWERED_STRANGE_MUCUS_BLOCK);
    public static final RegistrySupplier<Item> ULTRA_POWERED_STRANGE_MUCUS_ITEM             = blockItem(ULTRA_POWERED_STRANGE_MUCUS_BLOCK);
    public static final RegistrySupplier<Item> LIMITER_REMOVER_ITEM                         = blockItem(LIMITER_REMOVER);
    //endregion Mucus Item

    //region Crystallized End Stone
    public static final RegistrySupplier<Item> CRYSTALLIZED_END_STONE_ITEM                  = blockItem(CRYSTALLIZED_END_STONE);
    public static final RegistrySupplier<Item> CRYSTALLIZED_END_STONE_BRICKS_ITEM           = blockItem(CRYSTALLIZED_END_STONE_BRICKS);
    public static final RegistrySupplier<Item> CRYSTALLIZED_END_STONE_BRICKS_SLAB_ITEM      = blockItem(CRYSTALLIZED_END_STONE_BRICKS_SLAB);
    public static final RegistrySupplier<Item> CRYSTALLIZED_END_STONE_BRICKS_WALL_ITEM      = blockItem(CRYSTALLIZED_END_STONE_BRICKS_WALL);
    public static final RegistrySupplier<Item> CRYSTALLIZED_END_STONE_BRICKS_STAIRS_ITEM    = blockItem(CRYSTALLIZED_END_STONE_BRICKS_STAIRS);
    //endregion Crystallized End Stone

    public static final RegistrySupplier<Item> ENDBEE_HIVE_ITEM                             = blockItem(CRYSTALLIZED_END_STONE_HIVE);
    public static final RegistrySupplier<Item> CRYSTALLIZED_END_STONE_PUPA_ITEM             = blockItem(CRYSTALLIZED_END_STONE_PUPA);

    //endregion Block Item
}
