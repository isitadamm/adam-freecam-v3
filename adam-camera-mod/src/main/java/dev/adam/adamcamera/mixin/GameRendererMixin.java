package dev.adam.adamcamera.mixin;

import dev.adam.adamcamera.AdamCameraClient;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Hides the first-person hand and block outline while the camera is detached. */
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void adamcamera$hideHand(CallbackInfo ci) {
        if (AdamCameraClient.isActive()) {
            ci.cancel();
        }
    }

    @Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
    private void adamcamera$hideOutline(org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        if (AdamCameraClient.isActive()) {
            cir.setReturnValue(false);
        }
    }
}
