package perfectvoid.mega.others;

import net.minecraft.block.Block;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.poi.PointOfInterestType;
import perfectvoid.mega.old_as_end;

public class OldEndTags {
    public static TagKey<Block> EndStoneBlocks = TagKey.of(RegistryKeys.BLOCK, new Identifier(old_as_end.MOD_ID, "end_stone_blocks"));

    public static TagKey<Biome> canSpawnEndCreatures = registerTag(RegistryKeys.BIOME,"can_spawn_end_creatures");
            //TagKey.of(RegistryKeys.BIOME, new Identifier(old_as_end.MOD_ID, "can_spawn_end_creatures"));

    public static TagKey<PointOfInterestType> EndHive = registerTag(RegistryKeys.POINT_OF_INTEREST_TYPE, "end_hive");

    private static <T> TagKey<T> registerTag(RegistryKey<? extends Registry<T>> key, String id){
        return TagKey.of(key, new Identifier(old_as_end.MOD_ID, id));
    }
}
