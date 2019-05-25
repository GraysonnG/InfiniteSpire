package infinitespire.abstracts;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import infinitespire.InfiniteSpire;

public abstract class Reward extends CustomReward {
	protected static final UIStrings rewardStrings = CardCrawlGame.languagePack.getUIString(InfiniteSpire.createID("RewardItems"));

	public Reward(Texture texture, String text, RewardItem.RewardType type){
		super(texture, text, type);
	}
}
