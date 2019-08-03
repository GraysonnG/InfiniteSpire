package infinitespire.rewards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import infinitespire.abstracts.Reward;
import infinitespire.patches.RewardItemTypeEnumPatch;

public class InterestReward extends Reward {

	public int amount;

	public InterestReward(int amount) {
		super(ImageMaster.UI_GOLD, getDescription(amount), RewardItemTypeEnumPatch.INTEREST);
		this.amount = amount;
	}

	@Override
	public boolean claimReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		AbstractDungeon.player.gainGold(this.amount);

		return true;
	}

	public static String getDescription(int amount){
		return rewardStrings.TEXT[1] + amount + rewardStrings.TEXT[2];
	}
}
