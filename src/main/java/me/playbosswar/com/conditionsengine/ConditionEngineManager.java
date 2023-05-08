package me.playbosswar.com.conditionsengine;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.api.ConditionRule;
import me.playbosswar.com.api.ConditionRules;
import me.playbosswar.com.events.JoinEvents;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.Futures;
import me.playbosswar.com.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * A special thanks to clip for designing PAPI, this is inspired by his work
 */
public class ConditionEngineManager {
    // String is the name of the conditions group
    private final List<ConditionExtension> conditionExtensions = new ArrayList<>();
    @NotNull
    private final File folder;

    public ConditionEngineManager() {
        this.folder = new File(CommandTimerPlugin.getPlugin().getDataFolder(), "extensions");
        registerAll();
    }

    public void disable() {
        unregisterAll();
    }

    public List<ConditionExtension> getConditionExtensions() {
        return conditionExtensions;
    }

    public ConditionExtension getConditionExtension(String name) {
        Optional<ConditionExtension> optional =
                conditionExtensions
                        .stream()
                        .filter(conditionExtension -> conditionExtension.getConditionGroupName().equals(name))
                        .findFirst();

        return optional.orElse(null);
    }

    public ConditionRule getRule(String conditionGroup, String ruleName) {
        Optional<ConditionExtension> optionalConditionExtension = conditionExtensions.stream()
                .filter(conditionExtension -> conditionExtension.getConditionGroupName().equals(conditionGroup)).findFirst();

        if(optionalConditionExtension.isPresent()) {
            ConditionRules rules = optionalConditionExtension.get().getRules();

            for(ConditionRule rule : rules) {
                if(rule.getName().equals(ruleName)) {
                    return rule;
                }
            }
        }

        return null;
    }

    public void onDisable() {
        conditionExtensions.forEach(conditionExtension -> conditionExtension.getRules().clear());
    }

    public void register(
            @NotNull final Class<? extends ConditionExtension> clazz) {
        try {
            final ConditionExtension condition = createExpansionInstance(clazz);

            Objects.requireNonNull(condition.getAuthor(), "The condition author is null!");
            Objects.requireNonNull(condition.getConditionGroupName(), "The condition name is null!");
            Objects.requireNonNull(condition.getVersion(), "The condition version is null!");

            condition.register();
            if(condition.getEvents().size() > 0) {
                condition.getEvents().forEach(event -> {
                            if(event instanceof Listener) {
                                Bukkit.getPluginManager().registerEvents((Listener) event,
                                        CommandTimerPlugin.getPlugin());
                            }
                        }
                );
            }
        } catch(LinkageError | NullPointerException ex) {
            final String reason;

            if(ex instanceof LinkageError) {
                reason = " (Is a dependency missing?)";
            } else {
                reason = " - One of its properties is null which is not allowed!";
            }

            CommandTimerPlugin.getPlugin().getLogger().severe("Failed to load expansion class " + clazz.getSimpleName() +
                    reason);
            CommandTimerPlugin.getPlugin().getLogger().log(Level.SEVERE, "", ex);
        }

    }

    public boolean register(@NotNull final ConditionExtension condition) {
        if(!condition.canRegister()) {
            return false;
        }

        conditionExtensions.add(condition);

        if(condition instanceof Listener) {
            Bukkit.getPluginManager().registerEvents(((Listener) condition), CommandTimerPlugin.getPlugin());
        }

        CommandTimerPlugin.getPlugin().getLogger().info("Successfully registered conditions: " + condition.getConditionGroupName() + ", " + condition.getRules().size() + " were addded");

        return true;
    }

    private void registerAll() {
        Futures.onMainThread(CommandTimerPlugin.getPlugin(), findExpansionsOnDisk(), (classes, exception) -> {
            if(exception != null) {
                CommandTimerPlugin.getPlugin().getLogger().log(
                        Level.SEVERE,
                        "failed to load class files of expansions",
                        exception);
                return;
            }

            classes.stream().filter(Objects::nonNull).forEach(this::register);
        });
    }

    private void unregisterAll() {
        this.conditionExtensions.clear();
    }

    @Nullable
    public ConditionExtension createExpansionInstance(
            @NotNull final Class<? extends ConditionExtension> clazz) throws LinkageError {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch(final Exception ex) {
            if(ex.getCause() instanceof LinkageError) {
                throw ((LinkageError) ex.getCause());
            }

            return null;
        }
    }

    @NotNull
    private static final Set<MethodSignature> ABSTRACT_EXPANSION_METHODS =
            Arrays.stream(ConditionExtension.class.getDeclaredMethods())
                    .filter(method -> Modifier.isAbstract(method.getModifiers()))
                    .map(method -> new MethodSignature(method.getName(), method.getParameterTypes()))
                    .collect(Collectors.toSet());

    @NotNull
    public CompletableFuture<@NotNull List<@Nullable Class<? extends ConditionExtension>>> findExpansionsOnDisk() {
        return Arrays.stream(Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".jar"))))
                .map(this::findExpansionInFile)
                .collect(Futures.collector());
    }

    @NotNull
    public CompletableFuture<@Nullable Class<? extends ConditionExtension>> findExpansionInFile(
            @NotNull final File file) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final Class<? extends ConditionExtension> expansionClass = Files.findClass(file,
                        ConditionExtension.class);

                if(expansionClass == null) {
                    CommandTimerPlugin.getPlugin().getLogger().severe("Failed to load Condition: " + file.getName() + ", as it " +
                            "does not have" +
                            " a class which extends ConditionExtension.");
                    return null;
                }

                Set<MethodSignature> expansionMethods = Arrays.stream(expansionClass.getDeclaredMethods())
                        .map(method -> new MethodSignature(method.getName(), method.getParameterTypes()))
                        .collect(Collectors.toSet());
                if(!expansionMethods.containsAll(ABSTRACT_EXPANSION_METHODS)) {
                    CommandTimerPlugin.getPlugin().getLogger().severe("Failed to load Condition: " + file.getName() + ", as it " +
                            "does not have " +
                            "the" +
                            " required methods declared for a " +
                            "ConditionExtension.");
                    return null;
                }

                return expansionClass;
            } catch(final VerifyError ex) {
                CommandTimerPlugin.getPlugin().getLogger().severe("Failed to load Condition class " + file.getName() +
                        " (Is a dependency missing?)");
                CommandTimerPlugin.getPlugin().getLogger().severe("Cause: " + ex.getClass().getSimpleName() + " " + ex.getMessage());
                return null;
            } catch(final Exception ex) {
                throw new CompletionException(ex);
            }
        });
    }
}
