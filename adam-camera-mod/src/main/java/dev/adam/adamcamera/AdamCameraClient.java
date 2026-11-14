package dev.adam.adamcamera;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class AdamCameraClient implements ClientModInitializer {

    public static final String MOD_ID = "adamcamera";

    private static KeyMapping toggleKey;

    private static AdamCamera camera;
    private static LocalPlayer owner;
    private static ClientLevel ownerLevel;
    private static ClientInput realInput;
    private static ClientInput dummyInput;
    private static CameraType rememberedCameraType;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(MOD_ID, "main"));

        // F4 by default; can be changed in Options > Controls > Key Binds.
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.adamcamera.toggle",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_F4,
                category));

        ClientTickEvents.START_CLIENT_TICK.register(AdamCameraClient::onStartTick);
        ClientTickEvents.END_CLIENT_TICK.register(AdamCameraClient::onEndTick);
    }

    public static boolean isActive() {
        return camera != null;
    }

    public static AdamCamera getCamera() {
        return camera;
    }

    private static void onStartTick(Minecraft mc) {
        if (camera == null) {
            return;
        }
        // Turn the camera off if the world/player changed under us (disconnect, respawn, dimension change, death).
        if (mc.player == null || mc.level == null || mc.player != owner || mc.level != ownerLevel
                || mc.player.isDeadOrDying()) {
            disable(mc, false);
            return;
        }
        // Keep the real player frozen while the camera is out.
        mc.player.input = dummyInput;
    }

    private static void onEndTick(Minecraft mc) {
        while (toggleKey.consumeClick()) {
            if (mc.player == null || mc.level == null) {
                continue;
            }
            if (camera == null) {
                enable(mc);
            } else {
                disable(mc, true);
            }
        }

        // Move the camera ourselves, once per tick.
        if (camera != null && mc.screen == null) {
            camera.updateMovement();
        }
    }

    private static void enable(Minecraft mc) {
        LocalPlayer player = mc.player;
        owner = player;
        ownerLevel = mc.level;

        // Freeze the real player by swapping in an input that never presses anything.
        realInput = player.input;
        dummyInput = new ClientInput();
        player.input = dummyInput;

        rememberedCameraType = mc.options.getCameraType();
        if (rememberedCameraType != CameraType.FIRST_PERSON) {
            mc.options.setCameraType(CameraType.FIRST_PERSON);
        }

        camera = new AdamCamera(mc.level);
        camera.snapTo(player.getX(), player.getEyeY() - camera.getEyeHeight(), player.getZ(),
                player.getYRot(), player.getXRot());
        camera.spawn();
        mc.setCameraEntity(camera);
        mc.smartCull = false;

        player.displayClientMessage(Component.translatable("adamcamera.enabled"), true);
    }

    private static void disable(Minecraft mc, boolean announce) {
        AdamCamera oldCamera = camera;
        camera = null;
        mc.smartCull = true;

        if (mc.player != null) {
            // Only hand the old input back if it's still the same player object.
            if (mc.player == owner && realInput != null) {
                mc.player.input = realInput;
            }
            mc.setCameraEntity(mc.player);
        }

        if (oldCamera != null && mc.level != null && mc.level == ownerLevel) {
            oldCamera.despawn();
        }

        if (rememberedCameraType != null) {
            mc.options.setCameraType(rememberedCameraType);
        }

        if (announce && mc.player != null) {
            mc.player.displayClientMessage(Component.translatable("adamcamera.disabled"), true);
        }

        owner = null;
        ownerLevel = null;
        realInput = null;
        dummyInput = null;
        rememberedCameraType = null;
    }
}
