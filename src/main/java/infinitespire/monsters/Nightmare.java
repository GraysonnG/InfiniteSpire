package infinitespire.monsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;

import infinitespire.InfiniteSpire;
import infinitespire.powers.RealityShiftPower;
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
	private int attackCount;
	
	public Nightmare() {
		super(NAME, ID, 250, -0.0F, -10.0F, 160f, 300f, null);
		this.type = EnemyType.ELITE;
		this.dialogX = -160.0f * Settings.scale;
		this.dialogY = 40f * Settings.scale;
		this.img = InfiniteSpire.getTexture("img/monsters/nightmare/nightmare-1.png");
		this.setHp(150);
		
		this.attackDmg = 5;
		this.slamDmg = 25;
		this.blockCount = 20;
		this.attackCount = 3;
		
		this.damage.add(new DamageInfo(this, this.attackDmg));
		this.damage.add(new DamageInfo(this, this.slamDmg));
		
	}

	@Override
	protected void getMove(int num) {
		if(firstTurn) {
			this.setMove(Nightmare.MOVES[0], (byte) 1, Intent.STRONG_DEBUFF);
			return;
		}
		if (num > 20) {
			this.setMove(Nightmare.MOVES[2], (byte) 3, Intent.ATTACK_BUFF, this.damage.get(1).base);
			return;
		}else {
			this.setMove(Nightmare.MOVES[3], (byte) 4, Intent.DEFEND);
			return;
		}
	}

	@Override
	public void usePreBattleAction() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RealityShiftPower(this), 30));
	}

	@Override
	public void damage(DamageInfo info) {
		super.damage(info);
		
		if(this.hasPower("is_Reality_Shift") && !firstTurn) {
			AbstractPower p = this.getPower("is_Reality_Shift");
			p.amount -= info.output;
			if(p.amount <= 0) {
				p.amount = 50;
				p.flash();
				onEnoughDamageTaken();
			}
			p.updateDescription();
		}
	}

	public void onEnoughDamageTaken() {
		AbstractDungeon.effectList.add(new PowerBuffEffect(this.hb.cX - this.animX, this.hb.cY + this.hb.height / 2.0f, ""));
		AbstractDungeon.actionManager.addToTop(new GainBlockAction(this, this, this.blockCount));
		
		this.setMove(Nightmare.MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base, this.attackCount, true);
		this.createIntent();
		
		AbstractDungeon.actionManager.cardQueue.clear();
        for (final AbstractCard c : AbstractDungeon.player.limbo.group) {
            AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
        }
        AbstractDungeon.player.limbo.group.clear();
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.overlayMenu.endTurnButton.disable(true);
	}
	
	@Override
	public void update() {
		super.update();
		spriteEffect += Gdx.graphics.getDeltaTime();
		this.drawY += (Math.sin(spriteEffect) / (10f * Settings.scale));
		
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
			this.firstTurn = false;
			break;
		case 2:
			AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
			for(int i = 0; i < this.attackCount; i++) {
				AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
				this.attackCount++;
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
		if(this.hasPower("is_Reality_Shift")) {
			this.getPower("is_Reality_Shift").amount = 50;
		}
	}

}
