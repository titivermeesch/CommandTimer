package me.playbosswar.com.utils;

import me.playbosswar.com.enums.WorldWeather;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class WeatherConditions {
    public static boolean checkPlayerMatchesWeather(Player p, List<WorldWeather> worldWeathers) {
        if(worldWeathers.contains(WorldWeather.RAINING) && Objects.equals(p.getPlayerWeather(), WeatherType.DOWNFALL)) {
            return true;
        }

        if(worldWeathers.contains(WorldWeather.THUNDER) && p.getWorld().hasStorm()) {
            return true;
        }

        return worldWeathers.contains(WorldWeather.CLEAR);
    }
}
