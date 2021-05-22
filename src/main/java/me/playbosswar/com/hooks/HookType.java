package me.playbosswar.com.hooks;

public enum HookType {
    PAPI("Placeholder API", new String[]{ "",
            "§7CommandTimer hooks into this integration to",
            "§7use task values like the next execution of",
            "§7a task in another plugin that integrations with",
            "§7PlaceholderAPI",
            "",
            "§bAvailable placeholders:",
            "",
            "§7  - §e%commandtimer_taskname_seconds%: §7Show the amount of",
            "§7    seconds between each execution (only works if seconds are set)",
            "",
            "§7  - §e%commandtimer_timername_nextExecution%: §7Show when the next",
            "§7     execution of a task will be",
            "",
            "§7You can use §bsecondsFormat §7and §bnextExecutionFormat §7if",
            "§7you want the amount in hours, minutes, seconds"
    }),
    METRICS("Metrics", new String[]{ "",
            "§7Metrics is an integration to keep track of certain",
            "§7server statistics like player count, what machine",
            "§7you are using,...",
    });

    private String displayName;
    private String[] loreDescription;

    HookType(String displayName, String[] loreDescription) {
        this.displayName = displayName;
        this.loreDescription = loreDescription;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String[] getLoreDescription() {
        return loreDescription;
    }
}
