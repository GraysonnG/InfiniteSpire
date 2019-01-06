package infinitespire.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import infinitespire.InfiniteSpire;
import infinitespire.effects.BlackCardEffect;
import infinitespire.powers.KeyMirrorPower;
import infinitespire.powers.RealityShiftPower;
import infinitespire.powers.SpireBlightPower;

import java.util.Random;

public class Nightmare extends AbstractMonster {

	public static final String ID = "Nightmare";
	public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Nightmare");
	public static final String NAME = monsterStrings.NAME;
	public static final String[] MOVES = Nightmare.monsterStrings.MOVES;
	public static final String[] DIALOG = Nightmare.monsterStrings.DIALOG;
	public static int timesDefeated = 0;
	public static int timesNotReceivedBlackCard = 0;

	private int textureIndex = 0;

	private float spriteEffect;
	private boolean firstTurn = true;
	private boolean hasActivated = false;
	private boolean isAlpha;
	private int attackDmg;
	private int slamDmg;
	private int debuffDmg;
	private int blockCount;
	private int effectCount;
	private boolean isStrong;
	private float portalRotation = 0.0f;

	public Nightmare() {
		this(false);
	}

	public Nightmare(boolean isAlpha) {
		super(NAME, ID, 200, -0.0F, -10.0F, 160f, 300f, null);
		this.isAlpha = isAlpha;
		this.type = EnemyType.ELITE;
		this.dialogX = -160.0f * Settings.scale;
		this.dialogY = 40f * Settings.scale;
		this.img = InfiniteSpire.getTexture("img/infinitespire/monsters/nightmare/nightmare-1.png");
		int hpAmount = 200;

		if(this.isAlpha){
			this.name = "Nightmare Alpha";
			hpAmount += 100;
		}

		if(CardCrawlGame.playerName.equals("fiiiiilth")){
			this.name = "Niiiiightmare";
		}

		this.debuffDmg = 10;

		if(AbstractDungeon.bossCount > 1 || timesDefeated > 0) {
			isStrong = true;
			hpAmount *= 1.5f;
			this.attackDmg = 5;
			this.slamDmg = 25;
			this.blockCount = 30;
			this.effectCount = 4; //effectively 5
		} else {
			this.attackDmg = 5;
			this.slamDmg = 15;
			this.blockCount = 20;
			this.effectCount = 2; //effectively 3
		}

		if(Settings.hasSapphireKey) {
			hpAmount += 50;
		}

		this.setHp(hpAmount);

		this.damage.add(new DamageInfo(this, this.attackDmg));
		this.damage.add(new DamageInfo(this, this.slamDmg));
		this.damage.add(new DamageInfo(this, this.debuffDmg));
	}

	public static void save(SpireConfig config) {
		config.setInt("nightmareTimesDefeated", timesDefeated);
		config.setInt("nightmareTimesNotReceivedBlackCard", timesNotReceivedBlackCard);
	}

	public static void load(SpireConfig config) {
		timesDefeated = config.getInt("nightmareTimesDefeated");
		timesNotReceivedBlackCard = config.getInt("nightmareTimesNotReceivedBlackCard");
	}

	public static void clear(){
		timesDefeated = 0;
		timesNotReceivedBlackCard = 0;
	}

	@Override
	public void die() {
		super.die();
		timesDefeated++;
		timesNotReceivedBlackCard++;
	}

	@Override
	public void render(SpriteBatch sb) {
		if(this.isAlpha) {
			sb.setColor(Color.WHITE.cpy());
			sb.draw(InfiniteSpire.getTexture(
				"img/infinitespire/screen/portal.png"),
				this.hb.cX - 128f, this.hb.cY - 128f, 128f, 128f,
				256f, 256f, 1.5f, 1.5f,
				portalRotation * 90f, 0, 0, 256, 256,
				false, false);
		}
		super.render(sb);
	}

	@Override
	public void update() {
		super.update();
		this.portalRotation += Gdx.graphics.getDeltaTime(); //look if you get a crash on this line you been sitting on that screen way too long and honestly i just cant be bothered to loop this float so it never crashes
		spriteEffect += Gdx.graphics.getDeltaTime();
		this.drawY += (Math.sin(spriteEffect) / (10f * Settings.scale));

		if(Math.round(spriteEffect * 10) % 3 == 0) {
			if((new Random()).nextInt(3) == 0) {
				this.textureIndex += 1;
				if(textureIndex > 2) {
					textureIndex = 0;
				}
				this.img = InfiniteSpire.getTexture("img/infinitespire/monsters/nightmare/nightmare-" + (textureIndex + 1) +".png");
			}
		}
	}

	@Override
	public void usePreBattleAction() {
		GameActionManager manager = AbstractDungeon.actionManager;

		if(Settings.hasRubyKey) {
			manager.addToBottom(new ApplyPowerAction(this, this, new KeyMirrorPower(this, KeyMirrorPower.KeyColor.RUBY)));
		}

		if(Settings.hasSapphireKey) {
			manager.addToBottom(new ApplyPowerAction(this, this, new KeyMirrorPower(this, KeyMirrorPower.KeyColor.SAPPHIRE)));
		}

		if(Settings.hasEmeraldKey) {
			manager.addToBottom(new ApplyPowerAction(this, this, new KeyMirrorPower(this, KeyMirrorPower.KeyColor.EMERALD)));
		}

		if(this.isAlpha){
			manager.addToBottom(new ApplyPowerAction(this, this, new RealityShiftPower(this), 50));

			if(Settings.hasEmeraldKey) {
				manager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, this.maxHealth / 3)));
			}
		}

		if(Settings.hasEmeraldKey) {
			manager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 2), 2));
		}

		if(isStrong) {

			if(timesDefeated > 0) {
				manager.addToBottom(new ApplyPowerAction(this, this,
					new MetallicizePower(this, 5 * timesDefeated)));
			}

			if(timesDefeated > 1) {
				manager.addToBottom(new ApplyPowerAction(this, this,
					new StrengthPower(this, timesDefeated - 1)));
			}
		}
	}

	@Override
	public void takeTurn() {
		switch(this.nextMove) {
			case 1:
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new SpireBlightPower(AbstractDungeon.player)));
				if(!this.hasPower("is_Reality_Shift")) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RealityShiftPower(this), 50));
				}
				break;
			case 2:
				AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
				for(int i = 0; i < this.effectCount; i++) {
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
			case 5:
				AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
				for(int i = 0; i < this.effectCount; i++) {
					AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
				}
				AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, 25));
				break;
			case 6:
				AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
				AbstractCard voidCard = CardLibrary.getCopy(VoidCard.ID).makeCopy();

				AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(voidCard, 2, true, true));
				break;
		}
		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}

	@Override
	protected void getMove(int num) {
		if(this.isAlpha) {
			getMoveAlpha(num);
		} else {
			getMoveBeta(num);
		}
	}

	public void getMoveAlpha(int num) {
		this.hasActivated = false;
		if(firstTurn) {
			if(Settings.hasSapphireKey) {
				this.setMove(Nightmare.MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base, this.effectCount, true);
			} else {
				this.setMove(Nightmare.MOVES[3], (byte) 4, Intent.DEFEND);
			}
			firstTurn = false;
			return;
		}
		if(num > 20) {
			if(num % 3 == 0){
				this.setMove(Nightmare.MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base, this.effectCount, true);
			} else if(Settings.hasRubyKey && num % 3 == 1) {
				this.setMove((byte) 6, Intent.ATTACK_DEBUFF, this.damage.get(2).base);
			} else {
				this.setMove(Nightmare.MOVES[2], (byte) 3, Intent.ATTACK_BUFF, this.damage.get(1).base);
			}
		} else {
			this.setMove(Nightmare.MOVES[3], (byte) 4, Intent.DEFEND);
		}
	}

	public void getMoveBeta(int num) {
		this.hasActivated = false;
		if(firstTurn) {
			this.setMove(Nightmare.MOVES[0], (byte) 1, Intent.MAGIC);
			firstTurn = false;
			return;
		}
		if (num > 20) {
			if(Settings.hasRubyKey && num % 3 == 0){
				this.setMove(Nightmare.MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base, this.effectCount, true);
			} else {
				this.setMove(Nightmare.MOVES[2], (byte) 3, Intent.ATTACK_BUFF, this.damage.get(1).base);
			}
		} else {
			this.setMove(Nightmare.MOVES[3], (byte) 4, Intent.DEFEND);
		}
	}

	public void onEnoughDamageTaken() {
		this.effectCount++;
		if(AbstractDungeon.overlayMenu.endTurnButton.enabled) {
			if(this.isAlpha){
				this.onEnoughDamageTakenAlpha();
			} else {
				this.onEnoughDamageTakenBeta();
			}
		}
	}

	public void onEnoughDamageTakenAlpha() {
		AbstractDungeon.effectList.add(new PowerBuffEffect(this.hb.cX - this.animX, this.hb.cY + this.hb.height / 2.0f, ""));

		AbstractDungeon.actionManager.addToTop(new GainBlockAction(this, this, this.blockCount));
		AbstractDungeon.effectList.add(new BlackCardEffect());

		this.setMove((byte) 5, Intent.ATTACK_BUFF, this.damage.get(0).base, this.effectCount, true);
		this.createIntent();

		AbstractDungeon.actionManager.cardQueue.clear();
		for (final AbstractCard c : AbstractDungeon.player.limbo.group) {
			AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
		}
		AbstractDungeon.player.limbo.group.clear();
		AbstractDungeon.player.releaseCard();
		AbstractDungeon.overlayMenu.endTurnButton.disable(true);
		this.hasActivated = true;
	}

	public void onEnoughDamageTakenBeta() {
		AbstractDungeon.effectList.add(new PowerBuffEffect(this.hb.cX - this.animX, this.hb.cY + this.hb.height / 2.0f, ""));
		AbstractDungeon.actionManager.addToTop(new GainBlockAction(this, this, this.blockCount));
		AbstractDungeon.effectList.add(new BlackCardEffect());

		this.setMove(Nightmare.MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base, this.effectCount, true);
		this.createIntent();

		AbstractDungeon.actionManager.cardQueue.clear();
		for (final AbstractCard c : AbstractDungeon.player.limbo.group) {
			AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
		}
		AbstractDungeon.player.limbo.group.clear();
		AbstractDungeon.player.releaseCard();
		AbstractDungeon.overlayMenu.endTurnButton.disable(true);
		this.hasActivated = true;
	}
}
