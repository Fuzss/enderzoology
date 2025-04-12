package fuzs.enderzoology.client.model;

import fuzs.enderzoology.client.renderer.entity.state.OwlRenderState;
import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import java.util.Set;

public class OwlModel extends EntityModel<OwlRenderState> {
    public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(true,
            24.0F,
            0.0F,
            3.0F,
            2.0F,
            24.0F,
            Set.of("head"));

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public OwlModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.rightLeg = this.body.getChild("right_leg");
        this.leftLeg = this.body.getChild("left_leg");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition body = partDefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -3.0F, 6, 7, 5),
                PartPose.offset(0.0F, 15.0F, 0.0F));
        body.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(23, 0).addBox(-1.0F, 0.0F, -3.0F, 1, 5, 5),
                PartPose.offset(-3.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(23, 0).mirror().addBox(0.0F, 0.0F, -2.5F, 1, 5, 5),
                PartPose.offset(3.0F, 0.0F, -0.5F));
        PartDefinition rightLeg = body.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(23, 11).addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1),
                PartPose.offset(-1.5F, 7.0F, 0.0F));
        rightLeg.addOrReplaceChild("right_foot",
                CubeListBuilder.create().texOffs(28, 11).addBox(-1.0F, 0.0F, -2.0F, 2, 1, 3),
                PartPose.offset(0.0F, 1.0F, 0.0F));
        PartDefinition leftLeg = body.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(23, 11).addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1),
                PartPose.offset(1.5F, 7.0F, 0.0F));
        leftLeg.addOrReplaceChild("left_foot",
                CubeListBuilder.create().texOffs(28, 11).addBox(-1.0F, 0.0F, -2.0F, 2, 1, 3),
                PartPose.offset(0.0F, 1.0F, 0.0F));
        PartDefinition tailBase = body.addOrReplaceChild("tail_base",
                CubeListBuilder.create().texOffs(56, 0).addBox(-1.5F, 1.0F, -1.8F, 3, 3, 1),
                PartPose.offsetAndRotation(0.0F, 3.5F, 2.2F, 0.64F, 0.0F, 0.0F));
        tailBase.addOrReplaceChild("tail_part1",
                CubeListBuilder.create().texOffs(58, 5).addBox(0.0F, 2.0F, -1.7F, 2, 3, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.96F));
        tailBase.addOrReplaceChild("tail_part2",
                CubeListBuilder.create().texOffs(58, 5).mirror().addBox(-2.0F, 2.0F, -1.7F, 2, 3, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.96F));
        tailBase.addOrReplaceChild("tail_part3",
                CubeListBuilder.create().texOffs(58, 5).addBox(-1.0F, 2.0F, -1.7F, 2, 3, 1),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition head = partDefinition.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 20).addBox(-3.5F, -6.0F, -3.0F, 7, 6, 6),
                PartPose.offsetAndRotation(0.0F, 15.0F, -0.5F, 0.0F, 0.05F, 0.0F));
        head.addOrReplaceChild("beak",
                CubeListBuilder.create().texOffs(36, 29).addBox(0.0F, -0.8F, -4.0F, 1, 1, 2),
                PartPose.offsetAndRotation(-0.5F, -2.3F, -0.4F, 0.36F, 0.0F, 0.0F));
        head.addOrReplaceChild("right_ear",
                CubeListBuilder.create().texOffs(27, 29).addBox(-5.0F, -8.0F, -3.0F, 3, 2, 1),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("left_ear",
                CubeListBuilder.create().texOffs(27, 29).mirror().addBox(2.0F, -8.0F, -3.0F, 3, 2, 1),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void setupAnim(OwlRenderState renderState) {
        super.setupAnim(renderState);
        this.head.xRot = renderState.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = renderState.yRot * Mth.DEG_TO_RAD;
        this.leftLeg.xRot = -0.03F;
        this.rightLeg.xRot = -0.03F;
        if (renderState.isFlying) {
            this.leftLeg.xRot += Mth.DEG_TO_RAD * 40.0F;
            this.rightLeg.xRot += Mth.DEG_TO_RAD * 40.0F;
        }
        this.rightWing.zRot = renderState.flapAngle;
        this.leftWing.zRot = -renderState.flapAngle;
    }
}
