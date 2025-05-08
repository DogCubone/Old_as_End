package perfectvoid.mega.blocks.blockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;
import perfectvoid.mega.entities.EndBee;

import java.util.*;

public class EndBeeHiveEntity extends BlockEntity {
    public byte beesInHive = 0;
    public static final byte maxBeeAmount = 3;
    public static final int minimumTicksToLeave = 600;
    public static final int maximumExtraTicksToLeave = 600;

    public final List<NbtCompound> storedBees = new ArrayList<>();
    public final int[] ticksUntilBeeLeaves = new int[maxBeeAmount];

    public EndBeeHiveEntity(BlockPos pos, BlockState state) {
        super(BlocksAndItemsRegister.END_BEE_HIVE_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, EndBeeHiveEntity hiveEntity){
        if (hiveEntity.beesInHive > 0){
            for (byte i = 0; i < hiveEntity.beesInHive; i++){
                if (hiveEntity.ticksUntilBeeLeaves[i] <= 0)
                    hiveEntity.leaveHive(world, i);
                else hiveEntity.ticksUntilBeeLeaves[i] -= 1;
                System.out.println(hiveEntity.ticksUntilBeeLeaves[i]);
            }
        }
    }

    //region Util
    public boolean isHiveFull(){
        return this.beesInHive == maxBeeAmount;
    }
    //endregion Util

    private void leaveHive(World world, byte index) {
        NbtCompound beeData = storedBees.get(index);
        storedBees.remove(index);
        Entity beeEntity = EntityType.loadEntityWithPassengers(beeData, world, (entityp) -> entityp);

        world.spawnEntity(beeEntity);
    }

    private void enterHive(EndBee endBee, boolean hasNectar){
        if (this.beesInHive < maxBeeAmount) {

            NbtCompound beeNBT = new NbtCompound();
            endBee.saveNbt(beeNBT);
            beeNBT.putBoolean("hasPollen", false);
            endBee.remove(Entity.RemovalReason.DISCARDED);

            if (storedBees.get(this.beesInHive).isEmpty()) {
                storedBees.add(this.beesInHive, beeNBT);
                ticksUntilBeeLeaves[this.beesInHive ] = (hasNectar) ? minimumTicksToLeave + endBee.getWorld().random.nextInt(maximumExtraTicksToLeave) : minimumTicksToLeave;
            }

            ++this.beesInHive;
        }
    }
}
