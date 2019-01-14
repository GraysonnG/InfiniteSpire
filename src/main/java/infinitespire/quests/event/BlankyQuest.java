package infinitespire.quests.event;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.RegalPillow;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.helpers.QuestHelper;
import infinitespire.interfaces.IAutoQuest;
import infinitespire.relics.BlanksBlanky;

public class BlankyQuest extends Quest implements IAutoQuest {

    public static final String ID = BlankyQuest.class.getName();
    public static final Color COLOR = new Color(0f,0.8f,1f,1f);
    public static boolean isAbandoned;
    public int heal;

    public BlankyQuest() {
        super(ID, COLOR, 1, QuestType.BLUE, QuestRarity.SPECIAL);
    }

    @Override
    public boolean shouldBegin() {
       return !isAbandoned && AbstractDungeon.player.hasRelic(RegalPillow.ID) && !AbstractDungeon.player.hasRelic(BlanksBlanky.ID);
    }

    @Override
    public void giveReward() {
        AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, heal));
    }

    @Override
    public Texture getTexture() {
        return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/event.png");
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
    public void removeQuest() {
        super.removeQuest();
        if(this.abandon){
            isAbandoned = true;
        }
    }

    @Override
    public boolean autoClaim() {
        return true;
    }

    @Override
    public String getRewardString() {
      if (Settings.language == Settings.GameLanguage.FRA){
          return "Soignez-vous " + heal + " PV";
      } else {
          return "Heal " + heal + " HP";
      }
    }

    @Override
    public String getTitle() {
      if (Settings.language == Settings.GameLanguage.FRA){
        return "Obtenez une couverture.";
      } else {
        return "Get a Blanky.";
      }
    }

    @Override
    public Quest getCopy() {
        return new BlankyQuest();
    }
}
