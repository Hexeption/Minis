//? if !neoforge && >=1.21.4 {
package uk.co.hexeption.minis.mixin;

import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * SpecialModelRenderersAccessor
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
@Mixin(SpecialModelRenderers.class)
public interface SpecialModelRenderersAccessor {

    @SuppressWarnings("rawtypes")
    @Accessor("ID_MAPPER")
    static ExtraCodecs.LateBoundIdMapper minis$idMapper() {
        throw new AssertionError();
    }
}
//?}
