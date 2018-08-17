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

    public AddQuestAction(Quest questToAdd){
        quests = new ArrayList<>();
        quests.add(questToAdd);

    }

    public AddQuestAction(Collection<Quest> questsToAdd){
        quests = new ArrayList<>();
        quests.addAll(questsToAdd);
    }

    @Override
    public void update() {
        for(Quest quest : quests) {
            if (!InfiniteSpire.questLog.hasQuest(quest) && InfiniteSpire.questLog.getAmount(quest.type) < 7) {
                InfiniteSpire.questLog.add(quest.createNew());
                AbstractDungeon.topLevelEffects.add(new QuestLogUpdateEffect());
            }
        }

        this.isDone = true;
    }
}

