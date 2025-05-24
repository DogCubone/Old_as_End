package perfectvoid.mega.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;
import perfectvoid.mega.blocks.blockEntity.EndBeeHiveEntity;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EndBee extends AnimalEntity implements Flutterer {
    public static final float beeSpeed = 1.2f;
    private BlockPos hivePos = null;
    private BlockPos flowerPos = null;
    private boolean hasPollen = false;


    public int ticksToCreateNewHive = 0;
    private short ticksUntilSearchForHive = 0;
    private short tickUntilSearchForNewFlower = 0;

    private static final TrackedData<Byte> EndBeeState = DataTracker.registerData(EndBee.class, TrackedDataHandlerRegistry.BYTE);

    public final AnimationState idleState = new AnimationState();
    private byte idleTimeout = 0;



    public EndBee(EntityType<? extends EndBee> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.limitFallDistance();
    }

    public static DefaultAttributeContainer.Builder createEndBeeAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new PollinateChorus());
        this.goalSelector.add(1, new FlyToHiveGoal(0.9f));
        this.goalSelector.add(1, new findFlowerGoal((short)6));
        this.goalSelector.add(2, new findEndHiveGoal());
        this.goalSelector.add(2, new FlyToFlowerGoal(0.9f));
//        this.goalSelector.add(3, new FlyGoal(this, 0.9f));
    }

    @Override
    protected void initDataTracker() {
        /* Note to self:
        0: DEFAULT
        1: MOVING TO/POLLINATING FLOWER
        2: LAYING LARVAE/EGG
        3: MAKING HIVE
         */
        this.dataTracker.startTracking(EndBeeState, (byte) 0);
        super.initDataTracker();
    }

    private void resetState(){
        this.dataTracker.set(EndBeeState, (byte) 0);
    }

    //region Custom NBT data
    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("hivePos"))
            this.hivePos = NbtHelper.toBlockPos(nbt.getCompound("hivePos"));

        if (nbt.contains("flowerPos"))
            this.flowerPos = NbtHelper.toBlockPos(nbt.getCompound("flowerPos"));

        super.readNbt(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.hivePos != null)   nbt.put("hivePos", NbtHelper.fromBlockPos(this.hivePos));
        if (this.flowerPos != null) nbt.put("flowerPos", NbtHelper.fromBlockPos(this.flowerPos));

        nbt.putBoolean("hasPollen", this.hasPollen);

        super.writeCustomDataToNbt(nbt);
    }
    //endregion Custom NBT data

    //region Util
    private boolean isValidHive(BlockPos pos){
        if (this.getWorld().getBlockEntity(pos) instanceof EndBeeHiveEntity hive)
            return !hive.isHiveFull();
        else return false;
    }

    int sla = 0;

    @Override
    public void tick() {
        if (this.getWorld().isClient) {
            if (this.idleTimeout <= 0) {
                this.idleTimeout = 24;
                this.idleState.start(this.age);
            } else --this.idleTimeout;
        } else {
            if (this.hivePos == null) --this.ticksUntilSearchForHive;
            if (this.flowerPos == null) ++this.tickUntilSearchForNewFlower;
        }


        if (!this.getWorld().isClient && this.sla <= 0){
            this.sla = 30;
            System.out.println("Ticks até procurar nova colmeia: " + this.ticksUntilSearchForHive);
            System.out.println(this.hivePos);
            System.out.println("Ticks até procurar nova flor: " + this.tickUntilSearchForNewFlower);
            System.out.println("Flor em: " + this.flowerPos);
            System.out.println("Has pollen: "+ this.hasPollen);
        } else --this.sla;

        super.tick();
    }
    //endregion Util

    @Override
    public boolean isInAir() {
        return !this.isOnGround();
    }

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

    private class findFlowerGoal extends Goal{
        private final short searchDistance;

        public findFlowerGoal(short searchDistance) {
            this.searchDistance = searchDistance;
        }

        @Override
        public boolean canStart() {
            return EndBee.this.tickUntilSearchForNewFlower >= 250;
        }

        @Override
        public void stop() {
            EndBee.this.tickUntilSearchForNewFlower = 0;
        }

        private final Predicate<BlockState> isValidFlower = (state) ->
                state.isOf(BlocksAndItemsRegister.SMALL_CHORUS_FLOWER.get()) || state.isOf(Blocks.CHORUS_FLOWER);

        @Override
        public void start() {
            EndBee.this.flowerPos = getValidFlower();
        }

        protected BlockPos getValidFlower(){
            BlockPos beePos = EndBee.this.getBlockPos();
            BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            World world = EndBee.this.getWorld();

            for (int y = 0; y <= searchDistance; y++) {
                for (int x = 0; x <= searchDistance; x++) {
                    for (int z = 0; z <= searchDistance; z++) {
                        mutablePos.set(beePos, x, y, z);

                        if (isValidFlower.test(world.getBlockState(mutablePos))) {
                            return mutablePos.toImmutable();
                        }
                    }
                }
            }
            return null;
        }
    }

    private class findEndHiveGoal extends Goal {

        public boolean canStart(){
            return EndBee.this.hivePos == null && EndBee.this.ticksUntilSearchForHive <= 0;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        public void start(){
//            EndBee.this.ticksUntilSearchForHive = (short)random.nextBetween(80, 100);
            List<BlockPos> hivesClose = this.hivesInRange();

            if (!hivesClose.isEmpty()){
                EndBee.this.hivePos = hivesClose.get(0);
                hivesClose.clear();
            }
        }


        private List<BlockPos> hivesInRange(){
            BlockPos beePos = EndBee.this.getBlockPos();
            PointOfInterestStorage PoIS = ((ServerWorld) EndBee.this.getWorld()).getPointOfInterestStorage();
            return PoIS.getInSquare(poiType -> poiType.isIn(PointOfInterestTypeTags.BEE_HOME),
                    beePos, 30, PointOfInterestStorage.OccupationStatus.ANY).map(PointOfInterest::getPos).collect(Collectors.toList());
        }
    }

    private class FlyToHiveGoal extends Goal {
        private final float speed;
        private int ticksToForgetHive;

        public FlyToHiveGoal(float speed) {
            this.speed = speed;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return EndBee.this.hivePos != null && EndBee.this.dataTracker.get(EndBeeState) == 0;
        }

        @Override
        public boolean shouldContinue() {
            return this.canStart();
        }

        @Override
        public void start() {
            ticksToForgetHive = 0;
        }

        @Override
        public void stop() {
            EndBee.this.navigation.stop();
        }

        @Override
        public void tick() {
            if (EndBee.this.hivePos == null) return;

            Vec3d targetVec = Vec3d.ofCenter(EndBee.this.hivePos);
            EndBee.this.getMoveControl().moveTo(targetVec.x, targetVec.y, targetVec.z, speed);

            if (!EndBee.this.getNavigation().isFollowingPath())
                ticksToForgetHive++;
            else
                ticksToForgetHive = 0;

            if (ticksToForgetHive > 60) {
                EndBee.this.hivePos = null;
                EndBee.this.ticksUntilSearchForHive = (short)random.nextBetween(80, 100);
                this.stop();
            }
        }
    }

    private class FlyToFlowerGoal extends Goal {
        private final float speed;
        private short ticksToForgetFlower;

        public FlyToFlowerGoal(float speed) {
            this.speed = speed;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return EndBee.this.flowerPos != null;
        }

        @Override
        public boolean shouldContinue() {
            return this.canStart();
        }

        @Override
        public void start() {
            ticksToForgetFlower = 0;
        }

        @Override
        public void stop() {
            EndBee.this.navigation.stop();
            EndBee.this.resetState();
        }

        @Override
        public void tick() {
            if (EndBee.this.flowerPos == null) return;

            Vec3d targetVec = Vec3d.ofCenter(EndBee.this.flowerPos);
            EndBee.this.getMoveControl().moveTo(targetVec.x, targetVec.y, targetVec.z, speed);

            if (!EndBee.this.getNavigation().isFollowingPath()) {
                ticksToForgetFlower++;
            } else {
                ticksToForgetFlower = 0;
            }

            if (ticksToForgetFlower > 600) {
                EndBee.this.flowerPos = null;
                this.stop();
            }
        }
    }

    private class PollinateChorus extends Goal {
        protected int ticks = 0;


        @Override
        public boolean canStart() {
            return EndBee.this.flowerPos != null && !EndBee.this.hasPollen && EndBee.this.flowerPos.isWithinDistance(EndBee.this.getPos(), 1);
        }

        @Override
        public void start() {
            EndBee.this.dataTracker.set(EndBeeState, (byte) 1);
            EndBee.this.getLookControl().lookAt(EndBee.this.flowerPos.toCenterPos());
        }

        @Override
        public boolean shouldContinue() {
            return EndBee.this.flowerPos != null;
        }

        @Override
        public void stop() {
            EndBee.this.navigation.stop();
            EndBee.this.resetState();
        }


        @Override
        public void tick() {
            if (ticks >= 350){
                EndBee.this.hasPollen = true;
                EndBee.this.flowerPos = null;
                this.stop();
            }
            else ++ticks;
            if (!EndBee.this.flowerPos.isWithinDistance(EndBee.this.getPos(), 1)){
                EndBee.this.getMoveControl().moveTo(EndBee.this.flowerPos.getX(), EndBee.this.flowerPos.getY(), EndBee.this.flowerPos.getZ(), 0.9f);
            }
            super.tick();
        }
    }
}
