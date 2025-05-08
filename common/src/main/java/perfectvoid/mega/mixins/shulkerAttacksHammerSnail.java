package perfectvoid.mega.mixins;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import perfectvoid.mega.entities.ShellHammerSnail;

import java.util.Optional;

@Mixin(ShulkerEntity.class)
public abstract class shulkerAttacksHammerSnail extends GolemEntity implements VariantHolder<Optional<DyeColor>>, Monster {

    protected shulkerAttacksHammerSnail(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    protected void initCustomGoal(CallbackInfo ci){
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, ShellHammerSnail.class, true));
        this.goalSelector.add(3, new LookAtEntityGoal(this, ShellHammerSnail.class, 6.0F, 0.04F, true));
    }

    @Override
    public void setVariant(Optional<DyeColor> variant) {
    }

    @Override
    public Optional<DyeColor> getVariant() {
        return Optional.empty();
    }
}
