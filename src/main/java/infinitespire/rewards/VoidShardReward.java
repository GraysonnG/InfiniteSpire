package infinitespire.rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Reward;
import infinitespire.patches.RewardItemTypeEnumPatch;

public class VoidShardReward extends Reward {

	private static final Texture ICON = InfiniteSpire.Textures.getUITexture("topPanel/avhari/voidShard.png");
	private static final UIStrings STRINGS = CardCrawlGame.languagePack.getUIString("VoidShard");

	public int amountOfShards;

	public VoidShardReward(){
		this(1);
	}

	public VoidShardReward(int amount) {
		super(ICON, makeDescription(amount), RewardItemTypeEnumPatch.VOID_SHARD);
		this.amountOfShards = amount;
	}

	public static String makeDescription(int amount) {
		if(amount > 1) {
			return amount + STRINGS.TEXT[4];
		}
		return amount + STRINGS.TEXT[3];
	}

	@Override
	public boolean claimReward() {
		InfiniteSpire.gainVoidShards(amountOfShards);
		return true;
	}
}
