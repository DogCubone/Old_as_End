package perfectvoid.mega.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import perfectvoid.mega.Registers.ExtraRegister;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;
import perfectvoid.mega.Registers.SoundRegister;


public class ShellHammerSnail extends PassiveEntity {
    public final AnimationState idleState = new AnimationState();
    public final AnimationState attackState = new AnimationState();

    public byte animationTick = 0;
    private byte idleTimeout = 0;

    public ShellHammerSnail(EntityType<? extends ShellHammerSnail> entityType, World world) {
        super(entityType, world);
        this.setStepHeight(1f);
    }

    public static DefaultAttributeContainer.Builder createShellHammerSnailAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15)
                .add(EntityAttributes.GENERIC_ARMOR, 5)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 3)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.6)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20);
    }

    @Override
    protected void initGoals() {
        //Attack
        this.goalSelector.add(0, new ShellAttackGoal(this, 0.15, true));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, ShulkerEntity.class, false));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, EndMussel.class, true));

        //Move
        this.goalSelector.add(2, new EscapeDangerGoal(this, 0.15));
        this.goalSelector.add(3, new WanderAroundGoal(this, 0.12));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.12));

        super.initGoals();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) tickAnimation();
        else if (this.hasStatusEffect(StatusEffects.LEVITATION))
            placeMucus(this.getWorld());
    }

    private void tickAnimation(){
        if (this.idleTimeout <= 0) {
            this.idleTimeout = 80;
            this.idleState.start(this.age);
        } else --this.idleTimeout;

        if (this.animationTick >= 16) {
            this.animationTick = 0;
        } else ++this.animationTick;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 1) this.attackState.start(this.age);
        super.handleStatus(status);
    }

    @Override //Places the mucus right when the snail takes damage (from any source)
    protected float modifyAppliedDamage(DamageSource source, float amount) {
        if (!this.getWorld().isClient)
            placeMucus(this.getWorld());

        return super.modifyAppliedDamage(source, amount);
    }

    //Places the mucus underneath the snail... The mucus is actually a lie, it doesn't affect the snail speed and the snail already has its own gravity effect
    protected void placeMucus(World world) {
        this.setStatusEffect(new StatusEffectInstance(ExtraRegister.GRAVITY.get(), 2, 2, true, false), this);
        BlockState mucus = BlocksAndItemsRegister.GRAVITATIONAL_MUCUS.get().getDefaultState();
        BlockPos pos = this.getBlockPos();

        if (mucus.canPlaceAt(world, pos))
            world.setBlockState(pos, mucus);
    }

    @Override
    public boolean tryAttack(Entity target) {
        this.getWorld().sendEntityStatus(this, (byte) 1);
        return super.tryAttack(target);
    }

    @Override //This is what makes you be able to walk on the snail, isn't it cool?
    public boolean isCollidable() {
        return this.isAlive();
    }

    @Override
    public int getXpToDrop() {
        return random.nextBetween(2, 4);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(getStepSound(),2f, 1f);
    }

    protected SoundEvent getStepSound(){
        if (this.random.nextFloat() >= 0.65) return SoundRegister.SHELL_HAMMER_SNAIL_STEP_01.get();
        if (this.random.nextFloat() >= 0.35) return SoundRegister.SHELL_HAMMER_SNAIL_STEP_02.get();
        else return SoundRegister.SHELL_HAMMER_SNAIL_STEP_03.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundRegister.SHELL_HAMMER_SNAIL_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (this.random.nextFloat() > 0.65) return SoundRegister.SHELL_HAMMER_SNAIL_GRUNT_01.get();
        if (this.random.nextFloat() > 0.35) return SoundRegister.SHELL_HAMMER_SNAIL_GRUNT_02.get();
        else return SoundRegister.SHELL_HAMMER_SNAIL_GRUNT_03.get();
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }


    private static class ShellAttackGoal extends MeleeAttackGoal{
        public ShellAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        protected void attack(LivingEntity target, double squaredDistance) {
            if (target.isInRange(this.mob, 1.6f) && this.getCooldown() <= 0) {
                this.resetCooldown();
                this.mob.swingHand(Hand.MAIN_HAND);
                this.mob.tryAttack(target);
            }
        }
    }

}
