package perfectvoid.mega.fabric;

import net.fabricmc.api.ModInitializer;
import perfectvoid.mega.old_as_end;

public final class old_as_endFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        old_as_end.init();
        WorldSetup.init();
    }
}
