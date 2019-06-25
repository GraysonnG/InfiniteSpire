package infinitespire.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Colors;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;
import infinitespire.InfiniteSpire;
import infinitespire.patches.IntentEnumPatch;
import infinitespire.powers.TempThornsPower;

import java.util.ArrayList;

public class LordOfFortification extends LordBoss {

	public static final String ID = InfiniteSpire.createID("LordOfFortification");
	private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
	private static final int BASE_MAX_HP = 10000;

	private int phase;
	private static final float ANIM_TIME = 10f;
	private float animTicker;

	public LordOfFortification() {
		super(monsterStrings.NAME, ID, BASE_MAX_HP, 0, 0, 300f, 300f, 5f,
			(me) -> {
				AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(me, me, MoveValues.RAGEBUFF));
			});

		this.loadAnimation(
			InfiniteSpire.createPath("monsters/lordbosses/fortification/LordOfFortification.atlas"),
			InfiniteSpire.createPath("monsters/lordbosses/fortification/LordOfFortification.json"),
			2.0f);

		this.state.setAnimation(0, "idle", true);
		this.state.setAnimation(1, "shieldidle", true);

		if(CardCrawlGame.isInARun()) {
			for(int d : MoveValues.getDamageValues()) {
				damage.add(new DamageInfo(this, d));
			}

			doHealthScaling(BASE_MAX_HP);
		}
	}

	@Override
	public void usePreBattleAction() {
		super.usePreBattleAction();
	}

	@Override
	public void update() {
		super.update();
		if(animTicker > ANIM_TIME) {
			this.state.setAnimation(1, "shieldlift", false);
			this.state.addAnimation(1, "shieldidle", true, 0.0f);
			animTicker = 0.0f;
		}
		animTicker += Gdx.graphics.getDeltaTime();
	}

	@Override
	public void takeTurn() {
		AbstractPlayer player = AbstractDungeon.player;

		switch (this.nextMove) {
			case 0:
				doAction(new AddTemporaryHPAction(this, this, MoveValues.DEFEND));
				break;
			case 1:
				doAction(new AddTemporaryHPAction(this, this, MoveValues.DEFEND_1));
				doAction(new DamageAction(player, damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
				break;
			case 2:
				doAction(new AddTemporaryHPAction(this, this, MoveValues.DEFEND_2));
				doAction(new DamageAction(player, damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
				break;
			case 3:
				doAction(new AddTemporaryHPAction(this, this, MoveValues.DEFEND_3));
				doAction(new DamageAction(player, damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
				break;
			case 4:
				CardCrawlGame.sound.playA("GHOST_ORB_IGNITE_1", -0.6F);
				doAction(
					new VFXAction(
						new CollectorCurseEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY),
						2f));
				doAction(
					new ApplyPowerAction(
						AbstractDungeon.player,
						this,
						new WeakPower(AbstractDungeon.player, MoveValues.DEBUFF, true),
						MoveValues.DEBUFF));
				doAction(
					new ApplyPowerAction(
						AbstractDungeon.player,
						this,
						new VulnerablePower(AbstractDungeon.player, MoveValues.DEBUFF, true),
						MoveValues.DEBUFF));
				doAction(
					new ApplyPowerAction(
						AbstractDungeon.player,
						this,
						new FrailPower(AbstractDungeon.player, MoveValues.DEBUFF, true),
						MoveValues.DEBUFF));

				break;
			case 5:
				doAction(new VFXAction(new VerticalAuraEffect(Colors.get(InfiniteSpire.GDX_INFINITE_RED_NAME), this.hb.cX, this.hb.cY)));
				doAction(new ApplyPowerAction(this, this, new TempThornsPower(this, MoveValues.THORNS_TURNS, MoveValues.THORNS_DAMAGE, true)));
				break;
			case 6:
				doAction(new HealAction(this, this, MoveValues.getHealAmount(this.max_hp)));
				break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}

	@Override
	protected void getMove(int i) {
		InfiniteSpire.logger.info("phase: " + phase + " | turn: " + turn);

		switch (phase % 3) {
			case 0:
				switch (turn % 3) {
					case 0:
						this.setMove(MoveBytes.DEFEND, IntentEnumPatch.INFINITE_TEMPHP);
						break;
					case 1:
						this.setMove(MoveBytes.ATTACK_DEFEND_1, IntentEnumPatch.ATTACK_INFINITE_TEMPHP, damage.get(0).base);
						break;
					case 2:
						this.setMove(monsterStrings.MOVES[0], MoveBytes.DEBUFF, Intent.STRONG_DEBUFF);
						break;
				}
				break;
			case 1:
				switch (turn % 3) {
					case 0:
						this.setMove(MoveBytes.DEFEND, IntentEnumPatch.INFINITE_TEMPHP);
						break;
					case 1:
						this.setMove(MoveBytes.ATTACK_DEFEND_2, IntentEnumPatch.ATTACK_INFINITE_TEMPHP, damage.get(1).base);
						break;
					case 2:
						this.setMove(monsterStrings.MOVES[1], MoveBytes.THORNS, Intent.BUFF);
						break;
				}
				break;
			case 2:
				switch (turn % 3) {
					case 0:
						this.setMove(MoveBytes.DEFEND, IntentEnumPatch.INFINITE_TEMPHP);
						break;
					case 1:
						this.setMove(MoveBytes.ATTACK_DEFEND_3, IntentEnumPatch.ATTACK_INFINITE_TEMPHP, damage.get(2).base);
						break;
					case 2:
						this.setMove(monsterStrings.MOVES[2], MoveBytes.HEAL, Intent.MAGIC);
						break;
				}
				break;
		}
		if(turn % 3 == 0 && turn != 0) phase++;
		turn++;
	}

	private static class MoveBytes {
		private static final byte DEFEND = 0; //             Defend 50
		private static final byte ATTACK_DEFEND_1 = 1; //    Attack 50 Defend 50
		private static final byte ATTACK_DEFEND_2 = 2; //    Attack 75 Defend 25
		private static final byte ATTACK_DEFEND_3 = 3; //    Attack 25 Defend 75
		private static final byte DEBUFF = 4; //             Big Debuff (Weak, Frail, Vuln)
		private static final byte THORNS = 5; //             Gain 3 Turns of 10 Temp Thorns
		private static final byte HEAL = 6; //               Heal 5%
	}

	private static class MoveValues {
		private static final int DEFEND = 300;
		private static final int DEFEND_1 = 50;
		private static final int DEFEND_2 = 25;
		private static final int DEFEND_3 = 75;

		private static final int ATTACK_1 = 50;
		private static final int ATTACK_2 = 75;
		private static final int ATTACK_3 = 25;

		private static final int DEBUFF = 6;
		private static final int THORNS_TURNS = 3;
		private static final int THORNS_DAMAGE = 10;
		private static final float HEAL_PERCENT = 0.1f;

		private static final int RAGEBUFF = 50;

		public static ArrayList<Integer> getDamageValues() {
			ArrayList<Integer> values = new ArrayList<>();

			values.add(ATTACK_1);
			values.add(ATTACK_2);
			values.add(ATTACK_3);

			return values;
		}

		public static int getHealAmount(int maxHp) {
			return Math.round((float) maxHp * HEAL_PERCENT);
		}
	}
}
