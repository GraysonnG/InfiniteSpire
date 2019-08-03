package infinitespire.monsters;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import infinitespire.InfiniteSpire;

public class LordOfDawn extends LordBoss {
	public static final String ID = InfiniteSpire.createID("LordOfDawn");
	private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
	private static final int BASE_MAX_HP = 5000;

	public LordOfDawn() {
		super(monsterStrings.NAME, ID, BASE_MAX_HP, 0, 0, 300f, 300f, 20f,
			(me) -> {
				AbstractDungeon.actionManager.addToBottom(new HealAction(me, me, BASE_MAX_HP / 10));
			}
		);
	}

	@Override
	public void takeTurn() {

	}

	@Override
	protected void getMove(int i) {

	}

	private static class MoveBytes {

	}

	private static class MoveValues {

	}
}
