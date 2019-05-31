package com.atherys.quests.api.quest.modifiers;

import com.atherys.quests.api.quest.Quest;
import com.atherys.quests.api.quester.Quester;
import com.google.gson.annotations.Expose;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Timeable {
    @Expose
    private int seconds;

    private LocalDateTime timeStarted;

    private Consumer<Quester> onComplete;

    public Timeable(int seconds, Consumer<Quester> onComplete) {
        this.seconds = seconds;
        this.onComplete = onComplete;
    }

    public void startTiming() {
        this.timeStarted = LocalDateTime.now();
    }

    @Nullable
    public LocalDateTime getTimeStarted() {
        return timeStarted;
    }

    public int getSeconds() {
        return seconds;
    }

    public Optional<Consumer<Quester>> onComplete() {
        return Optional.ofNullable(onComplete);
    }
}
