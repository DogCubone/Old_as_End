package perfectvoid.mega.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import perfectvoid.mega.Registers.ExtraRegister;
import perfectvoid.mega.Registers.SoundRegister;

public class ShellHammer extends ToolItem {
    private final float attackDamage;
    private final float slamPower;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public ShellHammer(int attackDamage, float attackSpeed, float slamPower, Settings settings, ToolMaterial material) {
        super(material, settings);
        this.attackDamage = (float)attackDamage + material.getAttackDamage();
        this.slamPower    = slamPower;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getWorld();
        if (!world.isClient){
            slam(stack, target, attacker);
            stack.damage(1, attacker, (entity) -> entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundRegister.HAMMER_ATTACK.get(), attacker.getSoundCategory(), 1F, 0.5F + world.random.nextFloat());
        }
        return false;
    }

    private void slam(ItemStack weapon, LivingEntity entity, LivingEntity sourceEntity){
        float slamPowerLevel = this.slamPower + (EnchantmentHelper.getLevel(ExtraRegister.SLAM.get(), weapon));

        double yAngle = sourceEntity.getRotationVector().y;
        double xPower = sourceEntity.getRotationVector().x * slamPowerLevel;
        double zPower = sourceEntity.getRotationVector().z * slamPowerLevel;

        Vec3d entVector = new Vec3d(xPower, 0.5, zPower);

        //This code makes you either achieve haven or go to hell :3
        if (yAngle >= 0.7 || yAngle <= -0.7)
            entVector = new Vec3d(0, absMultiplier(entVector.y, slamPowerLevel, yAngle < -0.7), 0);

        entity.setVelocity(entVector);
    }

    private double absMultiplier(double axis, double multiplier, boolean invert){
        axis = (axis >= 0.0D) ? axis * multiplier : -axis; //Custom "Math.abs" to avoid always multiplying

        if (invert) axis = -axis * multiplier;

        return axis;
    }

    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return 1.0f;
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }
}
