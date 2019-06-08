package infinitespire.quests.event;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.mod.replay.monsters.replay.CaptainAbe;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import infinitespire.AutoLoaderIgnore;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.helpers.QuestHelper;
import infinitespire.interfaces.IAutoQuest;
import infinitespire.quests.SlayQuest;
import infinitespire.relics.EvilPickle;

@AutoLoaderIgnore
public class CaptainAbeQuest extends SlayQuest implements IAutoQuest {
    public int gold;
    public static final String ID = CaptainAbeQuest.class.getName();
    public static final Color COLOR = new Color(0.2f, 0.2f, 0.2f, 1f);
    private static boolean isAbandoned = false;

    public CaptainAbeQuest() {
        this.id = CaptainAbeQuest.class.getName();
        this.color = COLOR;
        this.type = QuestType.BLUE;
        this.rarity = QuestRarity.SPECIAL;
        this.maxSteps = 1;
        this.monster = CaptainAbe.ID;
    }

    @Override
    public boolean shouldBegin() {
        if(isAbandoned || AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) return false;
        return AbstractDungeon.bossKey.equals("Pondfish");
    }

    @Override
    public void giveReward() {
        EvilPickle pickle = new EvilPickle();
        pickle.instantObtain();
    }

    @Override
    public Texture getTexture() {
        return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/boss.png");
    }

    @Override
    public Quest createNew() {
        gold = QuestHelper.makeRandomCost(500);
        return this;
    }

    @Override
    public String getRewardString() {
        return "Receive a Special Relic";
    }

    @Override
    public String getTitle() {
        return "Defeat Captain Abe";
    }

    @Override
    public Quest getCopy() {
        return new CaptainAbeQuest();
    }
}
