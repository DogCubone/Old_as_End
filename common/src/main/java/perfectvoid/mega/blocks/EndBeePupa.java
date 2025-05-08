package perfectvoid.mega.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;

public class EndBeePupa extends Block {
    private static final IntProperty beeEggs = IntProperty.of("eggs", 1, 3);

    public EndBeePupa(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(beeEggs, 1));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    public void addEgg(BlockPos pos, BlockState state, World world){
        if (state.get(beeEggs) < 3) {
            world.setBlockState(pos, state.with(beeEggs, state.get(beeEggs) + 1), 3);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.random.nextInt(8) == 0){
            for (Direction direction : DIRECTIONS) {
                //Checks if there is a place to spawn the Bee.
                if (world.isAir(pos.offset(direction))) {
                    PassiveEntity endBeeEntity = EntityType.BEE.create(world);

                    //WHAT DO YOU MEAN IT MIGHT PRODUCE A 'NullPointerException'? WHAT THE HECK? DIDN'T I JUST SET IT UP IN THERE?
                    //WHY MOJANG, WHYYYYYYY
                    if (endBeeEntity != null) {
                        endBeeEntity.setBaby(true);
                        endBeeEntity.setPosition(pos.offset(direction).toCenterPos());

                        world.spawnEntity(endBeeEntity);
                    }

                    removeEggOrSelf(state, pos, world); //Removes an egg from the pupa. Removes itself if there's no more eggs to be removed
                    break;
                }
            }
        }
    }

    private static void removeEggOrSelf(BlockState state, BlockPos pos, World world){
        if (state.get(beeEggs) == 1) // If there is only one egg, the block will be changed to its normal version.
            world.setBlockState(pos, BlocksAndItemsRegister.CRYSTALLIZED_END_STONE.get().getDefaultState(), 3);
        else //Removes the egg from the block.
            world.setBlockState(pos, state.with(beeEggs, state.get(beeEggs) - 1), 3);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            ItemStack pupaItem = new ItemStack(this);

            NbtCompound blockData = new NbtCompound();
            blockData.putInt("eggs", state.get(beeEggs));

            pupaItem.setSubNbt("BlockStateTag", blockData);
            ItemEntity pupaItemDrop = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), pupaItem);
            pupaItemDrop.setToDefaultPickupDelay();
            world.spawnEntity(pupaItemDrop);
        }
        super.onBreak(world, pos, state, player); //I believe that by letting this here, it still calls the default one... so I'll let it stay.
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(beeEggs);
    }
}
