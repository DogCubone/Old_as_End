package perfectvoid.mega.world;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import perfectvoid.mega.old_as_end;

public class OldEndConfFeature {

    public static final RegistryKey<ConfiguredFeature<?,?>> SMALL_CHORUS_FLOWER_CONFIG = registerConfKey("small_chorus_flower_config");

    public static RegistryKey<ConfiguredFeature<?, ?>> registerConfKey(String name){
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(old_as_end.MOD_ID, name));
    }

}
