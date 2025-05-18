package me.playbosswar.com.scheduler;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

public class FoliaSchedulerAdapter implements SchedulerAdapter {
    private static final boolean SUPPORTED;
    private static final boolean HAS_IS_CANCELLED;

    private static @Nullable MethodHandle ASYNC_SCHEDULER_RUN;
    private static @Nullable MethodHandle ASYNC_SCHEDULER_RUN_DELAYED;
    private static @Nullable MethodHandle ASYNC_SCHEDULER_RUN_RATE;
    private static @Nullable MethodHandle SCHEDULED_TASK_IS_CANCELLED;
    private static @Nullable MethodHandle SCHEDULED_TASK_CANCEL;

    static {
        boolean supporting = true;
        boolean hasMethod = false;
        try {
            Lookup lookup = MethodHandles.publicLookup();
            Class<?> scheduledTaskType = Class.forName("io.papermc.paper.threadedregions.scheduler.ScheduledTask");
            SCHEDULED_TASK_CANCEL = lookup.findVirtual(scheduledTaskType, "cancel", MethodType.methodType(
                    Class.forName("io.papermc.paper.threadedregions.scheduler.ScheduledTask$CancelledState")
            ));
            SCHEDULED_TASK_IS_CANCELLED = lookup.findVirtual(scheduledTaskType, "isCancelled", MethodType.methodType(boolean.class));

            Class<?> asyncSchedulerType = Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");

            MethodHandle getAsyncScheduler = lookup.findVirtual(Server.class, "getGlobalRegionScheduler", MethodType.methodType(asyncSchedulerType));
            Object asyncScheduler = getAsyncScheduler.invoke(Bukkit.getServer());

            ASYNC_SCHEDULER_RUN = lookup.findVirtual(asyncSchedulerType, "run", MethodType.methodType(
                    scheduledTaskType, Plugin.class, Consumer.class
            )).bindTo(asyncScheduler);
            ASYNC_SCHEDULER_RUN_DELAYED = lookup.findVirtual(asyncSchedulerType, "runDelayed", MethodType.methodType(
                    scheduledTaskType, Plugin.class, Consumer.class, long.class
            )).bindTo(asyncScheduler);
            ASYNC_SCHEDULER_RUN_RATE = lookup.findVirtual(asyncSchedulerType, "runAtFixedRate", MethodType.methodType(
                    scheduledTaskType, Plugin.class, Consumer.class, long.class, long.class
            )).bindTo(asyncScheduler);

            try {
                BukkitTask.class.getMethod("isCancelled");
                hasMethod = true;
            } catch (NoSuchMethodException ignored) {
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            supporting = false;
        } catch (Throwable throwable) {
            Logger.getLogger(FoliaSchedulerAdapter.class.getName()).log(Level.WARNING, "Error in Folia scheduler adapter initialization", throwable);
        }
        SUPPORTED = supporting;
        HAS_IS_CANCELLED = hasMethod;
    }

    private final Plugin plugin;

    public FoliaSchedulerAdapter(Plugin plugin) {
        this.plugin = plugin;
    }

    public static boolean isSupported() {
        return SUPPORTED;
    }

    @Override
    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
        try {
            final Consumer<Object> consumer = task -> runnable.run();
            return new ScheduledTask(Objects.requireNonNull(ASYNC_SCHEDULER_RUN_RATE).invoke(plugin, consumer, delay, period));
        } catch (Throwable e) {
            plugin.getLogger().log(Level.SEVERE, "Error in task scheduling by the Folia scheduler adapter", e);
        }
        return new ScheduledTask(null);
    }

    @Override
    public BukkitTask runTask(Runnable runnable) {
        try {
            final Consumer<Object> consumer = task -> runnable.run();
            return new ScheduledTask(Objects.requireNonNull(ASYNC_SCHEDULER_RUN).invoke(plugin, consumer));
        } catch (Throwable e) {
            plugin.getLogger().log(Level.SEVERE, "Error in task scheduling by the Folia scheduler adapter", e);
        }
        return new ScheduledTask(null);
    }


    @Override
    public BukkitTask runTaskLater(Runnable runnable, long delay) {
        try {
            final Consumer<Object> consumer = task -> runnable.run();
            return new ScheduledTask(Objects.requireNonNull(ASYNC_SCHEDULER_RUN_DELAYED).invoke(plugin, consumer, delay));
        } catch (Throwable e) {
            plugin.getLogger().log(Level.SEVERE, "Error in task scheduling by the Folia scheduler adapter", e);
        }
        return new ScheduledTask(null);
    }

    private class ScheduledTask implements BukkitTask {
        private final Object task;

        public ScheduledTask(Object task) {
            this.task = task;
        }

        @Override
        public int getTaskId() {
            return 0;
        }

        @Override
        public Plugin getOwner() {
            return null;
        }

        @Override
        public boolean isSync() {
            return false;
        }

        public boolean isCancelled() {
            if (!HAS_IS_CANCELLED) {
                return false;
            }
            try {
                return (boolean) Objects.requireNonNull(SCHEDULED_TASK_IS_CANCELLED).invoke(task);
            } catch (Throwable e) {
                plugin.getLogger().log(Level.SEVERE, "Error in task cancellation check by the Folia scheduler adapter", e);
            }
            return false;
        }

        @Override
        public void cancel() {
            try {
                Objects.requireNonNull(SCHEDULED_TASK_CANCEL).invoke(task);
            } catch (Throwable e) {
                plugin.getLogger().log(Level.SEVERE, "Error in task canceling by the Folia scheduler adapter", e);
            }
        }
    }
}

