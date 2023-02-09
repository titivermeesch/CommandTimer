package me.playbosswar.com.api.events;

import me.playbosswar.com.api.NeededValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class EventExtension {
    @NotNull
    public abstract String getEventName();

    @NotNull
    public abstract String[] getEventDescription();

    // List of fields that
    public abstract ArrayList<NeededValue<?>> getReturnedValues();
}
