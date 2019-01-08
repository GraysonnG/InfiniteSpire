package infinitespire.rewards;

import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import infinitespire.patches.RewardItemTypeEnumPatch;

public class InterestReward extends CustomReward {

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
		return "Collect " + amount + " gold in interest";
	}
}
