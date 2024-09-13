package fuzs.enderzoology.client.model;

import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Wolf;

public class DireWolfModel<T extends Wolf> extends WolfModel<T> {
    private final ModelPart head;

    public DireWolfModel(ModelPart modelPart) {
        super(modelPart);
        this.head = modelPart.getChild("head");
    }

    public ModelPart getHead() {
        return this.head;
    }
}
