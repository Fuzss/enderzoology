package fuzs.enderzoology.attachment;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class CommandSourceHelper {

    private CommandSourceHelper() {
        // NO-OP
    }

    public static CommandSourceStack createEmptyCommandSource(ServerLevel serverLevel) {
        return createEmptyCommandSource(serverLevel, 2);
    }

    public static CommandSourceStack createEmptyCommandSource(ServerLevel serverLevel, int permissionLevel) {
        return createEmptyCommandSource(serverLevel, null, permissionLevel);
    }

    public static CommandSourceStack createEmptyCommandSource(ServerLevel serverLevel, @Nullable Entity entity, int permissionLevel) {
        return new CommandSourceStack(
                CommandSource.NULL,
                Vec3.ZERO,
                Vec2.ZERO,
                serverLevel,
                permissionLevel, "Empty",
                Component.literal("Empty"),
                serverLevel.getServer(),
                entity
        ) {
            @Override
            public void sendSuccess(Supplier<Component> messageSupplier, boolean allowLogging) {
                // prevent CommandSourceStack::broadcastToAdmins from being called
                super.sendSuccess(messageSupplier, false);
            }
        };
    }
}
