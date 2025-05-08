package perfectvoid.mega.fabric;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.gen.GenerationStep;
import perfectvoid.mega.Registers.EntityRegister;
import perfectvoid.mega.others.OldEndTags;

import java.util.function.Predicate;

import static perfectvoid.mega.world.OldEndFeatures.OLD_END_ORE_FEATURE;
import static perfectvoid.mega.world.OldEndFeatures.SMALL_CHORUS_FLOWER_FEATURE;

public class WorldSetup {
    private static final Predicate<BiomeSelectionContext> isInTheEnd = isEnd();

    public static void init(){
        initWorldFeatures();
        initCreatureSpawn();
    }

    private static void initWorldFeatures(){
        BiomeModifications.addFeature(
                isInTheEnd,
                GenerationStep.Feature.VEGETAL_DECORATION,
                SMALL_CHORUS_FLOWER_FEATURE
        );

        BiomeModifications.addFeature(
                isInTheEnd,
                GenerationStep.Feature.UNDERGROUND_ORES,
                OLD_END_ORE_FEATURE
        );
    }

    private static void initCreatureSpawn(){
        BiomeModifications.addSpawn(
                isInTheEnd,
                SpawnGroup.MONSTER,
                EntityRegister.SHELL_HAMMER_SNAIL_ENTITY.get(),
                3, 1, 4
        );

        BiomeModifications.addSpawn(
                isInTheEnd,
                SpawnGroup.MONSTER,
                EntityRegister.END_MUSSEL_ENTITY.get(), 6, 3, 5
        );
    }

    private static Predicate<BiomeSelectionContext> isEnd(){
        return BiomeSelectors.tag(OldEndTags.canSpawnEndCreatures);
    }
}
