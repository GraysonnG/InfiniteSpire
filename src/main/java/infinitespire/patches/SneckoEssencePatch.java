package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class SneckoEssencePatch {
	@SpirePatch(clz = AbstractGameAction.class, method=SpirePatch.CLASS)
	public static class Field {
		public static SpireField<Boolean> isSnecked = new SpireField<>(() -> false);
	}
}
