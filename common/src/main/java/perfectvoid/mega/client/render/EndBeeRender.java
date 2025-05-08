package perfectvoid.mega.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import perfectvoid.mega.client.models.EndBeeModel;
import perfectvoid.mega.entities.EndBee;
import perfectvoid.mega.old_as_end;

@Environment(EnvType.CLIENT)
public class EndBeeRender extends MobEntityRenderer<EndBee, EndBeeModel> {
    private static final Identifier EndBeeTexture = new Identifier(old_as_end.MOD_ID, "textures/entity/end_bee.png");

    public EndBeeRender(EntityRendererFactory.Context context) {
        super(context, new EndBeeModel(context.getPart(EndBeeModel.EndBeeLayer)), 0.8f);
    }

    @Override
    public Identifier getTexture(EndBee entity) {
        return EndBeeTexture;
    }
}
