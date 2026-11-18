package me.playbosswar.com.utils;

import io.sentry.SentryEvent;
import io.sentry.exception.ExceptionMechanismException;
import io.sentry.protocol.Mechanism;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class SentryEventFilterTest {
    private static Throwable withStack(Throwable t, String... classNames) {
        StackTraceElement[] stack = new StackTraceElement[classNames.length];
        for(int i = 0; i < classNames.length; i++) {
            stack[i] = new StackTraceElement(classNames[i], "run", "X.java", 1);
        }
        t.setStackTrace(stack);
        return t;
    }

    private static SentryEvent uncaught(Throwable t) {
        return new SentryEvent(new ExceptionMechanismException(new Mechanism(), t, Thread.currentThread()));
    }

    @Test
    void keepsExceptionThrownFromPluginCode() {
        SentryEvent event = uncaught(withStack(new RuntimeException(),
                "java.util.ArrayList", "me.playbosswar.com.tasks.TaskRunner"));
        assertSame(event, SentryEventFilter.filter(event));
    }

    @Test
    void dropsExceptionFromAnotherPlugin() {
        SentryEvent event = uncaught(withStack(new RuntimeException(),
                "com.example.otherplugin.Foo", "io.netty.channel.Bar", "java.lang.Thread"));
        assertNull(SentryEventFilter.filter(event));
    }

    @Test
    void keepsWhenPluginFrameIsOnlyInCause() {
        Throwable cause = withStack(new IllegalStateException(), "me.playbosswar.com.utils.Files");
        SentryEvent event = uncaught(withStack(new RuntimeException(cause), "org.bukkit.craftbukkit.Scheduler"));
        assertSame(event, SentryEventFilter.filter(event));
    }

    @Test
    void ignoresRelocatedSentryFrames() {
        SentryEvent event = uncaught(withStack(new RuntimeException(),
                "me.playbosswar.com.commandtimer.sentry.UncaughtExceptionHandlerIntegration",
                "com.example.otherplugin.Foo"));
        assertNull(SentryEventFilter.filter(event));
    }

    @Test
    void keepsEventsWithoutThrowable() {
        SentryEvent event = new SentryEvent();
        assertSame(event, SentryEventFilter.filter(event));
    }
}
