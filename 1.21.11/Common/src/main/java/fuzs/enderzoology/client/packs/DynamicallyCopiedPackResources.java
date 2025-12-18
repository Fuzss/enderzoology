package fuzs.enderzoology.client.packs;

import com.mojang.blaze3d.platform.NativeImage;
import fuzs.puzzleslib.api.resources.v1.AbstractModPackResources;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DynamicallyCopiedPackResources extends AbstractModPackResources {
    private final ResourceManager resourceManager;
    private final VanillaPackResources vanillaPackResources;
    private final Map<Identifier, TextureCopy> textures;

    protected DynamicallyCopiedPackResources(TextureCopy... textures) {
        Minecraft minecraft = Minecraft.getInstance();
        this.resourceManager = minecraft.getResourceManager();
        this.vanillaPackResources = minecraft.getVanillaPackResources();
        this.textures = Stream.of(textures)
                .collect(Collectors.toMap(TextureCopy::destinationLocation, Function.identity()));
    }

    public static Supplier<AbstractModPackResources> create(TextureCopy... textures) {
        return () -> {
            return new DynamicallyCopiedPackResources(textures);
        };
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType packType, Identifier identifier) {
        if (this.textures.containsKey(identifier)) {
            TextureCopy textureCopy = this.textures.get(identifier);
            Optional<Resource> vanillaResource = this.resourceManager.getResource(textureCopy.vanillaLocation());
            if (vanillaResource.isPresent()) {
                try (NativeImage nativeImage = NativeImage.read(vanillaResource.get().open())) {
                    // check the vanilla texture aspect ratio; some mods using OptiFine change the texture file completely since they also change the model
                    // make sure to check the aspect ratio instead of absolute width / height to support higher resolution resource packs
                    // in that case fall back to the skeleton texture from the vanilla assets pack
                    if (nativeImage.getWidth() / nativeImage.getHeight() !=
                            textureCopy.vanillaImageWidth() / textureCopy.vanillaImageHeight()) {
                        return this.vanillaPackResources.getResource(packType, textureCopy.vanillaLocation());
                    }
                } catch (IOException exception) {
                    // NO-OP
                }
                return vanillaResource.get()::open;
            }
        }

        return null;
    }

    @Override
    public Set<String> getNamespaces(PackType packType) {
        return this.textures.keySet().stream().map(Identifier::getNamespace).collect(Collectors.toSet());
    }

    public record TextureCopy(Identifier vanillaLocation,
                              Identifier destinationLocation,
                              int vanillaImageWidth,
                              int vanillaImageHeight) {

        public TextureCopy {
            if (vanillaLocation.getNamespace().equals(destinationLocation.getNamespace())) {
                throw new IllegalStateException("%s and %s share same namespace".formatted(vanillaLocation, destinationLocation));
            }
            if (!vanillaLocation.getPath().endsWith(".png")) {
                throw new IllegalArgumentException("%s is no texture location".formatted(vanillaLocation));
            }
            if (!destinationLocation.getPath().endsWith(".png")) {
                throw new IllegalArgumentException("%s is no texture location".formatted(destinationLocation));
            }
        }
    }
}
