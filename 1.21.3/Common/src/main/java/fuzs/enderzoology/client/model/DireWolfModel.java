package fuzs.enderzoology.client.model;

import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;

public class DireWolfModel extends WolfModel {
    public final ModelPart head;

    public DireWolfModel(ModelPart modelPart) {
        super(modelPart);
        this.head = modelPart.getChild("head");
    }

    public ModelPart getHead() {
        return this.head;
    }
}
