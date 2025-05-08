package perfectvoid.mega.items;

import net.minecraft.entity.Bucketable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class MusselEntityBucket extends Item {
    private final EntityType<?> entityType;
    private final SoundEvent emptyingSound;

    public MusselEntityBucket(EntityType<?> type, SoundEvent emptyingSound, Settings settings) {
        super(settings);
        this.entityType = type;
        this.emptyingSound = emptyingSound;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        if (world instanceof ServerWorld serverWorld) {
            this.spawnEntity(serverWorld, context.getStack(), pos);
            serverWorld.emitGameEvent(player, GameEvent.ENTITY_PLACE, pos);

            assert player != null;
            if (!player.getAbilities().creativeMode) {
                context.getStack().decrement(1);
                player.getInventory().insertStack(new ItemStack(Items.BUCKET));
                player.incrementStat(Stats.USED.getOrCreateStat(this));
            }
        }
        context.getWorld().playSound(player, pos, this.emptyingSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        return ActionResult.success(world.isClient);
    }


    private void spawnEntity(ServerWorld world, ItemStack stack, BlockPos pos) {
        Entity entity = this.entityType.spawnFromItemStack(world, stack, null, pos, SpawnReason.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            bucketable.copyDataFromNbt(stack.getOrCreateNbt());
            bucketable.setFromBucket(true);
        }

    }
}
