package perfectvoid.mega.others;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import perfectvoid.mega.old_as_end;

public class OldEndTags {
    public static TagKey<Block> EndStoneBlocks = TagKey.of(RegistryKeys.BLOCK, new Identifier(old_as_end.MOD_ID, "end_stone_blocks"));

    public static TagKey<Biome> canSpawnEndCreatures = TagKey.of(RegistryKeys.BIOME, new Identifier(old_as_end.MOD_ID, "can_spawn_end_creatures"));
}
