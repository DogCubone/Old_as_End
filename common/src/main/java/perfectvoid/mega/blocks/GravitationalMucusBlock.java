package perfectvoid.mega.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GravitationalMucusBlock extends Block {
    public GravitationalMucusBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.addVelocity(0, -0.8, 0);
        entity.slowMovement(state, new Vec3d(0.9, 1.2, 0.9));
    }
}
