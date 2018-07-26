package infinitespire.monsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import infinitespire.InfiniteSpire;
import infinitespire.powers.SpireBlightPower;

public class Nightmare extends AbstractMonster {
	
	public static final String ID = "Nightmare";
	public static final String NAME = "Nightmare";
	public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Nightmare");
	public static final String[] MOVES = Nightmare.monsterStrings.MOVES;
	public static final String[] DIALOG = Nightmare.monsterStrings.DIALOG;
	
	private int textureIndex = 0;
	
	private float spriteEffect;
	private boolean firstTurn = true;
	private int attackDmg;
	private int slamDmg;
	private int blockCount;
	private int damageTakenLastTurn;
	
	public Nightmare() {
		super(NAME, ID, 150, -0.0F, -10.0F, 160f, 300f, null);
		this.type = EnemyType.ELITE;
		this.dialogX = -160.0f * Settings.scale;
		this.dialogY = 40f * Settings.scale;
		this.img = InfiniteSpire.getTexture("img/monsters/nightmare/nightmare-1.png");
		this.setHp(150);
		
		this.attackDmg = 5;
		this.slamDmg = 25;
		this.blockCount = 20;
		
		
		this.damage.add(new DamageInfo(this, this.attackDmg));
		this.damage.add(new DamageInfo(this, this.slamDmg));
		
	}

	@Override
	protected void getMove(int num) {
		if(firstTurn) {
			this.setMove(Nightmare.MOVES[0], (byte) 1, Intent.STRONG_DEBUFF);
			this.firstTurn = false;
			return;
		}
		if (num > 20) {
			if(damageTakenLastTurn < 25) {
				this.setMove(Nightmare.MOVES[2], (byte) 3, Intent.ATTACK_BUFF, this.damage.get(1).base);
				return;
			} else {
				this.setMove(Nightmare.MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base, 3, true);
				return;
			}
		}else {
			this.setMove(Nightmare.MOVES[3], (byte) 4, Intent.DEFEND);
			return;
		}
	}
	
	

	@Override
	public void damage(DamageInfo damage) {
		super.damage(damage);
		damageTakenLastTurn = damage.output;
	}

	
	
	@Override
	public void update() {
		super.update();
		spriteEffect += Gdx.graphics.getDeltaTime();
		this.drawY += (Math.sin(spriteEffect) / (10f * Settings.scale));
		
		InfiniteSpire.logger.info(Math.sin(spriteEffect));
		
		if((int) Math.round(spriteEffect * 10) % 3 == 0) {
			if((new Random()).nextInt(3) == 0) {
				this.textureIndex += 1;
				if(textureIndex > 2) {
					textureIndex = 0;
				}
				this.img = InfiniteSpire.getTexture("img/monsters/nightmare/nightmare-"+ (textureIndex + 1) +".png");
			}
		}
	}

	@Override
	public void takeTurn() {
		switch(this.nextMove) {
		case 1:
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new SpireBlightPower(AbstractDungeon.player)));
			break;
		case 2:
			AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
			for(int i = 0; i < 3; i++) {
				AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
			}
			break;
		case 3:
			AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
			break;
		case 4:
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.blockCount));
			break;
		}
		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}

}
