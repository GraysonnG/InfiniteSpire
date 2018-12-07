package infinitespire.patches;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.relics.CursedDice;
import javassist.CtBehavior;

public class CursedDicePatch {

	@SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
	public static class AbstractDungeonPatches {
		@SpireInsertPatch(locator = DeathLocator.class)
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

		private static class DeathLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws Exception {
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getMonsters");

				return LineFinder.findInOrder(ctBehavior, matcher);
			}
		}
	}

	@SpirePatch(clz = Soul.class, method = "discard", paramtypez = {AbstractCard.class, boolean.class})
	public static class SoulCardPatches {
		@SpirePostfixPatch
		public static void moveToExhaustInsteadPatch(Soul __instance, AbstractCard card, boolean visualOnly) {
			if(AbstractDungeon.player.hasRelic(CursedDice.ID)) {
				CursedDice dice = (CursedDice) AbstractDungeon.player.getRelic(CursedDice.ID);
				dice.moveCardToExhaust(card, visualOnly);
			}
		}
	}
}
