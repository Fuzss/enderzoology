package fuzs.enderzoology.data.tags;

import fuzs.enderzoology.init.ModItems;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class ModItemTagProvider extends AbstractTagProvider<Item> {

    public ModItemTagProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(ModItems.HUNTING_BOW_ITEM);
        this.tag(ItemTags.BOW_ENCHANTABLE).add(ModItems.HUNTING_BOW_ITEM);
        this.tag(ItemTags.CROSSBOW_ENCHANTABLE).add(ModItems.HUNTING_BOW_ITEM);
    }
}
