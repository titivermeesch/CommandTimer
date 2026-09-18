package me.playbosswar.com.utils;

import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OfflinePlayerCacheTest {
    private AtomicLong now;
    private AtomicBoolean onServerThread;
    private AtomicInteger loadCount;
    private List<Runnable> asyncQueue;

    @BeforeEach
    void setUp() {
        now = new AtomicLong(1_000L);
        onServerThread = new AtomicBoolean(false);
        loadCount = new AtomicInteger(0);
        asyncQueue = new ArrayList<>();
    }

    private OfflinePlayerCache cacheOf(OfflinePlayer... players) {
        return new OfflinePlayerCache(() -> {
            loadCount.incrementAndGet();
            return players;
        }, onServerThread::get, asyncQueue::add, now::get, OfflinePlayerCache.DEFAULT_TTL_MS);
    }

    private void runQueuedAsyncWork() {
        List<Runnable> queued = new ArrayList<>(asyncQueue);
        asyncQueue.clear();
        queued.forEach(Runnable::run);
    }

    @Test
    void servesTheSameSnapshotUntilTheTtlExpires() {
        OfflinePlayer player = mock(OfflinePlayer.class);
        OfflinePlayerCache cache = cacheOf(player);

        assertArrayEquals(new OfflinePlayer[]{player}, cache.get());
        assertArrayEquals(new OfflinePlayer[]{player}, cache.get());
        assertEquals(1, loadCount.get(), "the player data directory should only be read once per TTL");

        now.addAndGet(OfflinePlayerCache.DEFAULT_TTL_MS);
        cache.get();

        assertEquals(2, loadCount.get(), "an expired snapshot should be reloaded");
    }

    @Test
    void neverReadsPlayersOnTheServerThread() {
        OfflinePlayer player = mock(OfflinePlayer.class);
        OfflinePlayerCache cache = cacheOf(player);
        onServerThread.set(true);

        assertEquals(0, cache.get().length, "a cold cache must not make the server thread wait for a load");
        assertEquals(0, loadCount.get());
        assertEquals(1, asyncQueue.size(), "the refresh should be handed to the async scheduler instead");

        onServerThread.set(false);
        runQueuedAsyncWork();

        assertEquals(1, loadCount.get());
        assertArrayEquals(new OfflinePlayer[]{player}, cache.get());
    }

    @Test
    void resolvesProfileNamesOffTheServerThread() {
        AtomicBoolean resolvedOnServerThread = new AtomicBoolean(false);
        OfflinePlayer player = mock(OfflinePlayer.class);
        when(player.getName()).thenAnswer(invocation -> {
            resolvedOnServerThread.compareAndSet(false, onServerThread.get());
            return "Notch";
        });

        OfflinePlayerCache cache = cacheOf(player);
        onServerThread.set(true);
        cache.get();

        verify(player, never()).getName();

        onServerThread.set(false);
        runQueuedAsyncWork();

        verify(player, atLeastOnce()).getName();
        assertFalse(resolvedOnServerThread.get(), "a Mojang profile lookup must never run on the server thread");
    }

    @Test
    void stopsWarmingNamesAfterTheTimeBudget() {
        OfflinePlayer slow = mock(OfflinePlayer.class);
        OfflinePlayer next = mock(OfflinePlayer.class);
        when(slow.getName()).thenAnswer(invocation -> {
            now.addAndGet(5_000L);
            return "Notch";
        });

        OfflinePlayerCache cache = cacheOf(slow, next);

        assertEquals(2, cache.get().length, "every player is still returned, only the warming is cut short");
        verify(slow).getName();
        verify(next, never()).getName();
    }

    @Test
    void onlySchedulesOneRefreshAtATime() {
        OfflinePlayerCache cache = cacheOf(mock(OfflinePlayer.class));
        onServerThread.set(true);

        cache.get();
        cache.get();
        cache.get();

        assertEquals(1, asyncQueue.size());
    }

    @Test
    void invalidateForcesAReload() {
        OfflinePlayerCache cache = cacheOf(mock(OfflinePlayer.class));
        cache.get();
        assertEquals(1, loadCount.get());

        cache.invalidate();
        cache.get();

        assertEquals(2, loadCount.get());
    }

    @Test
    void toleratesALoaderThatReturnsNothing() {
        OfflinePlayerCache cache = new OfflinePlayerCache(() -> null, onServerThread::get, asyncQueue::add, now::get,
                OfflinePlayerCache.DEFAULT_TTL_MS);

        assertEquals(0, cache.get().length);
        assertEquals(0, cache.size());
    }

    @Test
    void reportsNoServerThreadWhenNoServerIsRunning() {
        assertFalse(Tools.isServerThread(), "without a server there is no thread we could block");
    }
}
