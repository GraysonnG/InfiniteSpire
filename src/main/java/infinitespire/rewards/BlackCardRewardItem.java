package infinitespire.rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.abstracts.Reward;
import infinitespire.helpers.CardHelper;
import infinitespire.patches.RewardItemTypeEnumPatch;
import infinitespire.util.TextureLoader;

public class BlackCardRewardItem extends Reward {

	private static final Texture TEXTURE = TextureLoader.getTexture("img/infinitespire/ui/cardReward/blackreward.png");
	private static final String TEXT = rewardStrings.TEXT[0];

	public BlackCardRewardItem() {
		super(TEXTURE, TEXT, RewardItemTypeEnumPatch.BLACK_CARD);
		this.cards = CardHelper.getBlackRewardCards();
	}

	@Override
	public boolean claimReward() {
		if(AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
			AbstractDungeon.cardRewardScreen.open(this.cards, this, "Pick A Black Card.");
			AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
		}
		return false;
	}
}
