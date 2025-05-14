package perfectvoid.mega.Registers;

import com.mojang.serialization.Codec;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import perfectvoid.mega.entities.EndBee;
import perfectvoid.mega.entities.EndMussel;
import perfectvoid.mega.entities.ShellHammerSnail;
import perfectvoid.mega.old_as_end;

import java.util.Optional;

public class EntityRegister {

    public static void init() {
        memoryModuleType.register();
        entity.register();
        initAttributes();
    }

    private static void initAttributes(){
        EntityAttributeRegistry.register(END_BEE, EndBee::createEndBeeAttributes);
        EntityAttributeRegistry.register(END_MUSSEL_ENTITY, EndMussel::createEndMusselAttributes);
        EntityAttributeRegistry.register(SHELL_HAMMER_SNAIL_ENTITY, ShellHammerSnail::createShellHammerSnailAttributes);
    }

    public static final DeferredRegister<EntityType<?>> entity = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.ENTITY_TYPE);
    public static final DeferredRegister<MemoryModuleType<?>> memoryModuleType = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.MEMORY_MODULE_TYPE);


    //region Memory Module Type
    public static final RegistrySupplier<MemoryModuleType<BlockPos>> HIVE_LOCATION = memoryModuleType.register("hive_location", ()-> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));
    public static final RegistrySupplier<MemoryModuleType<BlockPos>> FLOWER_LOCATION = memoryModuleType.register("flower_location", ()-> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));
    public static final RegistrySupplier<MemoryModuleType<BlockPos>> FORGET_HIVE_TICK = memoryModuleType.register("forget_hive_tick", ()-> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));
    public static final RegistrySupplier<MemoryModuleType<BlockPos>> FORGET_FLOWER_TICK = memoryModuleType.register("forget_flower_tick", ()-> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));

    //endregion Memory Module Type

    //region Entity
    public static final RegistrySupplier<EntityType<EndBee>> END_BEE                             = entity.register("end_bee", () -> EntityType.Builder.create(EndBee::new, SpawnGroup.CREATURE).setDimensions(0.8f, 0.8f).build(modID("end_bee")));
    public static final RegistrySupplier<EntityType<EndMussel>> END_MUSSEL_ENTITY                = entity.register("end_mussel", () -> EntityType.Builder.create(EndMussel::new, SpawnGroup.CREATURE).setDimensions(0.3f, 0.3f).build(modID("end_mussel")));
    public static final RegistrySupplier<EntityType<ShellHammerSnail>> SHELL_HAMMER_SNAIL_ENTITY = entity.register("shell_hammer_snail", () -> EntityType.Builder.create(ShellHammerSnail::new, SpawnGroup.CREATURE).setDimensions(0.6f, 0.5f).build(modID("shell_hammer_snail")));
    //endregion Entity

    private static String modID(String name){
        return old_as_end.MOD_ID + ':' + name;
    }
}
