package infinitespire.patches;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.relics.CursedDice;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
public class CursedDicePatch {

	@SpireInsertPatch(locator = Locator.class)
	public static SpireReturn CursedDiceCheck(AbstractPlayer player, DamageInfo info) {
		if(player.hasRelic(CursedDice.ID)) {
			if(player.getRelic(CursedDice.ID).counter > -2) {
				player.isDead = false;
				player.currentHealth = 0;
				player.getRelic(CursedDice.ID).onTrigger();
				return SpireReturn.Return(null);
			}
		}


		return SpireReturn.Continue();
	}

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctBehavior) throws Exception {
			Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getMonsters");

			return LineFinder.findInOrder(ctBehavior, matcher);
		}
	}
}
