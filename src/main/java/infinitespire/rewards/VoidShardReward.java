package infinitespire.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import infinitespire.InfiniteSpire;
import infinitespire.patches.RewardItemTypeEnumPatch;

public class VoidShardReward extends CustomReward {

	private static final Texture ICON = InfiniteSpire.Textures.getUITexture("topPanel/avhari/voidShard.png");

	public int amountOfShards;

	public VoidShardReward(){
		this(1);
	}

	public VoidShardReward(int amount) {
		super(ICON, amount + " Void Shard", RewardItemTypeEnumPatch.VOID_SHARD);
		this.amountOfShards = amount;
	}

	@Override
	public boolean claimReward() {
		CardCrawlGame.sound.play("RELIC_DROP_CLINK");
		InfiniteSpire.voidShardCount += amountOfShards;
		return true;
	}
}
