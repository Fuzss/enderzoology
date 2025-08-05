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
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.rightWing = root.getChild("right_wing");
        this.leftWing = root.getChild("left_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -5.0F, -2.5F, 6.0F, 5.0F, 5.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));

        head.addOrReplaceChild("right_eyebrow",
                CubeListBuilder.create().texOffs(23, 15).addBox(-4.0F, -14.0F, -2.6F, 3.0F, 2.0F, 0.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        head.addOrReplaceChild("left_eyebrow",
                CubeListBuilder.create().texOffs(23, 17).addBox(1.0F, -14.0F, -2.6F, 3.0F, 2.0F, 0.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        head.addOrReplaceChild("beak",
                CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, -11.0F, -3.0F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition body = partDefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(1, 10).addBox(-3.0F, -3.0F, -2.5F, 6.0F, 6.0F, 5.0F),
                PartPose.offset(0.0F, 19.0F, 0.0F));

        body.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(18, 28).addBox(-3.0F, -2.0F, 2.5F, 5.0F, 0.0F, 3.0F),
                PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition rightLeg = partDefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(14, 22).addBox(-5.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.0F, 22.0F, 0.0F));

        rightLeg.addOrReplaceChild("right_toes",
                CubeListBuilder.create().texOffs(11, 28).addBox(-3.5F, 0.0F, -3.0F, 3.0F, 0.0F, 2.0F),
                PartPose.offset(-2.0F, 2.0F, 0.0F));

        PartDefinition leftLeg = partDefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(23, 22).addBox(3.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.0F, 22.0F, 0.0F));

        leftLeg.addOrReplaceChild("left_toes",
                CubeListBuilder.create().texOffs(11, 28).addBox(0.5F, 0.0F, -3.0F, 3.0F, 0.0F, 2.0F),
                PartPose.offset(2.0F, 2.0F, 0.0F));

        partDefinition.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(2, 21).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 6.0F, 4.0F),
                PartPose.offset(-3.0F, 16.0F, 0.0F));

        partDefinition.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(22, 4).addBox(0.0F, 0.0F, -2.0F, 1.0F, 6.0F, 4.0F),
                PartPose.offset(3.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    @Override
    public void setupAnim(OwlRenderState renderState) {
        super.setupAnim(renderState);
        this.head.xRot = renderState.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = renderState.yRot * Mth.DEG_TO_RAD;
        if (renderState.isFlying) {
            this.body.xRot += 0.4937F;
            this.leftLeg.xRot += Mth.DEG_TO_RAD * 40.0F;
            this.rightLeg.xRot += Mth.DEG_TO_RAD * 40.0F;
        }
        this.rightWing.zRot = renderState.flapAngle * 0.7F;
        this.leftWing.zRot = -renderState.flapAngle * 0.7F;
    }
}
