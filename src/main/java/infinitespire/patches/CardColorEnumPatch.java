package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType;

public class CardColorEnumPatch {
	
	public static class CardColorPatch {
		@SpireEnum
		public static CardColor INFINITE_BLACK;
	}
	
	public static class LibColorPatch {
		@SpireEnum
		public static LibraryType INFINITE_BLACK;
	}
}
