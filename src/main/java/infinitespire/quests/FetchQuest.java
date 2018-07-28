package infinitespire.quests;

import java.awt.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.relics.SpiritPoop;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import infinitespire.InfiniteSpire;

public class FetchQuest extends Quest implements PostUpdateSubscriber{
	
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
		
		AbstractRelic randRelic = returnRandomRelic(AbstractDungeon.returnRandomRelicTier());
		RelicLibrary.add(randRelic);
		
		randRelic = AbstractDungeon.miscRng.randomBoolean(0.02f) ? (new SpiritPoop()) : randRelic;
		
		String relicID = this.filterRelicID(randRelic.relicId);
		this.relicId = relicID;
		
		
		builder.append(Quest.createIDWithoutData(this.getClass().getName(), 1, 0, Color.CYAN));
		builder.append("-" + relicID);
		builder.append("-" + this.getCost(randRelic.tier.toString()));
		
		return builder.toString();
	}
	
	private static AbstractRelic returnRandomRelic(RelicTier tier) {
		String key = Circlet.ID;
		AbstractRelic retVal = new Circlet();
		switch(tier) {
		case BOSS:
			key = AbstractDungeon.bossRelicPool.get(AbstractDungeon.relicRng.random(AbstractDungeon.bossRelicPool.size() - 1));
			break;
		case COMMON:
			key = AbstractDungeon.commonRelicPool.get(AbstractDungeon.relicRng.random(AbstractDungeon.commonRelicPool.size() - 1));
			break;
		case RARE:
			key = AbstractDungeon.rareRelicPool.get(AbstractDungeon.relicRng.random(AbstractDungeon.rareRelicPool.size() - 1));
			break;
		case UNCOMMON:
			key = AbstractDungeon.uncommonRelicPool.get(AbstractDungeon.relicRng.random(AbstractDungeon.uncommonRelicPool.size() - 1));
			break;
		default:
			key = Circlet.ID;
			break;
		}	
		
		retVal = RelicLibrary.getRelic(key);
		
		return retVal;
	}

	@Override
	public void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		InfiniteSpire.points += cost;
	}

	@Override
	public String getTitle() {
		return "Obtain " + RelicLibrary.getRelic(unFilterRelicID(relicId)).name;
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
			return 1; 
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

	@Override
	public void receivePostUpdate() {
		if(!this.isCompleted() && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(unFilterRelicID(this.relicId))) {
			this.incrementQuestSteps();
		}
	}
	
	private String unFilterRelicID(String relicID) {
		String retVal = relicID;
		
		for(AbstractRelic relic : RelicLibrary.blueList) {
			if(relicID.equals(filterRelicID(relic.relicId))) {
				return relic.relicId;
			}
		}
		
		for(AbstractRelic relic : RelicLibrary.redList) {
			if(relicID.equals(filterRelicID(relic.relicId))) {
				return relic.relicId;
			}
		}
		
		for(AbstractRelic relic : RelicLibrary.greenList) {
			if(relicID.equals(filterRelicID(relic.relicId))) {
				return relic.relicId;
			}
		}
		
		for(AbstractRelic relic : RelicLibrary.bossList) {
			if(relicID.equals(filterRelicID(relic.relicId))) {
				return relic.relicId;
			}
		}
		
		for(AbstractRelic relic : RelicLibrary.rareList) {
			if(relicID.equals(filterRelicID(relic.relicId))) {
				return relic.relicId;
			}
		}
		
		for(AbstractRelic relic : RelicLibrary.uncommonList) {
			if(relicID.equals(filterRelicID(relic.relicId))) {
				return relic.relicId;
			}
		}
		
		for(AbstractRelic relic : RelicLibrary.commonList) {
			if(relicID.equals(filterRelicID(relic.relicId))) {
				return relic.relicId;
			}
		}
		
		for(AbstractRelic relic : RelicLibrary.shopList) {
			if(relicID.equals(filterRelicID(relic.relicId))) {
				return relic.relicId;
			}
		}
		
		for(AbstractRelic relic : RelicLibrary.specialList) {
			if(relicID.equals(filterRelicID(relic.relicId))) {
				return relic.relicId;
			}
		}
		
		
		return retVal;
	}
	
	private String filterRelicID(String relicID) {
		String retVal = "" + relicID;
		
		retVal.replace("-", "_");
		retVal.replace(":", "|");
		
		return retVal;
	}
}
