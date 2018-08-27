package infinitespire.quests.endless;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.monsters.Guardian;
import infinitespire.quests.SlayQuest;

public class EndlessQuestPart3 extends SlayQuest {
    public EndlessQuestPart3() {
        this.id = EndlessQuestPart3.class.getName();
        this.monster = Guardian.ID;
        this.color = new Color(0.75f, 0.0f, 1.0f, 1.0f);
        this.type = QuestType.BLUE;
        this.rarity = QuestRarity.SPECIAL;
        this.maxSteps = 1;
    }

    @Override
    public void giveReward() {
        //special unlock that changes things
    }

    @Override
    public Texture getTexture() {
        return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/nightmare.png");
    }

    @Override
    public String getTitle() {
        return "Kill ??? [Unimplemented]";
    }

    @Override
    public String getRewardString() {
        return "???";
    }

    @Override
    public Quest createNew() {
        return this;
    }

    @Override
    public Quest getCopy() {
        return new EndlessQuestPart3();
    }
}
