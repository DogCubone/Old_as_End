package perfectvoid.mega.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import perfectvoid.mega.client.models.EndMusselModel;
import perfectvoid.mega.entities.EndMussel;
import perfectvoid.mega.old_as_end;

@Environment(EnvType.CLIENT)
public class EndMusselRender extends MobEntityRenderer<EndMussel, EndMusselModel> {
    private static final Identifier MusselTexture = new Identifier(old_as_end.MOD_ID, "textures/entity/end_mussel.png");

    public EndMusselRender(EntityRendererFactory.Context context) {
        super(context, new EndMusselModel(context.getPart(EndMusselModel.EndMusselLayer)), 0.25f);
    }

    @Override
    public Identifier getTexture(EndMussel entity) {
        return MusselTexture;
    }
}
