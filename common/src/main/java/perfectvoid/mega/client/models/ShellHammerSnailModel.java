package perfectvoid.mega.client.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import perfectvoid.mega.client.EntityAnimations;
import perfectvoid.mega.entities.ShellHammerSnail;
import perfectvoid.mega.old_as_end;

@Environment(EnvType.CLIENT)
public class ShellHammerSnailModel extends SinglePartEntityModel<ShellHammerSnail> {
	public static final EntityModelLayer ShellHammerSnailLayer = new EntityModelLayer(new Identifier(old_as_end.MOD_ID, "shell_hammer_snail"), "main");

	private final ModelPart main;
	private final ModelPart snail;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart antenna;
	private final ModelPart feet;
	private final ModelPart shell;

	public ShellHammerSnailModel(ModelPart root) {
		this.main = root;
		this.snail = root.getChild("snail");
		this.body = this.snail.getChild("body");
		this.head = this.body.getChild("head");
		this.antenna = this.head.getChild("antenna");
		this.feet = this.body.getChild("feet");
		this.shell = this.snail.getChild("shell");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData snail = modelPartData.addChild("snail", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData body = snail.addChild("body", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, -4.05F, -2.8333F, 4.0F, 4.0F, 8.0F, new Dilation(0.0F))
				.uv(0, 10).cuboid(-1.5F, -3.05F, 5.1667F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.05F, -0.6667F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, -5.5F, -2.5F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.65F, -2.8333F));

		ModelPartData antenna = head.addChild("antenna", ModelPartBuilder.create().uv(0, 15).cuboid(-1.4F, -3.5F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 15).cuboid(1.35F, -3.5F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -5.0F, -1.0F));

		ModelPartData feet = body.addChild("feet", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, 0.0F, -6.5F, 6.0F, 0.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.15F, 1.6667F));

		ModelPartData shell = snail.addChild("shell", ModelPartBuilder.create().uv(0, 28).mirrored().cuboid(-5.0F, -3.5F, -3.5F, 2.0F, 7.0F, 7.0F, new Dilation(0.0F)).mirrored(false)
				.uv(19, 8).cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F))
				.uv(0, 28).cuboid(3.0F, -3.5F, -3.5F, 2.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -5.0F, 0.5F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public ModelPart getPart() {
		return snail;
	}

	@Override
	public void setAngles(ShellHammerSnail entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		headYaw = MathHelper.clamp(headYaw, -180.0F, 180.0F);
		this.snail.yaw = headYaw * 0.017453292F; //Why is this value so specific? I copied it from the camel.

		this.updateAnimation(entity.idleState, EntityAnimations.Shell_Hammer_Snail_Idle, animationProgress);
		this.updateAnimation(entity.attackState, EntityAnimations.Shell_Hammer_Snail_Attack, animationProgress);
		this.animateMovement(EntityAnimations.Shell_Hammer_Snail_Walk, limbAngle, limbDistance, 100f, 160f);
//		12f and 6f is... nice
//		15f and 5f is also... nice
//		80f, 140f seems to be the best one so far
	}
}