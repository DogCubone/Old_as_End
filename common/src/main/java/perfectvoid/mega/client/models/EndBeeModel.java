package perfectvoid.mega.client.models;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.Identifier;
import perfectvoid.mega.client.EntityAnimations;
import perfectvoid.mega.entities.EndBee;
import perfectvoid.mega.old_as_end;

@Environment(EnvType.CLIENT)
public class EndBeeModel extends SinglePartEntityModel<EndBee> {
	public static final EntityModelLayer EndBeeLayer = new EntityModelLayer(new Identifier(old_as_end.MOD_ID, "end_bee"), "main");

	private final ModelPart main;
	private final ModelPart body;
	private final ModelPart l_wing;
	private final ModelPart r_wing;
	private final ModelPart r_paws;
	private final ModelPart l_paws;

	public EndBeeModel(ModelPart root) {
		this.main = root;
		this.body = root.getChild("body");
		this.l_wing = this.body.getChild("l_wing");
		this.r_wing = this.body.getChild("r_wing");
		this.r_paws = this.body.getChild("r_paws");
		this.l_paws = this.body.getChild("l_paws");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(22, 23).cuboid(-3.5F, -4.0F, -3.0F, 7.0F, 7.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 17).cuboid(-4.0F, -4.5F, 0.0F, 8.0F, 8.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-4.5F, -5.0F, 3.0F, 9.0F, 9.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 28).cuboid(-3.5F, -4.0F, 11.0F, 7.0F, 7.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 2).cuboid(-1.5F, -4.0F, -6.0F, 0.0F, 2.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(1.5F, -4.0F, -6.0F, 0.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, -5.0F));

		ModelPartData l_wing = body.addChild("l_wing", ModelPartBuilder.create().uv(20, 0).cuboid(-0.5F, 0.0F, -3.0F, 7.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -4.0F, 2.0F));

		ModelPartData r_wing = body.addChild("r_wing", ModelPartBuilder.create().uv(16, 17).cuboid(-6.5F, 0.0F, -3.0F, 7.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, -4.0F, 2.0F));

		ModelPartData r_paws = body.addChild("r_paws", ModelPartBuilder.create().uv(18, 27).cuboid(0.0F, 0.0F, -3.0F, 0.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.25F, 3.0F, 0.0F));

		ModelPartData l_paws = body.addChild("l_paws", ModelPartBuilder.create().uv(26, 0).cuboid(0.0F, 0.0F, -3.0F, 0.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(3.25F, 3.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}


	@Override
	public ModelPart getPart() {
		return main;
	}

	@Override
	public void setAngles(EndBee endBee, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);

		this.animateMovement(EntityAnimations.End_Bee_Fly, limbAngle, limbDistance, 1f, 2f);
		this.updateAnimation(endBee.idleState, EntityAnimations.End_Bee_Idle, animationProgress);
	}
}