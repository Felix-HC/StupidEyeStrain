package de.felix.stupidEyeStrain.command;

import de.felix.stupidEyeStrain.StupidEyeStrain;
import de.felix.stupidEyeStrain.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;

public class ModCommands {
    public static void initialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager
                    .literal("break")
                    .executes(commandContext -> {
                        StupidEyeStrain.openReminderScreen();
                        return 1;
                 }));
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager
                    .literal("stupideyestrain")
                    .then(ClientCommandManager.literal("config")
                        .executes(commandContext -> {
                            Minecraft.getInstance().schedule(() -> {
                                Minecraft.getInstance().setScreen(AutoConfigClient.getConfigScreen(ModConfig.class, Minecraft.getInstance().screen).get());
                            });
                            return 1;
                        })));
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager
                    .literal("stupideyestrain")
                    .then(ClientCommandManager.literal("enable")
                            .executes(commandContext -> {
                                StupidEyeStrain.getConfig().enabled = true;
                                StupidEyeStrain.onUpdateConfig();
                                return 1;
                            })));
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager
                    .literal("stupideyestrain")
                    .then(ClientCommandManager.literal("disable")
                            .executes(commandContext -> {
                                StupidEyeStrain.getConfig().enabled = false;
                                StupidEyeStrain.onUpdateConfig();
                                return 1;
                            })));
        });
    }
}
