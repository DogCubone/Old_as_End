package perfectvoid.mega.Registers;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import perfectvoid.mega.old_as_end;

public class SoundRegister {
    public static void init(){
        soundEvent.register();
    }

    public static final DeferredRegister<SoundEvent> soundEvent = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.SOUND_EVENT);


    //region sounds
    public static final RegistrySupplier<SoundEvent> HAMMER_ATTACK = soundEvent.register("hammer_attack", ()->
            SoundEvent.of(new Identifier(old_as_end.MOD_ID,"hammer_attack")));

    public static final RegistrySupplier<SoundEvent> SHELL_HAMMER_SNAIL_GRUNT_01 = soundEvent.register("shell_hammer_snail_grunt_01", ()->
            SoundEvent.of(new Identifier(old_as_end.MOD_ID,"shell_hammer_snail_grunt_01")));
    public static final RegistrySupplier<SoundEvent> SHELL_HAMMER_SNAIL_GRUNT_02 = soundEvent.register("shell_hammer_snail_grunt_02", ()->
            SoundEvent.of(new Identifier(old_as_end.MOD_ID,"shell_hammer_snail_grunt_02")));
    public static final RegistrySupplier<SoundEvent> SHELL_HAMMER_SNAIL_GRUNT_03 = soundEvent.register("shell_hammer_snail_grunt_03", ()->
            SoundEvent.of(new Identifier(old_as_end.MOD_ID,"shell_hammer_snail_grunt_03")));
    public static final RegistrySupplier<SoundEvent> SHELL_HAMMER_SNAIL_HURT = soundEvent.register("shell_hammer_snail_hurt", ()->
            SoundEvent.of(new Identifier(old_as_end.MOD_ID,"shell_hammer_snail_hurt")));
    public static final RegistrySupplier<SoundEvent> SHELL_HAMMER_SNAIL_STEP_01 = soundEvent.register("shell_hammer_snail_step_01", ()->
            SoundEvent.of(new Identifier(old_as_end.MOD_ID,"shell_hammer_snail_step_01")));
    public static final RegistrySupplier<SoundEvent> SHELL_HAMMER_SNAIL_STEP_02 = soundEvent.register("shell_hammer_snail_step_02", ()->
            SoundEvent.of(new Identifier(old_as_end.MOD_ID,"shell_hammer_snail_step_02")));
    public static final RegistrySupplier<SoundEvent> SHELL_HAMMER_SNAIL_STEP_03 = soundEvent.register("shell_hammer_snail_step_03", ()->
            SoundEvent.of(new Identifier(old_as_end.MOD_ID,"shell_hammer_snail_step_03")));
    //endregion sounds
}
