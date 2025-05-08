package perfectvoid.mega.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;
import perfectvoid.mega.entities.ShellHammerSnail;

public class Gravity extends StatusEffect {

    public Gravity() {
        super(StatusEffectCategory.NEUTRAL, 10668963);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        int power = (amplifier > 0) ? amplifier << 1 : 2;
        if (!(entity instanceof ShellHammerSnail)) {
            entity.setVelocity(0, -power, 0);
            entity.slowMovement(entity.getSteppingBlockState(), new Vec3d(0.3, 2, 0.3));
        } else
            entity.addVelocity(0, Math.abs(entity.getVelocity().y) * -power, 0);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
