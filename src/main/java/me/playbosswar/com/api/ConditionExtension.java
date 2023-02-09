package me.playbosswar.com.api;

import com.cryptomorin.xseries.XMaterial;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.events.EventExtension;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class ConditionExtension {
    /**
     * Give back the name of your group of conditions.
     * <p>
     * Please use capitals and _ only. No spaces, numbers or other special characters
     *
     * @return Condition group name
     */
    @NotNull
    public abstract String getConditionGroupName();

    @NotNull
    public abstract String[] getDescription();

    /**
     * Return the name of the person(s) who created these conditions
     *
     * @return Author name
     */
    @NotNull
    public abstract String getAuthor();

    /**
     * Get conditions group version
     *
     * @return Version
     */
    @NotNull
    public abstract String getVersion();

    /**
     * Material used to display the set of rules in GUIs
     *
     * @return XMaterial
     */
    public XMaterial getGroupIcon() {
        return XMaterial.COMMAND_BLOCK;
    }

    ;

    /**
     * Get name of a required plugin for these conditions
     *
     * @return Plugin name
     */
    @Nullable
    public String getRequiredPlugin() {
        return null;
    }

    /**
     * Return a list of all the rules to register
     *
     * @return list of rules
     */
    @NotNull
    public ConditionRules getRules() {
        return new ConditionRules();
    }

    @NotNull
    public final CommandTimerPlugin getCommandTimerPlugin() {
        return CommandTimerPlugin.getInstance();
    }

    /**
     * Check if conditions can be activated
     *
     * @return boolean
     */
    public boolean canRegister() {
        return getRequiredPlugin() == null
                || Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    /**
     * Manually register a condition. The conditions will register themselves if the file is placed
     * under CommandTimer/extensions
     *
     * @return boolean if registration went through
     */
    public boolean register() {
        return getCommandTimerPlugin().getConditionEngineManager().register(this);
    }

    public abstract ArrayList<EventExtension> getEvents();
}
