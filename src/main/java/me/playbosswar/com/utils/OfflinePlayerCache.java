package me.playbosswar.com.utils;

import me.playbosswar.com.CommandTimerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OfflinePlayerCache {
    public static final long DEFAULT_TTL_MS = 300_000L;

    private static final long NAME_WARM_BUDGET_MS = 2_000L;

    private static final OfflinePlayer[] EMPTY = new OfflinePlayer[0];

    private final Supplier<OfflinePlayer[]> loader;
    private final BooleanSupplier onServerThread;
    private final Consumer<Runnable> asyncExecutor;
    private final Supplier<Long> clock;
    private final long ttlMs;

    private final AtomicBoolean refreshing = new AtomicBoolean(false);
    private volatile OfflinePlayer[] snapshot = EMPTY;
    private volatile long refreshedAt = 0L;
    private volatile boolean loaded = false;

    public OfflinePlayerCache() {
        this(Bukkit::getOfflinePlayers, Tools::isServerThread,
                runnable -> CommandTimerPlugin.getScheduler().runTaskAsynchronously(runnable),
                System::currentTimeMillis, DEFAULT_TTL_MS);
    }

    OfflinePlayerCache(Supplier<OfflinePlayer[]> loader, BooleanSupplier onServerThread,
                       Consumer<Runnable> asyncExecutor, Supplier<Long> clock, long ttlMs) {
        this.loader = loader;
        this.onServerThread = onServerThread;
        this.asyncExecutor = asyncExecutor;
        this.clock = clock;
        this.ttlMs = ttlMs;
    }

    public OfflinePlayer[] get() {
        if(isStale()) {
            if(onServerThread.getAsBoolean()) {
                scheduleRefresh();
            } else {
                refreshNow();
            }
        }

        return snapshot;
    }

    public void refreshNow() {
        if(!refreshing.compareAndSet(false, true)) {
            return;
        }

        try {
            load();
        } finally {
            refreshing.set(false);
        }
    }

    public void scheduleRefresh() {
        if(!refreshing.compareAndSet(false, true)) {
            return;
        }

        try {
            asyncExecutor.accept(() -> {
                try {
                    load();
                } finally {
                    refreshing.set(false);
                }
            });
        } catch(RuntimeException e) {
            refreshing.set(false);
            throw e;
        }
    }

    public void invalidate() {
        loaded = false;
    }

    public int size() {
        return snapshot.length;
    }

    private boolean isStale() {
        return !loaded || clock.get() - refreshedAt >= ttlMs;
    }

    private void load() {
        OfflinePlayer[] players = loader.get();
        if(players == null) {
            players = EMPTY;
        }

        warmNames(players);

        snapshot = players;
        refreshedAt = clock.get();
        loaded = true;
    }

    private void warmNames(OfflinePlayer[] players) {
        long deadline = clock.get() + NAME_WARM_BUDGET_MS;

        for(OfflinePlayer player : players) {
            if(clock.get() >= deadline) {
                return;
            }

            try {
                player.getName();
            } catch(Exception ignored) {
            }
        }
    }
}
