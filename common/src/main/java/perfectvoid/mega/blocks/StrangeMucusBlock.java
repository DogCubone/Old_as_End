package perfectvoid.mega.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;

public class StrangeMucusBlock extends Block {
    private final float velocityMultiplier;
    private final float velocityLimit;
    private final byte originalParticleAmount;
    private short particleAmount;
    public static final BooleanProperty isLimited = BooleanProperty.of("is_limited");

    public StrangeMucusBlock(Float speedPower, float maximumVelocity, byte particleAmount, Settings settings) {
        super(settings);
        this.velocityMultiplier = speedPower;
        this.velocityLimit = maximumVelocity;
        this.originalParticleAmount = particleAmount;
        this.particleAmount = particleAmount;
        this.setDefaultState(this.stateManager.getDefaultState().with(isLimited, true));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            BlockState neighborState = world.getBlockState(sourcePos);

            //Makes so the mucus have a chain reaction once a limit remover is placed by its side.
            if (isMucusBlock(neighborState.getBlock()) && !neighborState.get(isLimited)) {
                world.setBlockState(pos, state.with(isLimited, false), 3);
              } else if (state != this.getDefaultState()) //tries to reset the block limit.
                  world.scheduleBlockTick(pos, this, 3);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(isLimited, true));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(isLimited)) this.particleAmount = (short)((this.originalParticleAmount + 1) << 1);
        else this.particleAmount = this.originalParticleAmount;

        //Spawns particle if the block is eligible for it and if the random allows to do so.
        if (this.particleAmount > 0 && random.nextInt(4) == 0) {

            //Avoids spawning particles in places you can't see, this adds more stuff for the CPU to process, but I hope it pays of with less load on the GPU
            //I'm only doing this since it is a technically a transparent block, so minecraft will try to render the particles of each side. I don't know how I could optimize this more :(
            for (Direction direction : DIRECTIONS) {
                if ((world.isAir(pos.offset(direction)) || world.getBlockState(pos.offset(direction)).isReplaceable()))
                    for (short i = 0; i < particleAmount; i++) spawnAtDirection(world, pos, direction, random);
            }
        }
    }

    //simple code (that repeats a lot) that spawns particles according to the "allowed" direction
    protected static void spawnAtDirection(World world, BlockPos pos, Direction direction, Random random) {

        switch (direction) {
            case UP -> world.addParticle(new DustParticleEffect(DustParticleEffect.RED, 0.5f), true,
                    pos.getX() + random.nextFloat() - 0.02f, pos.getY() + 1, pos.getZ() + random.nextFloat() - 0.02f,
                    0, 0, 0);
            case DOWN -> world.addParticle(new DustParticleEffect(DustParticleEffect.RED, 0.5f), true,
                    pos.getX() + random.nextFloat() - 0.02f, pos.getY() - 0.01f, pos.getZ() + random.nextFloat() - 0.02f,
                    0, 0, 0);
            case SOUTH -> world.addParticle(new DustParticleEffect(DustParticleEffect.RED, 0.5f), true,
                    pos.getX() + random.nextFloat() - 0.02f, pos.getY() + random.nextFloat() - 0.02f, pos.getZ() + 1,
                    0, 0, 0);
            case NORTH -> world.addParticle(new DustParticleEffect(DustParticleEffect.RED, 0.5f), true,
                    pos.getX() + random.nextFloat() - 0.02f, pos.getY() + random.nextFloat() - 0.02f, pos.getZ() - 0.01f,
                    0, 0, 0);
            case EAST -> world.addParticle(new DustParticleEffect(DustParticleEffect.RED, 0.5f), true,
                    pos.getX() + 1, pos.getY() + random.nextFloat() - 0.02f, pos.getZ() + random.nextFloat() - 0.02f,
                    0, 0, 0);
            case WEST -> world.addParticle(new DustParticleEffect(DustParticleEffect.RED, 0.5f), true,
                    pos.getX() - 0.01f, pos.getY() + random.nextFloat() - 0.02f, pos.getZ() + random.nextFloat() - 0.02f,
                    0, 0, 0);
        }
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        if (!world.isClient) {
            BlockPos pos = ctx.getBlockPos();

            BlockState blockState;
            for (Direction direction : DIRECTIONS) {
                blockState = world.getBlockState(pos.offset(direction));
                if (isMucusBlock(blockState.getBlock()) && !blockState.get(isLimited))
                    return this.getDefaultState().with(isLimited, false);
            }
        }
        return this.getDefaultState();
    }

    protected static boolean isMucusBlock(Block block) {
        return block instanceof StrangeMucusBlock || block == BlocksAndItemsRegister.LIMITER_REMOVER.get();
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {

        double velocityX = Math.abs(entity.getVelocity().x);
        double velocityZ = Math.abs(entity.getVelocity().z);
        double velocityY = Math.abs(entity.getVelocity().y);

        boolean isFasterThanLimit = state.get(isLimited) && (velocityX > velocityLimit || velocityZ > velocityLimit || velocityY > velocityLimit);

        if (!isFasterThanLimit && !entity.isInSneakingPose()) {
            entity.setVelocity(speedPly(velocityX, velocityZ, velocityY, entity.getVelocity(), velocityMultiplier + 1));
//            entity.setVelocity(speedUP(velocityX, velocityZ, velocityY, entity.getVelocity(), velocityMultiplier));
        }
    }

    //Trust me, it's way harder to make stuff go straight without this.
    protected static Vec3d speedPly(double velX, double velZ, double velY, Vec3d baseVelocity, float velocityMulti) {
        if (velZ > 0.02)
            velZ *= velocityMulti;
        if (velX > 0.02)
            velX *= velocityMulti;
        if (velY > 0.02) {
            velY += 0.05;
            velY *= velocityMulti;
        }

        return new Vec3d(velX * Math.signum(baseVelocity.x), velY * Math.signum(baseVelocity.y), velZ * Math.signum(baseVelocity.z));
    }

    protected static Vec3d speedUP(double velX, double velZ, double velY, Vec3d baseVelocity, float velocityMulti) {

        if (velZ > 0.02)
            velZ += velocityMulti;
        if (velX > 0.02)
            velX += velocityMulti;
        if (velY > 0.02)
            velY += velocityMulti / 2;

        return new Vec3d(velX * Math.signum(baseVelocity.x), velY * Math.signum(baseVelocity.y), velZ * Math.signum(baseVelocity.z));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(isLimited);
    }
}
