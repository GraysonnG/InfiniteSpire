package infinitespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.effects.QuestLogUpdateEffect;

import java.util.ArrayList;
import java.util.Collection;

public class AddQuestAction extends AbstractGameAction{
    private ArrayList<Quest> quests;
    private boolean doEffect;

    public AddQuestAction(Quest questToAdd){
        quests = new ArrayList<>();
        quests.add(questToAdd);
        doEffect = true;

    }

    public AddQuestAction(Collection<Quest> questsToAdd){
        quests = new ArrayList<>();
        quests.addAll(questsToAdd);
        doEffect = true;
    }

    public AddQuestAction(Quest questToAdd, boolean doEffect){
        this(questToAdd);
        this.doEffect = doEffect;
    }

    public AddQuestAction(Collection<Quest> questsToAdd, boolean doEffect){
        this(questsToAdd);
        this.doEffect = doEffect;
    }

    @Override
    public void update() {
        for(Quest quest : quests) {
            if (!InfiniteSpire.questLog.hasQuest(quest) && InfiniteSpire.questLog.getAmount(quest.type) < 7) {
                InfiniteSpire.questLog.add(quest.createNew());
                if(doEffect) AbstractDungeon.topLevelEffects.add(new QuestLogUpdateEffect());
                InfiniteSpire.publishOnQuestAdded(quest);
            }
        }

        this.isDone = true;
    }
}

