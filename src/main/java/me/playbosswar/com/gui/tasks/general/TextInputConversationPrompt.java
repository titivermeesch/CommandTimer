package me.playbosswar.com.gui.tasks.general;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.Callback;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextInputConversationPrompt extends StringPrompt {
    private final String title;
    LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Callback<String> callback;

    public TextInputConversationPrompt(String title, Callback<String> callback) {
        super();
        this.title = title;
        this.callback = callback;
    }

    public TextInputConversationPrompt(LanguageKey languageKey, Callback<String> callback) {
        super();
        this.title = languageManager.get(languageKey);
        this.callback = callback;
    }

    public TextInputConversationPrompt(Callback<String> callback) {
        super();
        this.title = languageManager.get(LanguageKey.TEXT_INPUT_DEFAULT);
        this.callback = callback;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return this.title;
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        callback.execute(input);
        return Prompt.END_OF_CONVERSATION;
    }
}
