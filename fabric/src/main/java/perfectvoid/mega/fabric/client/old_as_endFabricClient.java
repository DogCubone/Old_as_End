package perfectvoid.mega.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import perfectvoid.mega.Registers.BlocksAndItemsRegister;
import perfectvoid.mega.client.ClientRender;

public final class old_as_endFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientRender.initEntityModels();

        BlockRenderLayerMap.INSTANCE.putBlock(BlocksAndItemsRegister.CHORUS_HONEY_BLOCK.get(), RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(BlocksAndItemsRegister.GRAVITATIONAL_MUCUS.get(), RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksAndItemsRegister.STRANGE_MUCUS_BLOCK.get(), RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksAndItemsRegister.POWERED_STRANGE_MUCUS_BLOCK.get(), RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksAndItemsRegister.ULTRA_POWERED_STRANGE_MUCUS_BLOCK.get(), RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksAndItemsRegister.GRAVITATIONAL_MUCUS_BLOCK.get(), RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksAndItemsRegister.POTTED_SMALL_CHORUS_FLOWER.get(), RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlocksAndItemsRegister.SMALL_CHORUS_FLOWER.get(), RenderLayer.getCutout());
    }
}
