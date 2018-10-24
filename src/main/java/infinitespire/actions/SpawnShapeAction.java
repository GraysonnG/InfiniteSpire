package infinitespire.actions;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.monsters.MassOfShapes;

public class SpawnShapeAction extends AbstractGameAction {

	private int slot;
	private MassOfShapes boss;

	public SpawnShapeAction(int slot, MassOfShapes boss) {
		this.slot = slot;
		this.boss = boss;
	}

	@Override
	public void update() {
		AbstractMonster monster;

		Vector2 minPos = getMinionPosBySlotNum(slot);

		monster = MonsterHelper.getAncientShape(minPos.x, minPos.y);
		monster.usePreBattleAction();
		monster.useUniversalPreBattleAction();


		AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(monster, true));
		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(monster));

		boss.addMinion(monster);

		this.isDone = true;
	}

	private Vector2 getMinionPosBySlotNum(int slotNum){
		Vector2 pos = new Vector2();

		switch(slotNum){
			case 0:
				pos.x = -360f;
				pos.y = 10f;
				break;
			case 1:
				pos.x = -535f;
				pos.y = 25f;
				break;
			case 2:
				pos.x = -400f;
				pos.y = 185f;
				break;
		}

		return pos;
	}
}
