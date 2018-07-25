package infinitespire.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.powers.SpireBlightPower;

public class Nightmare extends AbstractMonster {
	
	public static final String ID = "Nightmare";
	public static final String NAME = "Nightmare";
	public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Nightmare");
	public static final String[] MOVES = Nightmare.monsterStrings.MOVES;
	public static final String[] DIALOG = Nightmare.monsterStrings.DIALOG;
	private int attackDmg;
	private int slamDmg;
	private int blockCount;
	private int turnCount;
	private int damageTakenLastTurn;
	
	public Nightmare() {
		super(NAME, ID, 350, (1920f / 4f) * 3, 1080f / 2f, 160f, 300f, null);
		this.type = EnemyType.ELITE;
		this.dialogX = -160.0f * Settings.scale;
		this.dialogY = 40f * Settings.scale;
		this.img = InfiniteSpire.getTexture("img/monsters/nightmare.png");
		
		this.attackDmg = 5;
		this.slamDmg = 25;
		this.blockCount = 20;
		this.turnCount = 1;
		
		this.damage.add(new DamageInfo(this, this.attackDmg));
		this.damage.add(new DamageInfo(this, this.slamDmg));
		
	}

	@Override
	protected void getMove(int num) {
		if(turnCount == 1) {
			this.setMove(Nightmare.MOVES[0], (byte) 1, Intent.STRONG_DEBUFF);
			return;
		}
		if (num < 40) {
			if(damageTakenLastTurn < 100) {
				this.setMove(Nightmare.MOVES[2], (byte) 3, Intent.ATTACK_BUFF);
			} else {
				this.setMove(Nightmare.MOVES[1], (byte) 2, Intent.ATTACK);
			}
		}else {
			this.setMove(Nightmare.MOVES[3], (byte) 4, Intent.DEFEND);
		}
		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	

	@Override
	public void damage(DamageInfo damage) {
		super.damage(damage);
		damageTakenLastTurn = damage.output;
	}

	@Override
	public void takeTurn() {
		switch(this.nextMove) {
		case 1:
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new SpireBlightPower(AbstractDungeon.player)));
			break;
		case 2:
			//multihit but no buff
			break;
		case 3:
			//big hit and buff
			break;
		case 4:
			//defend
			break;
		}
		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}

}
