package perfectvoid.mega.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;
import perfectvoid.mega.blocks.blockEntity.EndBeeHiveEntity;

public class EndBeeHive extends BlockWithEntity implements BlockEntityProvider {
    private static final IntProperty honeyAmount = IntProperty.of("honey_amount", 0, 6);
    public static final byte maxHoney = 6;

    public EndBeeHive(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(honeyAmount, 0));
    }


    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EndBeeHiveEntity(pos, state);
    }


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlocksAndItemsRegister.END_BEE_HIVE_BLOCK_ENTITY.get(), EndBeeHiveEntity::tick);
    }

    private void expand(ServerWorld world, BlockPos pos, BlockState state){
        if (world.random.nextInt(5) == 0 && state.get(honeyAmount) > 4){
            return;
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        boolean isClient = world.isClient;

        //Makes sure the code will only run on the server side.
        if (!isClient) {
            ItemStack itemStackInHand = player.getStackInHand(hand);

            if(itemStackInHand.getItem() == Items.GLASS_BOTTLE && state.get(honeyAmount) == 3) {

                //Avoids Removing the glass bottle if in creative mode.
                if (!player.getAbilities().creativeMode)
                    itemStackInHand.decrement(1);

                //Adds the mucus bottle to the player Inventory and also gives a "Used" stats
                world.setBlockState(pos, state.with(honeyAmount, 0), 3);
                if(!player.giveItemStack(new ItemStack(BlocksAndItemsRegister.CHORUS_HONEY_BOTTLE.get(), 1)))
                    player.dropItem(new ItemStack(BlocksAndItemsRegister.CHORUS_HONEY_BOTTLE.get()), false);

                player.incrementStat(Stats.USED.getOrCreateStat(itemStackInHand.getItem()));
                player.incrementStat(Stats.PICKED_UP.getOrCreateStat(this.getDefaultState().getBlock().asItem()));
            }
        }

        return ActionResult.success(isClient);
    }


    //I feel sooo bad knowing this shit is pretty much a 1:1 copy from the original hive
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)){
            NbtCompound nbt = new NbtCompound();
            ItemStack hiveItem = new ItemStack(this);

            if (world.getBlockEntity(pos) instanceof EndBeeHiveEntity endHiveEntity && endHiveEntity.beesInHive > 0){
                NbtList beeList = new NbtList();

                beeList.addAll(endHiveEntity.storedBees);

                nbt.put("storedBees", beeList);
                nbt.putIntArray("ticksToLeave", endHiveEntity.ticksUntilBeeLeaves);
                BlockItem.setBlockEntityNbt(hiveItem, BlocksAndItemsRegister.END_BEE_HIVE_BLOCK_ENTITY.get(), nbt);
            }
            nbt = new NbtCompound();

            nbt.putInt("honeyAmount", state.get(honeyAmount));

            hiveItem.setSubNbt("BlockStateTag", nbt);
            ItemEntity hiveItemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), hiveItem);
            hiveItemEntity.setToDefaultPickupDelay();
            world.spawnEntity(hiveItemEntity);
        }
            super.onBreak(world, pos, state, player);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(honeyAmount);
    }
}
