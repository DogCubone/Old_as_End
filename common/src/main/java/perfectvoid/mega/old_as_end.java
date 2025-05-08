package perfectvoid.mega;

import perfectvoid.mega.Registers.*;

public final class old_as_end {
    public static final String MOD_ID = "old_as_end";

    public static void init() {
        EntityRegister.init();
        BlocksAndItemsRegister.init();
        ExtraRegister.init();
        SoundRegister.init();
        ArsenalRegister.init();

        PoiRegister.init();
        TabRegister.init();
    }
}
