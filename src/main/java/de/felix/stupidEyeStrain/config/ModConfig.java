package de.felix.stupidEyeStrain.config;

import de.felix.stupidEyeStrain.StupidEyeStrain;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = StupidEyeStrain.MOD_ID)
public class ModConfig implements ConfigData {
    public boolean enabled = true;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
    public int cooldown = 20;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int timer = 20;

    public boolean showToastsInMenus = true;
    public boolean onlyShowToasts = false;

    public boolean showSkipButton = true;
}
