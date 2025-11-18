package me.playbosswar.com.utils;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.hooks.PAPIHook;
import me.playbosswar.com.tasks.TaskInterval;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandExecutor {
    private static final String CONDITION_NO_MATCH = "Conditions did not match";

    public static class ExecutionContext {
        private final String command;
        private final Gender gender;
        private final TaskInterval interval;
        private final List<UUID> scopedPlayers;
        private final Function<OfflinePlayer, Boolean> conditionChecker;
        private final Runnable onExecution;
        private final Consumer<Integer> executionCounter;

        private ExecutionContext(Builder builder) {
            this.command = builder.command;
            this.gender = builder.gender;
            this.interval = builder.interval != null ? builder.interval : new TaskInterval(0, 0, 0, 0);
            this.scopedPlayers = builder.scopedPlayers;
            this.conditionChecker = builder.conditionChecker;
            this.onExecution = builder.onExecution;
            this.executionCounter = builder.executionCounter;
        }

        public static class Builder {
            private String command;
            private Gender gender;
            private TaskInterval interval;
            private List<UUID> scopedPlayers;
            private Function<OfflinePlayer, Boolean> conditionChecker;
            private Runnable onExecution;
            private Consumer<Integer> executionCounter;

            public Builder command(String command) {
                this.command = command;
                return this;
            }

            public Builder gender(Gender gender) {
                this.gender = gender;
                return this;
            }

            public Builder interval(TaskInterval interval) {
                this.interval = interval;
                return this;
            }

            public Builder scopedPlayers(List<UUID> scopedPlayers) {
                this.scopedPlayers = scopedPlayers;
                return this;
            }

            public Builder conditionChecker(Function<OfflinePlayer, Boolean> conditionChecker) {
                this.conditionChecker = conditionChecker;
                return this;
            }

            public Builder onExecution(Runnable onExecution) {
                this.onExecution = onExecution;
                return this;
            }

            public Builder executionCounter(Consumer<Integer> executionCounter) {
                this.executionCounter = executionCounter;
                return this;
            }

            public ExecutionContext build() {
                return new ExecutionContext(this);
            }
        }
    }

    public static boolean execute(ExecutionContext context) {
        Gender gender = context.gender;

        if (gender.equals(Gender.CONSOLE)) {
            return runConsoleCommand(context);
        } else if (gender.equals(Gender.PLAYER)) {
            return runPlayerCommand(context);
        } else if (gender.equals(Gender.OPERATOR)) {
            return runOperatorCommand(context);
        } else if (gender.equals(Gender.CONSOLE_PER_USER)) {
            return runConsolePerUserCommand(context);
        } else if (gender.equals(Gender.CONSOLE_PER_USER_OFFLINE)) {
            return runConsolePerUserOfflineCommand(context);
        } else if (gender.equals(Gender.CONSOLE_PROXY)) {
            return runConsoleProxyCommand(context);
        }

        return false;
    }

    private static boolean runConsoleCommand(ExecutionContext context) {
        if (context.conditionChecker != null) {
            Boolean valid = context.conditionChecker.apply(null);
            if (valid == null || !valid) {
                Messages.sendDebugConsole(CONDITION_NO_MATCH);
                return false;
            }
        }

        String command = context.command;
        CommandTimerPlugin.getScheduler().runTask(() -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, null));
            if (context.executionCounter != null) {
                context.executionCounter.accept(1);
            }
        });
        
        if (context.onExecution != null) {
            context.onExecution.run();
        }
        return true;
    }

    private static boolean runPlayerCommand(ExecutionContext context) {
        String command = context.command;

        Collection<Player> affectedPlayers = (Collection<Player>) Bukkit.getOnlinePlayers();
        if (context.scopedPlayers != null && !context.scopedPlayers.isEmpty()) {
            affectedPlayers = context.scopedPlayers.stream().map(Bukkit::getPlayer).filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        boolean delayedExecution = context.interval.toSeconds() > 0;
        int i = 0;
        boolean willExecute = false;
        for (Player p : affectedPlayers) {
            if (context.conditionChecker != null) {
                Boolean valid = context.conditionChecker.apply((OfflinePlayer) p);
                if (valid == null || !valid) {
                    Messages.sendDebugConsole(CONDITION_NO_MATCH);
                    continue;
                }
            }

            willExecute = true;
            if (delayedExecution) {
                CommandTimerPlugin.getScheduler().runTaskLater(() -> runForPlayer(p, command, context),
                        (20L * i * context.interval.toSeconds()) + 1);
            } else {
                runForPlayer(p, command, context);
            }
            i++;
        }

        return willExecute;
    }

    private static void runForPlayer(Player p, String command, ExecutionContext context) {
        String parsedCommand = PAPIHook.parsePAPI(command, p);
        CommandTimerPlugin.getScheduler().runTask(() -> {
            boolean executed = p.performCommand(parsedCommand);

            if (!executed) {
                String errorMessage = new StringEnhancer("Failed to execute command {command}").add("command", command)
                        .parse();
                throw new CommandException(errorMessage);
            }
            if (context.executionCounter != null) {
                context.executionCounter.accept(1);
            }
            if (context.onExecution != null) {
                context.onExecution.run();
            }
        });
    }

    private static boolean runOperatorCommand(ExecutionContext context) {
        String command = context.command;

        Collection<Player> affectedPlayers = (Collection<Player>) Bukkit.getOnlinePlayers();
        if (context.scopedPlayers != null && !context.scopedPlayers.isEmpty()) {
            affectedPlayers = context.scopedPlayers.stream().map(Bukkit::getPlayer).filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        boolean delayedExecutions = context.interval.toSeconds() > 0;
        int i = 0;
        boolean willExecute = false;
        for (Player p : affectedPlayers) {
            boolean wasAlreadyOp = p.isOp();

            try {
                p.setOp(true);
                if (context.conditionChecker != null) {
                    Boolean valid = context.conditionChecker.apply((OfflinePlayer) p);
                    if (valid == null || !valid) {
                        Messages.sendDebugConsole(CONDITION_NO_MATCH);

                        if (!wasAlreadyOp) {
                            p.setOp(false);
                        }
                        continue;
                    }
                }
                willExecute = true;

                if (delayedExecutions) {
                    CommandTimerPlugin.getScheduler().runTaskLater(() -> {
                        p.performCommand(PAPIHook.parsePAPI(command, p));
                        if (context.executionCounter != null) {
                            context.executionCounter.accept(1);
                        }
                        if (context.onExecution != null) {
                            context.onExecution.run();
                        }
                    }, (20L * i * context.interval.toSeconds()) + 1);
                } else {
                    CommandTimerPlugin.getScheduler().runTask(() -> {
                        p.performCommand(PAPIHook.parsePAPI(command, p));
                        if (context.executionCounter != null) {
                            context.executionCounter.accept(1);
                        }
                        if (context.onExecution != null) {
                            context.onExecution.run();
                        }
                    });
                }

            } finally {
                if (!wasAlreadyOp) {
                    p.setOp(false);
                }
            }
            i++;
        }

        return willExecute;
    }

    private static boolean runConsolePerUserCommand(ExecutionContext context) {
        String command = context.command;

        Collection<Player> affectedPlayers = (Collection<Player>) Bukkit.getOnlinePlayers();
        if (context.scopedPlayers != null && !context.scopedPlayers.isEmpty()) {
            affectedPlayers = context.scopedPlayers.stream().map(Bukkit::getPlayer).filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        boolean delayedExecutions = context.interval.toSeconds() > 0;
        int i = 0;
        boolean willExecute = false;
        for (Player p : affectedPlayers) {
            if (context.conditionChecker != null) {
                Boolean valid = context.conditionChecker.apply((OfflinePlayer) p);
                if (valid == null || !valid) {
                    Messages.sendDebugConsole(CONDITION_NO_MATCH);
                    continue;
                }
            }
            willExecute = true;

            if (delayedExecutions) {
                CommandTimerPlugin.getScheduler().runTaskLater(() -> {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                    if (context.executionCounter != null) {
                        context.executionCounter.accept(1);
                    }
                    if (context.onExecution != null) {
                        context.onExecution.run();
                    }
                }, (20L * i * context.interval.toSeconds()) + 1);
            } else {
                CommandTimerPlugin.getScheduler().runTask(() -> {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                    if (context.executionCounter != null) {
                        context.executionCounter.accept(1);
                    }
                    if (context.onExecution != null) {
                        context.onExecution.run();
                    }
                });
            }
            i++;
        }

        return willExecute;
    }

    private static boolean runConsolePerUserOfflineCommand(ExecutionContext context) {
        String command = context.command;
        boolean delayedExecutions = context.interval.toSeconds() > 0;

        int i = 0;
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (context.conditionChecker != null) {
                Boolean valid = context.conditionChecker.apply(p);
                if (valid == null || !valid) {
                    Messages.sendDebugConsole(CONDITION_NO_MATCH);
                    continue;
                }
            }
            if (delayedExecutions) {
                CommandTimerPlugin.getScheduler().runTaskLater(() -> {
                    CommandTimerPlugin.getScheduler().runTask(() -> {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                        if (context.executionCounter != null) {
                            context.executionCounter.accept(1);
                        }
                        if (context.onExecution != null) {
                            context.onExecution.run();
                        }
                    });
                }, (20L * i * context.interval.toSeconds()) + 1);
            } else {
                CommandTimerPlugin.getScheduler().runTask(() -> {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                    if (context.executionCounter != null) {
                        context.executionCounter.accept(1);
                    }
                    if (context.onExecution != null) {
                        context.onExecution.run();
                    }
                });
            }
            i++;
        }

        return true;
    }

    private static boolean runConsoleProxyCommand(ExecutionContext context) {
        if (context.conditionChecker != null) {
            Boolean valid = context.conditionChecker.apply(null);
            if (valid == null || !valid) {
                Messages.sendDebugConsole(CONDITION_NO_MATCH);
                return false;
            }
        }

        String command = context.command;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("executeConsoleCommand");
            out.writeUTF(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getServer().sendPluginMessage(CommandTimerPlugin.getPlugin(), "commandtimer:main", b.toByteArray());
        if (context.executionCounter != null) {
            context.executionCounter.accept(1);
        }
        if (context.onExecution != null) {
            context.onExecution.run();
        }
        return true;
    }
}

