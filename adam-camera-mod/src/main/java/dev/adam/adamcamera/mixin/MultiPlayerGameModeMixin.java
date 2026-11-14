package dev.adam.adamcamera.mixin;

import dev.adam.adamcamera.AdamCameraClient;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Stops block/entity interaction from a far-away camera position. */
@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void adamcamera$blockUseOnBlock(CallbackInfoReturnable<InteractionResult> cir) {
        if (AdamCameraClient.isActive()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void adamcamera$blockInteractEntity(CallbackInfoReturnable<InteractionResult> cir) {
        if (AdamCameraClient.isActive()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method = "interactAt", at = @At("HEAD"), cancellable = true)
    private void adamcamera$blockInteractEntityAt(CallbackInfoReturnable<InteractionResult> cir) {
        if (AdamCameraClient.isActive()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void adamcamera$blockAttackEntity(CallbackInfo ci) {
        if (AdamCameraClient.isActive()) {
            ci.cancel();
        }
    }
}
