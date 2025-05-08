package perfectvoid.mega.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;
import perfectvoid.mega.others.TemporaryFixBucketableEntity;

public class EndMussel extends PassiveEntity implements TemporaryFixBucketableEntity {

    private static final TrackedData<Boolean> isFromBucket =  DataTracker.registerData(EndMussel.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState idleState = new AnimationState();
    private int idleTimeout = 0;

    public EndMussel(EntityType<? extends EndMussel> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_OTHER, 1.0f);
        this.setStepHeight(1f);
    }

    public static DefaultAttributeContainer.Builder createEndMusselAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 3);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 0.12));
        this.goalSelector.add(2, new WanderAroundGoal(this, 0.1));
        this.goalSelector.add(2, new FleeEntityGoal<>(this, ShellHammerSnail.class, 4f, 0.1, 0.11));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.1));
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(isFromBucket, false);
    }

    protected void tickAnimation(){
        if (this.idleTimeout <= 0){
            this.idleTimeout = 40;
            this.idleState.start(this.age);
        } else --this.idleTimeout;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) tickAnimation();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        NbtCompound customNBT = new NbtCompound();
        customNBT.putBoolean("isFromBucket", this.isFromBucket());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setFromBucket(nbt.getBoolean("isFromBucket"));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return TemporaryFixBucketableEntity.tryBucket(player, hand, this).orElse(super.interactMob(player, hand));
    }

    @Override
    public int getXpToDrop() {
        return random.nextBetween(1, 3);
    }

    @Override
    public boolean isFromBucket() {
        return this.dataTracker.get(isFromBucket);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
       this.dataTracker.set(isFromBucket, fromBucket);
    }

    @Override
    public void copyDataToStack(ItemStack stack) {
        Bucketable.copyDataToStack(this, stack);
    }

    @Override
    public void copyDataFromNbt(NbtCompound nbt) {
        Bucketable.copyDataFromNbt(this, nbt);
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(BlocksAndItemsRegister.END_MUSSEL_BUCKET.get());
    }

    @Override
    public SoundEvent getBucketFillSound() {
        return SoundEvents.ITEM_BUCKET_FILL;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SHULKER_HURT;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHULKER_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHULKER_DEATH;
    }
}
