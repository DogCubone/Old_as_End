package perfectvoid.mega.world;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import perfectvoid.mega.old_as_end;


public class OldEndFeatures {

    public static final RegistryKey<PlacedFeature> SMALL_CHORUS_FLOWER_FEATURE = registerPlacedKey("small_chorus_flower");
    public static final RegistryKey<PlacedFeature> OLD_END_ORE_FEATURE = registerPlacedKey("old_end_ore");

    private static RegistryKey<PlacedFeature> registerPlacedKey(String name){
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(old_as_end.MOD_ID, name));
    }

}