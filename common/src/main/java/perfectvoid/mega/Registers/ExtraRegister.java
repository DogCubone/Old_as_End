package perfectvoid.mega.Registers;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import perfectvoid.mega.effects.Gravity;
import perfectvoid.mega.old_as_end;
import perfectvoid.mega.others.Slam;

public class ExtraRegister {

    public static void init(){
    enchantments.register();
    effect.register();
    }

    public static final DeferredRegister<Enchantment> enchantments = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.ENCHANTMENT);
    public static final DeferredRegister<StatusEffect> effect = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.STATUS_EFFECT);

    //region Enchantments
    public static final RegistrySupplier<Enchantment> SLAM = enchantments.register("slam", ()-> new Slam(Enchantment.Rarity.RARE, EquipmentSlot.values()));
    //endregion Enchantments

    //region Effects
    public static final RegistrySupplier<StatusEffect> GRAVITY = effect.register("gravity", Gravity::new);
    //endregion Effects
}
