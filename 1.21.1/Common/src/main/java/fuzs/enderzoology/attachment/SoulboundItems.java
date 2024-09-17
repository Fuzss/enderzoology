package fuzs.enderzoology.attachment;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.init.v3.registry.LookupHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record SoulboundItems(List<ItemStack> items) {
    public static final SoulboundItems EMPTY = new SoulboundItems(Collections.emptyList());
    public static final Codec<SoulboundItems> CODEC = ItemStack.CODEC.listOf()
            .xmap(SoulboundItems::new, SoulboundItems::items);
    public static final StreamCodec<RegistryFriendlyByteBuf, SoulboundItems> STREAM_CODEC = ItemStack.LIST_STREAM_CODEC.map(
            SoulboundItems::new,
            SoulboundItems::items
    );

    public static EventResult onLivingDrops(LivingEntity entity, DamageSource damageSource, Collection<ItemEntity> drops, boolean recentlyHit) {
        if (entity instanceof ServerPlayer serverPlayer) {
            ModRegistry.SOULBOUND_ITEMS_ATTACHMENT_TYPE.set(serverPlayer, saveOnDeath(serverPlayer, drops));
        }

        return EventResult.PASS;
    }

    public static SoulboundItems saveOnDeath(ServerPlayer serverPlayer, Collection<ItemEntity> drops) {
        Holder<Enchantment> enchantment = LookupHelper.lookupEnchantment(serverPlayer,
                ModRegistry.SOULBOUND_ENCHANTMENT
        );
        List<ItemStack> items = drops.stream().filter((ItemEntity itemEntity) -> {
            return EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemEntity.getItem()) > 0;
        }).toList().stream().peek(drops::remove).map(ItemEntity::getItem).map(ItemStack::copy).peek((ItemStack itemStack) -> {
            if (!serverPlayer.getAbilities().instabuild) {
                int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemStack);
                if (serverPlayer.getRandom().nextInt(enchantmentLevel) == 0) {
                    EnchantmentHelper.updateEnchantments(itemStack, (ItemEnchantments.Mutable enchantments) -> {
                        enchantments.set(enchantment, enchantments.getLevel(enchantment) - 1);
                    });
                }
            }
        }).toList();
        return new SoulboundItems(items);
    }

    public static void onCopy(ServerPlayer originalPlayer, ServerPlayer newPlayer, boolean originalStillAlive) {
        if (!originalStillAlive) {
            ModRegistry.SOULBOUND_ITEMS_ATTACHMENT_TYPE.getOrDefault(originalPlayer, EMPTY)
                    .restoreAfterRespawn(newPlayer);
        }
    }

    public void restoreAfterRespawn(ServerPlayer serverPlayer) {
        CommandSourceStack commandSource = CommandSourceHelper.createEmptyCommandSource(serverPlayer.serverLevel());
        for (ItemStack itemStack : this.items) {
            ItemInput itemInput = new ItemInput(itemStack.getItemHolder(), itemStack.getComponentsPatch());
            try {
                GiveCommand.giveItem(commandSource,
                        itemInput,
                        Collections.singletonList(serverPlayer),
                        itemStack.getCount()
                );
            } catch (CommandSyntaxException exception) {
                // NO-OP
            }
        }
    }
}
