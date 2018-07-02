package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import infinitespire.InfiniteSpire;

public class AbstractMonsterEverythingPatch {
	@SpirePatch(cls="com.megacrit.cardcrawl.monsters.AbstractMonster", method="die", paramtypes={"boolean"})
	public static class Die {
		public static void Prefix(AbstractMonster __instance, boolean trigger) {
			InfiniteSpire.logger.info(__instance.name + " died!");
			int pointsToGain = 0;
			
			switch(__instance.type) {
			case BOSS:
				pointsToGain = 300;
				break;
			case ELITE:
				pointsToGain = 100;
				break;
			case NORMAL:
			default:
				pointsToGain = 10;
				break;
			}
			
			InfiniteSpire.points += pointsToGain;
		}
	}
}
