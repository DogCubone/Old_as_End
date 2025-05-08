package perfectvoid.mega.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import perfectvoid.mega.client.models.ShellHammerSnailModel;
import perfectvoid.mega.entities.ShellHammerSnail;
import perfectvoid.mega.old_as_end;

@Environment(EnvType.CLIENT)
public class ShellHammerSnailRender extends MobEntityRenderer<ShellHammerSnail, ShellHammerSnailModel> {
    private static final Identifier ShellHammerSnailTexture1 = new Identifier(old_as_end.MOD_ID, "textures/entity/shell_snail/shell_hammer_snail.png");
    private static final Identifier ShellHammerSnailTexture2 = new Identifier(old_as_end.MOD_ID, "textures/entity/shell_snail/shell_hammer_snail2.png");
    private static final Identifier ShellHammerSnailTexture3 = new Identifier(old_as_end.MOD_ID, "textures/entity/shell_snail/shell_hammer_snail3.png");
    private static final Identifier ShellHammerSnailTexture4 = new Identifier(old_as_end.MOD_ID, "textures/entity/shell_snail/shell_hammer_snail4.png");

    public ShellHammerSnailRender(EntityRendererFactory.Context context) {
        super(context, new ShellHammerSnailModel(context.getPart(ShellHammerSnailModel.ShellHammerSnailLayer)), 0.4f);
    }

    @Override
    public Identifier getTexture(ShellHammerSnail entity) {
        if (entity.animationTick >= 12) return ShellHammerSnailTexture4;
        else if (entity.animationTick >= 8) return ShellHammerSnailTexture3;
        else if (entity.animationTick >= 4) return ShellHammerSnailTexture2;
        else return ShellHammerSnailTexture1;
    }
}
