package infinitespire.quests;

import java.awt.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.BaseMod;
import basemod.interfaces.RelicGetSubscriber;
import infinitespire.InfiniteSpire;

public class FetchQuest extends Quest implements RelicGetSubscriber{
	
	private String relicId;
	private AbstractRelic.RelicTier rarity;

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
		this.rarity = randRelic.tier;
		
		String relicID = randRelic.relicId;
		
		builder.append(Quest.createIDWithoutData(this.getClass().getName(), 1, 0, Color.CYAN));
		builder.append("-" + relicID);
		
		return builder.toString();
	}

	@Override
	protected void giveReward() {
		int silverGain = 0;
		
		switch(rarity) {
			
		case COMMON:
			silverGain = 100;
			break;
		case UNCOMMON:
			silverGain = 200;
			break;
		case RARE:
			silverGain = 300;
			break;
			
		case BOSS:
		case SHOP:
		case SPECIAL:
		case STARTER:
		default:
			silverGain = 100;
			break;
		
		}
		
		silverGain = MathUtils.round(silverGain * AbstractDungeon.merchantRng.random(0.95f, 1.05f));
		
		InfiniteSpire.points += silverGain;
		CardCrawlGame.sound.play("GOLD_GAIN");
	}

	@Override
	public void receiveRelicGet(AbstractRelic relic) {
		if(relic.relicId.equals(this.relicId)) {
			this.incrementQuestSteps();
		}
	}
}
