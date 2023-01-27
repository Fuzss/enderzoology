package fuzs.enderzoology.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.FlyingAnimal;

public class OwlModel<T extends Entity & FlyingAnimal> extends AgeableListModel<T> {
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public OwlModel(ModelPart root) {
        super(true, 24.0F, 0.0F, 3.0F, 2.0F, 24.0F);
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.rightLeg = this.body.getChild("right_leg");
        this.leftLeg = this.body.getChild("left_leg");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -3.0F, 6, 7, 5), PartPose.offset(0.0F, 15.0F, 0.0F));
        body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(23, 0).addBox(-1.0F, 0.0F, -3.0F, 1, 5, 5), PartPose.offset(-3.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(23, 0).mirror().addBox(0.0F, 0.0F, -2.5F, 1, 5, 5), PartPose.offset(3.0F, 0.0F, -0.5F));
        PartDefinition rightLeg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(23, 11).addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1), PartPose.offset(-1.5F, 7.0F, 0.0F));
        rightLeg.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(28, 11).addBox(-1.0F, 0.0F, -2.0F, 2, 1, 3), PartPose.offset(0.0F, 1.0F, 0.0F));
        PartDefinition leftLeg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(23, 11).addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1), PartPose.offset(1.5F, 7.0F, 0.0F));
        leftLeg.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(28, 11).addBox(-1.0F, 0.0F, -2.0F, 2, 1, 3), PartPose.offset(0.0F, 1.0F, 0.0F));
        PartDefinition tailBase = body.addOrReplaceChild("tail_base", CubeListBuilder.create().texOffs(56, 0).addBox(-1.5F, 1.0F, -1.8F, 3, 3, 1), PartPose.offsetAndRotation(0.0F, 3.5F, 2.2F, 0.64F, 0.0F, 0.0F));
        tailBase.addOrReplaceChild("tail_part1", CubeListBuilder.create().texOffs(58, 5).addBox(0.0F, 2.0F, -1.7F, 2, 3, 1), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.96F));
        tailBase.addOrReplaceChild("tail_part2", CubeListBuilder.create().texOffs(58, 5).mirror().addBox(-2.0F, 2.0F, -1.7F, 2, 3, 1), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.96F));
        tailBase.addOrReplaceChild("tail_part3", CubeListBuilder.create().texOffs(58, 5).addBox(-1.0F, 2.0F, -1.7F, 2, 3, 1), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-3.5F, -6.0F, -3.0F, 7, 6, 6), PartPose.offsetAndRotation(0.0F, 15.0F, -0.5F, 0.0F, 0.05F, 0.0F));
        head.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(36, 29).addBox(0.0F, -0.8F, -4.0F, 1, 1, 2), PartPose.offsetAndRotation(-0.5F, -2.3F, -0.4F, 0.36F, 0.0F, 0.0F));
        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(27, 29).addBox(-5.0F, -8.0F, -3.0F, 3, 2, 1), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(27, 29).mirror().addBox(2.0F, -8.0F, -3.0F, 3, 2, 1), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * 0.017453292F;
        this.head.yRot = netHeadYaw * 0.017453292F;
        this.leftLeg.xRot = -0.0299F;
        this.rightLeg.xRot = -0.0299F;
        if (entity.isFlying()) {
            this.leftLeg.xRot += 0.6981317F;
            this.rightLeg.xRot += 0.6981317F;
        }
        this.rightWing.zRot = ageInTicks;
        this.leftWing.zRot = -ageInTicks;
    }
}
