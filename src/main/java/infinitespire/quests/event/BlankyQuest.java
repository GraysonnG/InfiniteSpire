package infinitespire.quests.event;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.RegalPillow;
import infinitespire.abstracts.Quest;
import infinitespire.helpers.QuestHelper;
import infinitespire.interfaces.IAutoQuest;
import infinitespire.relics.BlanksBlanky;

public class BlankyQuest extends Quest implements IAutoQuest {

    public static final String ID = BlankyQuest.class.getName();
    public static final Color COLOR = new Color(0f,0.8f,1f,1f);
    public int heal;

    public BlankyQuest() {
        super(ID, COLOR, 1, QuestType.BLUE, QuestRarity.SPECIAL);
    }

    @Override
    public boolean shouldBegin() {
       return AbstractDungeon.player.hasRelic(RegalPillow.ID) && !AbstractDungeon.player.hasRelic(BlanksBlanky.ID);
    }

    @Override
    public void giveReward() {
        AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, heal));
    }

    @Override
    public Quest createNew() {
        this.heal = QuestHelper.makeRandomCost(AbstractDungeon.player.maxHealth / 5);
        return this;
    }

    @Override
    public void update(){
       if(AbstractDungeon.player.hasRelic(BlanksBlanky.ID) && !this.isCompleted()) {
           this.incrementQuestSteps();
       }
    }

    @Override
    public boolean autoClaim() {
        return true;
    }

    @Override
    public String getRewardString() {
        return "Heal " + heal + " HP";
    }

    @Override
    public String getTitle() {
        return "Get a Blanky.";
    }

    @Override
    public Quest getCopy() {
        return new BlankyQuest();
    }
}
