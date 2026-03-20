package de.felix.stupidEyeStrain;

import de.felix.stupidEyeStrain.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReminderScreen extends Screen {
    private final int timerSeconds = StupidEyeStrain.getConfig().timer;
    private boolean screenActive = false;

    public ReminderScreen() {
        super(Component.literal("Rest your eyes!"));
    }

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private int timeLeft = timerSeconds;
    private String timerText = "";

    Runnable timer = new Runnable() {
        @Override
        public void run() {
            if (timeLeft <= 0) {
                timerText = timerSeconds + (timerSeconds == 1 ? " second has passed" : " seconds have passed");
                continueButton.active = true;
                executor.shutdown();
            } else if (timeLeft == 1) {
                timerText = timeLeft + " second";
            } else {
                timerText = timeLeft + " seconds";
            }

            timeLeft--;
        }
    };

    private Button continueButton;
    private Button skipButton;

    @Override
    protected void init() {
        if (!screenActive) {
            executor.scheduleAtFixedRate(timer, 0, 1, TimeUnit.SECONDS);
            screenActive = true;
        }

        continueButton = Button.builder(Component.literal("Continue"), button -> {
                Minecraft.getInstance().setScreen(null);
                Timer.startTimer();
                screenActive = false;
        })
            .bounds(width / 2 - 100, height / 4 * 3, 200, 20)
            .tooltip(Tooltip.create(Component.literal("Continue playing!")))
            .build();
        continueButton.active = false;

        if (StupidEyeStrain.getConfig().showSkipButton) {
            skipButton = Button.builder(Component.literal("Skip"), button -> {
                        Minecraft.getInstance().setScreen(null);
                        Timer.startTimer();
                        screenActive = false;
                    })
                    .bounds(width / 2 + 110, height / 4 * 3, 50, 20)
                    .tooltip(Tooltip.create(Component.literal("Skip the timer")))
                    .build();

            addRenderableWidget(skipButton);
        }

        addRenderableWidget(continueButton);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(this.font, "Rest your eyes, look at something far away!", width / 2, 50, 0xFFFFFFFF);
        graphics.drawCenteredString(this.font, timerText, width / 2, 50 + this.font.lineHeight + 10, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
    }
}
