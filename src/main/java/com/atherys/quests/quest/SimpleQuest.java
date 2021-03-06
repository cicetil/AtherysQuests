package com.atherys.quests.quest;

import com.atherys.quests.AtherysQuests;
import com.atherys.quests.api.objective.Objective;
import com.atherys.quests.api.quest.AbstractQuest;
import com.atherys.quests.api.quest.Quest;
import com.atherys.quests.api.quester.Quester;
import com.atherys.quests.api.requirement.Requirement;
import com.atherys.quests.api.reward.Reward;
import com.atherys.quests.event.quest.QuestCompletedEvent;
import com.atherys.quests.event.quest.QuestStartedEvent;
import com.atherys.quests.event.quest.SimpleQuestProgressEvent;
import com.atherys.quests.util.CopyUtils;
import com.atherys.quests.views.AnyQuestView;
import com.atherys.quests.views.QuestView;
import com.google.gson.annotations.Expose;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link Quest} which allows the player to complete its objectives in any order they wish.
 */
public class SimpleQuest extends AbstractQuest<SimpleQuest> {

    @Expose
    private List<Objective> objectives = new ArrayList<>();

    protected SimpleQuest(String id, int version) {
        super(id, version);
    }

    protected SimpleQuest(SimpleQuest quest) {
        super(quest);
        this.objectives = CopyUtils.copyList(quest.getObjectives());
    }

    public SimpleQuest(String id, Text name, Text description, int version) {
        super(id, name, description, version);
    }

    protected void setDescription(Text description) {
        super.description = description;
    }

    protected void setName(Text name) {
        this.name = name;
    }

    @Override
    public List<Requirement> getRequirements() {
        return requirements;
    }

    protected <T extends Requirement> void addRequirement(T requirement) {
        if (!requirements.contains(requirement)) requirements.add(requirement);
    }

    @Override
    public List<Objective> getObjectives() {
        return objectives;
    }

    protected <T extends Objective> void addObjective(T objective) {
        if (!objectives.contains(objective)) objectives.add(objective);
    }

    @Override
    public List<Reward> getRewards() {
        return rewards;
    }

    protected <T extends Reward> void addReward(T reward) {
        if (!rewards.contains(reward)) rewards.add(reward);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void notify(Event event, Quester quester) {
        // if the Quest hasn't been completed yet
        if (!isComplete()) {

            // if the Quest hasn't been started yet ( this is the first notification )
            if (!isStarted()) {
                // set it as started
                this.started = true;
                Sponge.getEventManager().post(new QuestStartedEvent(this, quester));
            }

            // updated completed status based on the status of the objectives
            for (Objective objective : getObjectives()) {
                if (objective.isComplete()) continue; // if the objective has already been completed, skip it

                objective.notify(event, quester); // notify the objective

                if (objective.isComplete()) { // if the objective is completed after being notified
                    AtherysQuests.getInstance().getQuestMessagingService().info(quester, "You have completed an objective for the quest \"", this.getName(), "\""); // tell the player they have completed another objective of the Quest

                    Sponge.getEventManager().post(new SimpleQuestProgressEvent(this, objective, quester));

                    // update Quest complete status by iterating every objective, checking it's complete status, and concatenate with this.complete
                    this.complete = true;

                    for (Objective objective1 : getObjectives()) {
                        this.complete = this.complete && objective1.isComplete();
                    }
                }
            }

            if (isComplete()) Sponge.getEventManager().post(new QuestCompletedEvent(this, quester));
        }
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public SimpleQuest copy() {
        return new SimpleQuest(this);
    }

    @Override
    public QuestView createView() {
        return new AnyQuestView<>(this);
    }
}
