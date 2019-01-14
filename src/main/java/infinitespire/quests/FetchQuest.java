package infinitespire.quests;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.relics.Circlet;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.helpers.QuestHelper;

public class FetchQuest extends Quest {

	public static final String ID = FetchQuest.class.getName();
	private static final Color COLOR = new Color(0f, 1f, 0.75f, 1f);
	private static final QuestType TYPE = QuestType.GREEN;
	private static final int MAX_STEPS = 1;
	public int cost;
	public transient AbstractRelic relic;
	public String relicID;


	public FetchQuest() {
		super(ID, COLOR, MAX_STEPS, TYPE, QuestRarity.COMMON);
	}

	@Override
	public void update(){
		if(!this.isCompleted() && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(this.relicID)) {
			this.incrementQuestSteps();
		}
	}

	@Override
	public void render(SpriteBatch sb){
		if(this.isHovered){
			AbstractRelic r = RelicLibrary.getRelic(relicID);
			if(r != null){
				r.renderTip(sb);
			}
		}
	}

	@Override
	public Texture getTexture() {
		return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/chest.png");
		//return RelicLibrary.getRelic(relicID).img;
	}

	@Override
	public void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		AbstractDungeon.player.gainGold(cost);
	}

	@Override
	public String getTitle() {
		if(this.relic == null && this.relicID != null) {
			this.relic = RelicLibrary.getRelic(relicID);
		}
		if (Settings.language == Settings.GameLanguage.FRA){
				return "Obtenez " + relic.name;
		} else {
				return "Obtain " + relic.name;
		}

	}

	@Override
	public String getRewardString() {
		if (Settings.language == Settings.GameLanguage.FRA){
				return this.cost + "or";
		} else {
				return this.cost + "g";
		}

	}

	public int getCost(RelicTier tier) {
		int goldGain = 0;

		switch(tier) {

		case COMMON:
			goldGain = 75;
			break;
		case UNCOMMON:
			goldGain = 150;
			break;
		case RARE:
			goldGain = 225;
			break;
		case SPECIAL:
			return 1;
		case BOSS:
		case SHOP:
		case STARTER:
		default:
			goldGain = 75;
			break;
		}

		if(relic.relicId.equals(Circlet.ID)) {
			goldGain = 50;
		}

		goldGain = QuestHelper.makeRandomCost(goldGain);
		return goldGain;
	}

	@Override
	public Quest createNew() {
		this.relic = QuestHelper.returnRandomRelic(AbstractDungeon.returnRandomRelicTier());
		this.relicID = relic.relicId;
		this.cost = this.getCost(relic.tier);
		return this;
	}

	@Override
	public Quest getCopy() {
		return new FetchQuest();
	}
}
