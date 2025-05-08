package perfectvoid.mega.entities;

import net.minecraft.client.render.DimensionEffects;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;
import perfectvoid.mega.Registers.PoiRegister;
import perfectvoid.mega.blocks.blockEntity.EndBeeHiveEntity;

import java.util.List;

public class EndBeeA extends AnimalEntity implements Flutterer {
    public static final float beeSpeed = 1.2f;
    BlockPos hivePos = null;
    BlockPos flowerPos = null;
    boolean hasPollen = false;

    public int tickToCreateNewHive = 0;
    public int ticksToFindNewHive = 0;
    public int ticksToForgetHive = 0;


    public final AnimationState idleState = new AnimationState();
    private byte idleTimeout = 0;

    public EndBeeA(EntityType<? extends EndBeeA> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createEndBeeAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(3, new FlyGoal(this, 0.2));
        this.goalSelector.add(1, new MoveToHive(this, 1.2f));
        this.goalSelector.add(2, new findEndHiveGoal());
    }

    @Override
    public boolean isInAir() {
        return !this.isOnGround();
    }

    //region Custom NBT data
    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("hivePos"))
            this.hivePos = NbtHelper.toBlockPos(nbt.getCompound("hivePos"));

        if (nbt.contains("flowerPos"))
            this.flowerPos = NbtHelper.toBlockPos(nbt.getCompound("flowerPos"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("hivePos", NbtHelper.fromBlockPos(this.hivePos));
        nbt.put("flowerPos", NbtHelper.fromBlockPos(this.flowerPos));

        nbt.putBoolean("hasPollen", this.hasPollen);
    }
    //endregion Custom NBT data

    //region Util

    private boolean isValidHive(BlockPos pos){
        if (this.getWorld().getBlockEntity(pos) instanceof EndBeeHiveEntity hive)
            return !hive.isHiveFull();
        else return false;
    }

    //endregion Util

    //region breeding
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(BlocksAndItemsRegister.SMALL_CHORUS_FLOWER_ITEM.get());
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        super.breed(world, other);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
    //endregion breeding

    private class findEndHiveGoal extends Goal {

        public boolean canStart(){
            return EndBeeA.this.hivePos == null && EndBeeA.this.ticksToFindNewHive == 0;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        public void start(){
            List<BlockPos> hivesClose = this.hivesInRange();

            if (!hivesClose.isEmpty()){
                EndBeeA.this.hivePos = hivesClose.get(0);
                hivesClose.clear();
            }
        }

        private List<BlockPos> hivesInRange(){
            BlockPos beePos = EndBeeA.this.getBlockPos();
            PointOfInterestStorage PoIS = ((ServerWorld) EndBeeA.this.getWorld()).getPointOfInterestStorage();
            return PoIS.getInSquare(poiType -> poiType.value() == PoiRegister.END_HIVE.get(),
                    beePos, 30, PointOfInterestStorage.OccupationStatus.ANY).map(PointOfInterest::getPos).filter(EndBeeA.this::isValidHive).toList();
        }
    }

    private class MoveToHive extends Goal {
        protected EndBeeA beeEntity;
        public MoveToHive(EndBeeA beeEntity, float speed){

        }

        @Override
        public boolean canStart() {
            return EndBeeA.this.hivePos != null && !EndBeeA.this.hivePos.isWithinDistance(EndBeeA.this.getPos(), 2);
        }

        @Override
        public boolean shouldContinue() {
            return this.canStart();
        }

        @Override
        public void start() {
            BlockPos hivePos = EndBeeA.this.hivePos;
            EndBeeA.this.getNavigation().startMovingTo(hivePos.getX(), hivePos.getY(), hivePos.getZ(), beeSpeed);
        }

        @Override
        public void stop() {
            EndBeeA.this.getNavigation().stop();
        }
    }



}
