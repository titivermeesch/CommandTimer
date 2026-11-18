package me.playbosswar.com.utils;

import io.sentry.SentryEvent;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Sentry's uncaught exception handler is JVM-wide, so without this filter every crash on the server
 * (other plugins, Netty, the server itself) is reported to the CommandTimer project.
 */
public final class SentryEventFilter {
    private static final String PLUGIN_PACKAGE = "me.playbosswar.com.";
    // Sentry is shaded under the plugin package; its own frames must not count as ours.
    private static final String RELOCATED_SENTRY_PACKAGE = "me.playbosswar.com.commandtimer.sentry.";

    private SentryEventFilter() {
    }

    public static SentryEvent filter(SentryEvent event) {
        Throwable throwable = event.getThrowable();
        if(throwable == null) {
            return event;
        }
        return involvesPlugin(throwable) ? event : null;
    }

    static boolean involvesPlugin(Throwable throwable) {
        Set<Throwable> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        for(Throwable t = throwable; t != null && seen.add(t); t = t.getCause()) {
            for(StackTraceElement element : t.getStackTrace()) {
                String className = element.getClassName();
                if(className.startsWith(PLUGIN_PACKAGE) && !className.startsWith(RELOCATED_SENTRY_PACKAGE)) {
                    return true;
                }
            }
        }
        return false;
    }
}
