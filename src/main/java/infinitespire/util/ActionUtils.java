package infinitespire.util;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ActionUtils {

	public static void addActionSecondToTop(AbstractGameAction action) {
		if(AbstractDungeon.actionManager.actions.size() > 1) {
			AbstractDungeon.actionManager.actions.add(1, action);
		} else {
			AbstractDungeon.actionManager.addToBottom(action);
		}
	}
}
