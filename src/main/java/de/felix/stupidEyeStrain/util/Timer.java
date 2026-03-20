package de.felix.stupidEyeStrain.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;

import static de.felix.stupidEyeStrain.StupidEyeStrain.getConfig;
import static de.felix.stupidEyeStrain.StupidEyeStrain.openReminderScreen;

public class Timer {
    private static final Runnable timerRunnable = () -> {
        try {
            Thread.sleep((long) getConfig().cooldown * 1000 * 60); // ms -> s -> m
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!Thread.currentThread().isInterrupted()) {
            if (Minecraft.getInstance().screen == null && !getConfig().onlyShowToasts || !getConfig().showToastsInMenus && !getConfig().onlyShowToasts) {
                openReminderScreen();
            } else {
                Minecraft.getInstance().getToastManager().addToast(
                        SystemToast.multiline(Minecraft.getInstance(), SystemToast.SystemToastId.NARRATOR_TOGGLE, Component.nullToEmpty(getConfig().cooldown + (getConfig().cooldown == 1 ? " minute has" : " minutes have") + " passed"), Component.nullToEmpty("Rest your eyes, look at something far away!"))
                );

                Component startBreakLink = Component.literal(" [Start Break]")
                        .withStyle(style -> style.withClickEvent(new ClickEvent.RunCommand("break")))
                        .withStyle(style -> style.withBold(true))
                        .withColor(13457920);

                Component finalMessage = Component.literal(getConfig().cooldown + (getConfig().cooldown == 1 ? " minute has" : " minutes have") + " passed")
                        .append(startBreakLink);

                Minecraft.getInstance().execute(() -> Minecraft.getInstance().gui.getChat().addMessage(finalMessage));

                startTimer();
            }
        }
    };

    private static Thread thread;

    public static void startTimer() {
        thread = new Thread(timerRunnable);
        thread.start();
    }

    public static void stopTimer() {
        assert thread != null;
        thread.interrupt();
    }
}
