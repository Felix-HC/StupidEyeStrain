package de.felix.stupidEyeStrain;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import de.felix.stupidEyeStrain.command.ModCommands;
import de.felix.stupidEyeStrain.config.ModConfig;
import de.felix.stupidEyeStrain.util.Timer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class StupidEyeStrain implements ModInitializer {
    public static final String MOD_ID = "stupid-eye-strain";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static ModConfig CONFIG;
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static void openReminderScreen() {
        Timer.stopTimer();

        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().setScreen(new ReminderScreen());
        });
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing " + MOD_ID);
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ModCommands.initialize();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            Timer.startTimer();
        });
    }

    public static ModConfig getConfig() {
        return CONFIG;
    }
}
