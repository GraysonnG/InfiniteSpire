package infinitespire.crossover;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.crossover.cards.DarkHarmony;
import infinitespire.patches.CardColorEnumPatch;

public class BardCrossover {
	public static void loadBardBlackCards() {
		BaseMod.addCard(new DarkHarmony());
	}

	public static void removeBardBlackCards() {
		if(AbstractDungeon.player != null && !(AbstractDungeon.player instanceof Bard)) {
			BaseMod.removeCard(DarkHarmony.ID, CardColorEnumPatch.CardColorPatch.INFINITE_BLACK);
		} else {
			loadBardBlackCards();
		}
	}
}
