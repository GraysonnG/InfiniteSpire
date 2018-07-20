package infinitespire.quests;

import java.awt.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.relics.SpiritPoop;

import basemod.BaseMod;
import basemod.interfaces.RelicGetSubscriber;
import infinitespire.InfiniteSpire;

public class FetchQuest extends Quest implements RelicGetSubscriber{
	
	public String relicId;

	public FetchQuest(String id) {
		super(id);
		
		BaseMod.subscribe(this);
		
		relicId = this.id.split("-")[4];
	}
	
	public FetchQuest() {
		this(null);
	}
	
	@Override
	protected void preInitialize() {
	
	}

	@Override
	protected String generateID() {
		StringBuilder builder = new StringBuilder();
		
		AbstractRelic randRelic = AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier());
		
		randRelic = AbstractDungeon.miscRng.randomBoolean(0.02f) ? new SpiritPoop() : randRelic;
		
		String relicID = randRelic.relicId;
		this.relicId = relicID;
		
		
		builder.append(Quest.createIDWithoutData(this.getClass().getName(), 1, 0, Color.CYAN));
		builder.append("-" + relicID);
		builder.append("-" + this.getCost(randRelic.tier.toString()));
		
		return builder.toString();
	}

	@Override
	protected void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		InfiniteSpire.points += cost;
	}

	@Override
	public void receiveRelicGet(AbstractRelic relic) {
		if(relic.relicId.equals(this.relicId)) {
			this.incrementQuestSteps();
		}
	}

	@Override
	public String getTitle() {
		return "Obtain " + RelicLibrary.getRelic(relicId).name;
	}

	@Override
	public String getRewardString() {
		return this.cost + "s";
	}

	@Override
	public int getCost(String string) {
		int silverGain = 0;
		
		switch(string) {
			
		case "COMMON":
			silverGain = 100;
			break;
		case "UNCOMMON":
			silverGain = 200;
			break;
		case "RARE":
			silverGain = 300;
			break;
		case "SPECIAL":
			return -1; 
		case "BOSS":
		case "SHOP":
		case "STARTER":
		default:
			silverGain = 100;
			break;
		
		}
		
		if(relicId.equals(Circlet.ID)) {
			silverGain = 50;
		}
		
		silverGain = MathUtils.round(silverGain * AbstractDungeon.merchantRng.random(0.95f, 1.05f));
		return silverGain;
	}
}
