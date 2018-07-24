package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ScreenStatePatch {
	@SpireEnum
	public static AbstractDungeon.CurrentScreen PERK_SCREEN;
	@SpireEnum
	public static AbstractDungeon.CurrentScreen QUEST_LOG_SCREEN;
}
