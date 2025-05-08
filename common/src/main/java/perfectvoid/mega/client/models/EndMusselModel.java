package perfectvoid.mega.client.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import perfectvoid.mega.client.EntityAnimations;
import perfectvoid.mega.entities.EndMussel;
import perfectvoid.mega.old_as_end;

@Environment(EnvType.CLIENT)
public class EndMusselModel extends SinglePartEntityModel<EndMussel> {
    public static final EntityModelLayer EndMusselLayer = new EntityModelLayer(new Identifier(old_as_end.MOD_ID, "end_mussel"), "main");

    private final ModelPart main;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart segment;

    public EndMusselModel(ModelPart root) {
        this.main = root;
        this.body = root.getChild("body");
        this.head = body.getChild("head");
        this.segment = body.getChild("segment");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -2.5F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData segment = body.addChild("segment", ModelPartBuilder.create().uv(0, 8).cuboid(-1.5F, -3.0F, 1.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 16, 16);
    }

    @Override
    public void setAngles(EndMussel entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        headYaw = MathHelper.clamp(headYaw, -180.0F, 180.0F);
        this.body.yaw = headYaw * 0.017453292F;

        this.updateAnimation(entity.idleState, EntityAnimations.EndMussel_Idle, animationProgress, 1f);
        this.animateMovement(EntityAnimations.EndMussel_Walk, limbAngle, limbDistance, 15f, 230f);
    }

    public ModelPart getPart() {
        return main;
    }

}
