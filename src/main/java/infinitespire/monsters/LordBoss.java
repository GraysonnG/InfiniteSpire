package infinitespire.monsters;

import com.badlogic.gdx.graphics.Colors;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.blights.Shield;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.interfaces.RageBarMonster;
import infinitespire.ragebar.RageBar;

import java.util.function.Consumer;

public abstract class LordBoss extends AbstractMonster implements RageBarMonster {

	protected RageBar rageBar;
	protected int turn, max_hp;

	public LordBoss(String name, String id, int baseMaxHp, float hb_x, float hb_y, float hb_w, float hb_h, float maxRageBar,Consumer<AbstractCreature> onRageFill) {
		super(name, id, baseMaxHp, hb_x, hb_y, hb_w, hb_h, null);
		rageBar = new RageBar(this, maxRageBar, Colors.get(InfiniteSpire.GDX_INFINITE_PURPLE_NAME), onRageFill);
		this.type = EnemyType.BOSS;
	}

	@Override
	public void usePreBattleAction() {
		//load music
		InfiniteSpire.lordBackgroundEffect.beginEffect();
	}

	protected void doHealthScaling(int baseMaxHp) {
		int scaling = 0;

		if(AbstractDungeon.player.hasBlight(Shield.ID)) {
			scaling = AbstractDungeon.player.getBlight(Shield.ID).counter - 2;
			if(scaling < 0) scaling = 0;
		}

		this.maxHealth = baseMaxHp * (int) Math.pow(2, scaling);
		this.currentHealth = baseMaxHp * (int) Math.pow(2, scaling);
		this.max_hp = baseMaxHp * (int) Math.pow(2, scaling);
	}

	protected void doAction(AbstractGameAction action) {
		AbstractDungeon.actionManager.addToBottom(action);
	}

	@Override
	public RageBar getRageBar() {
		return this.rageBar;
	}
}
