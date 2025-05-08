package perfectvoid.mega.blocks;

import net.minecraft.block.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import perfectvoid.mega.others.OldEndTags;

public class SmallChorusFlower extends FlowerBlock implements Fertilizable {
    private static final int SPREAD_CHANCE = 10;
    private static final int GROW_CHANCE = 4;
    private static final int MAX_AGE = 5;
    public static final IntProperty AGE = Properties.AGE_5;

    protected static final Direction[] zAxis = {Direction.NORTH, Direction.SOUTH};
    protected static final Direction[] xAxis = {Direction.WEST, Direction.EAST};

    public SmallChorusFlower(StatusEffect suspiciousStewEffect, int effectDuration, Settings settings) {
        super(suspiciousStewEffect, effectDuration,settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int blockAge = state.get(AGE);

        if (canGrowIntoFruit(blockAge, world, pos))
            this.grow(world, random, pos, state);

        if (blockAge < MAX_AGE && world.random.nextInt(8) == 0){
            world.setBlockState(pos, state.with(AGE, blockAge + 1), 2);
            this.spread(world, pos, random);
        }
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        boolean isClient = world.isClient;

        if (!isClient) {
            int blockAGE = state.get(AGE);
            ItemStack itemInHand = player.getStackInHand(hand);

            if (canGrowIntoFruit(blockAGE, world, pos))
                this.grow((ServerWorld) world, world.getRandom(), pos, state);

            if (itemInHand.isOf(Items.BONE_MEAL) && blockAGE < MAX_AGE) {
                //Avoids Removing the item if in creative mode.
                if (!player.getAbilities().creativeMode) itemInHand.decrement(1);

                if (world.random.nextInt(3) == 0) world.setBlockState(pos, state.with(AGE, blockAGE + 1), 2);
            }
        }

        return ActionResult.success(isClient);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(OldEndTags.EndStoneBlocks) || floor.isIn(BlockTags.DIRT);
    }

    private boolean canGrowIntoFruit(int age, World world, BlockPos pos){
        return age == MAX_AGE && world.random.nextInt(GROW_CHANCE) == 0 && world.getBlockState(pos.down()).isIn(OldEndTags.EndStoneBlocks)
                && world.isAir(pos.up()) && scanForObstruction(world, pos);
    }

    private void spread(World world, BlockPos pos, Random random) {

        //I tried using a random on the "Direction.Type.HORIZONTAL", but it would only get one of the 2 axis... so, I decided to make it myself :3
        BlockPos.Mutable spreadPos = new BlockPos.Mutable(
                pos.getX() + Util.getRandom(xAxis, random).getOffsetX() * world.random.nextBetween(0, 5),
                0,
                pos.getZ() + Util.getRandom(zAxis, random).getOffsetZ() * world.random.nextBetween(0, 5)
        );

        if (spreadPos.getZ() == pos.getZ() && spreadPos.getX() == pos.getX()) return;

        if (world.random.nextInt(SPREAD_CHANCE) == 0 && world.getBlockState(pos.down()).isIn(OldEndTags.EndStoneBlocks)){
            for (byte i = -1; i <= 1; i++) {
                spreadPos.setY(pos.getY() + i);

                if (world.isAir(spreadPos) && this.canPlantOnTop(world.getBlockState(spreadPos.down()), world, spreadPos)) {
                    world.setBlockState(spreadPos, this.getDefaultState());
                    break;
                }
            }
        }
    }

    private boolean scanForObstruction(World world, BlockPos pos){
        BlockPos.Mutable mutablePos = new BlockPos.Mutable(0, pos.getY(), 0);

        //iterates through the directions && returns false if there is an obstruction (non-air block)
        for (int x = -3; x < 3; x++) {
            mutablePos.setX(pos.getX() + x);

            for (int z = -3; z < 3; z++) {
                if (x == 0 && z == 0) continue;
                mutablePos.setZ(pos.getZ() + z);

                if (!world.isAir(mutablePos))
                    return false;
            }
        }
        return true;
    }


    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos, Blocks.CHORUS_FLOWER.getDefaultState());
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

}
