package perfectvoid.mega.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import perfectvoid.mega.Registers.EntityRegister;
import perfectvoid.mega.client.models.EndBeeModel;
import perfectvoid.mega.client.models.EndMusselModel;
import perfectvoid.mega.client.models.ShellHammerSnailModel;
import perfectvoid.mega.client.render.EndBeeRender;
import perfectvoid.mega.client.render.EndMusselRender;
import perfectvoid.mega.client.render.ShellHammerSnailRender;

@Environment(EnvType.CLIENT)
public class ClientRender {

    public static void initEntityModels(){
        EntityRendererRegistry.register(EntityRegister.END_BEE, EndBeeRender::new);
        EntityModelLayerRegistry.register(EndBeeModel.EndBeeLayer, EndBeeModel::getTexturedModelData);

        EntityRendererRegistry.register(EntityRegister.END_MUSSEL_ENTITY, EndMusselRender::new);
        EntityModelLayerRegistry.register(EndMusselModel.EndMusselLayer, EndMusselModel::getTexturedModelData);

        EntityRendererRegistry.register(EntityRegister.SHELL_HAMMER_SNAIL_ENTITY, ShellHammerSnailRender::new);
        EntityModelLayerRegistry.register(ShellHammerSnailModel.ShellHammerSnailLayer, ShellHammerSnailModel::getTexturedModelData);
    }
}
