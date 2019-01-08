package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class RewardItemTypeEnumPatch {
	@SpireEnum
	public static RewardItem.RewardType QUEST;
	@SpireEnum
	public static RewardItem.RewardType BLACK_CARD;
	@SpireEnum
	public static RewardItem.RewardType INTEREST;
}
