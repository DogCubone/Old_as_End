package perfectvoid.mega.Registers;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.poi.PointOfInterestType;
import perfectvoid.mega.old_as_end;

import java.util.Set;

public class PoiRegister {

    public static void init(){
        poi.register();
    }

    public static final DeferredRegister<PointOfInterestType> poi = DeferredRegister.create(old_as_end.MOD_ID, RegistryKeys.POINT_OF_INTEREST_TYPE);

    public static final RegistrySupplier<PointOfInterestType> END_HIVE = poi.register("end_hive", ()->new
            PointOfInterestType(Set.copyOf(BlocksAndItemsRegister.CRYSTALLIZED_END_STONE_HIVE.get().getStateManager().getStates()), 0, 1));


}
