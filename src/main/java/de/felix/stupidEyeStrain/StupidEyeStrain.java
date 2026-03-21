package de.felix.stupidEyeStrain;

import de.felix.stupidEyeStrain.command.ModCommands;
import de.felix.stupidEyeStrain.config.ModConfig;
import de.felix.stupidEyeStrain.util.Timer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StupidEyeStrain implements ModInitializer {
    public static final String MOD_ID = "stupid-eye-strain";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static ModConfig CONFIG;

    public static void openReminderScreen() {
        Timer.stopTimer();

        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().setScreen(new ReminderScreen());
        });
    }

    public static void onUpdateConfig() {
        Timer.stopTimer();
        if (getConfig().enabled) {
            Timer.startTimer();
            Minecraft.getInstance().getToastManager().addToast(
                    SystemToast.multiline(Minecraft.getInstance(), SystemToast.SystemToastId.NARRATOR_TOGGLE, Component.nullToEmpty("Updated Config"), Component.nullToEmpty("Cooldown has been reset"))
            );
        }
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing " + MOD_ID);
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new)
                .registerSaveListener((configHolder, modConfig) -> {
                    onUpdateConfig();
                    return null;
                });
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ModCommands.initialize();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (getConfig().enabled) {
                Timer.startTimer();
            }
        });
    }

    public static ModConfig getConfig() {
        return CONFIG;
    }
}
