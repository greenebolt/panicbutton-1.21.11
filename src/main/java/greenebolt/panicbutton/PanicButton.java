package greenebolt.panicbutton;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PanicButton implements ModInitializer {
	public static final String MOD_ID = "panicbutton";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.parse("panicbutton"));
		String KEY = "key.panicbutton.quit_game";

		KeyMapping panickey;
		panickey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				KEY,
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_COMMA,
				CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (panickey.consumeClick()) {
				Disconnect();
			}
		});

		LOGGER.info("Panic Button Initialized!");
	}

	public static void Disconnect() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.getSingleplayerServer() != null) {
			mc.execute(() -> {
				mc.disconnectFromWorld(Component.translatable("Panic Quit!"));
			});
			return;
		}
		if (mc.player == null) return;
		mc.execute(() -> {
			mc.disconnect(new TitleScreen(), false);
		});
	}
}