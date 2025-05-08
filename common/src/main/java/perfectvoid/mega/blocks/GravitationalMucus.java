package perfectvoid.mega.blocks;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;
import perfectvoid.mega.entities.ShellHammerSnail;

import java.util.Objects;


public class GravitationalMucus extends FallingBlock implements Waterloggable {
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public GravitationalMucus(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false));
    }


    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity){
        if (entity instanceof ShulkerBulletEntity || entity instanceof ItemEntity) return;
        //        entity.setVelocity(0, -1.2, 0);
        entity.addVelocity(0, -1.2, 0);
        if(!(entity instanceof ShellHammerSnail)) entity.slowMovement(state, new Vec3d(0.7, 1, 0.7));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FallingBlock.createCuboidShape(0, 0, 0, 16, 0.1, 16);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.isAir(pos) && world.getBlockState(pos.down()) != BlocksAndItemsRegister.GRAVITATIONAL_MUCUS.get().getDefaultState()
                && world.getBlockState(pos.down()).isSolidBlock(world, pos.down());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        boolean isClient = world.isClient;

        //Makes sure the code will only run on the server side.
        if (!isClient) {
            ItemStack itemStackInHand = player.getStackInHand(hand);

            if(itemStackInHand.getItem() == Items.GLASS_BOTTLE) {
                world.removeBlock(pos, false); //Removes the block from the world.

                //Avoids Removing the glass bottle if in creative mode.
                if (!player.getAbilities().creativeMode)
                   itemStackInHand.decrement(1);

                //Adds the mucus bottle to the player Inventory and also gives a "Used" stats
                if(!player.giveItemStack(new ItemStack(this)))
                    player.dropItem(new ItemStack(this), false);

                player.incrementStat(Stats.USED.getOrCreateStat(itemStackInHand.getItem()));
                player.incrementStat(Stats.PICKED_UP.getOrCreateStat(this.getDefaultState().getBlock().asItem()));
            }
        }

        return ActionResult.success(isClient);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : state.getFluidState();
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return Objects.requireNonNull(super.getPlacementState(ctx)).with(WATERLOGGED,ctx.getWorld().getBlockState(ctx.getBlockPos()).getFluidState().getFluid() == Fluids.WATER);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }
}
