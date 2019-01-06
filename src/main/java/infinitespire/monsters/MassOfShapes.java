package infinitespire.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import infinitespire.actions.SpawnShapeAction;
import infinitespire.powers.ClusterPower;
import infinitespire.util.TextureLoader;

import java.util.ArrayList;

public class MassOfShapes extends AbstractMonster {
	public static final String ID = "infinitespire:MassOfShapes";
	private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
	private static final String NAME = monsterStrings.NAME;
	private static final int MAX_HP = 550;

	private static final int SMALL_BLOCK = 15;
	private static final int LARGE_BLOCK = 30;
	private static final int EXPLODE = 45;
	private static final int SPAWN_CHANCE = 33;

	private ArrayList<Minion> minions = new ArrayList<>();

	private int turn = 0;

	public MassOfShapes(){
		super(NAME, ID, MAX_HP, 0.0f, 0.0f, 458f, 415f, null);
		this.type = EnemyType.BOSS;

		this.dialogX = 0f;
		this.dialogY = 0f;
		this.img = TextureLoader.getTexture("img/infinitespire/monsters/massofshapes/massofshapes.png");

		this.damage.add(new DamageInfo(this, EXPLODE));
	}

	@Override
	public void usePreBattleAction() {
		CardCrawlGame.music.unsilenceBGM();
		AbstractDungeon.scene.fadeOutAmbiance();
		AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ClusterPower(this)));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, 3), 3));
	}

	public void update(){
		super.update();
		minions.removeIf((minion) -> minion.minion.isDead);
	}

	@Override
	public void die() {
		this.useFastShakeAnimation(5.0f);
		CardCrawlGame.screenShake.rumble(4.0f);
		this.deathTimer += 1.5f;
		super.die();
		this.onBossVictoryLogic();
		for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!m.isDead && !m.isDying) {
				AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
				AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
				AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2f));
			}
		}
	}

	@Override
	public void damage(DamageInfo info) {
		super.damage(info);

		if(this.currentBlock == 0) {
			if (info.owner != null && info.owner.equals(AbstractDungeon.player) && !AbstractDungeon.actionManager.turnHasEnded) {
				if (this.minions.size() < 3 && AbstractDungeon.miscRng.random(0, 99) <= SPAWN_CHANCE) {
					AbstractDungeon.actionManager.addToTop(new SpawnShapeAction(getNextOpenMinionSlot(), this));
				}
			}
		}

		if(this.isDead){
			for (Minion minion : minions){
				minion.minion.escape();
			}
		}
	}

	public void addMinion(AbstractMonster m) {
		Minion minion = new Minion(getNextOpenMinionSlot());
		minion.setMinion(m);
		minions.add(minion);
	}

	@Override
	public void takeTurn() {
		AbstractPlayer p = AbstractDungeon.player;
		AbstractMonster m = this;
		GameActionManager a = AbstractDungeon.actionManager;

		switch (this.nextMove){
			case MoveBytes.GAIN_THORNS:
				a.addToBottom(new ApplyPowerAction(m, m, new ThornsPower(m, 2), 2));
				a.addToBottom(new GainBlockAction(m, m, SMALL_BLOCK));
				break;
			case MoveBytes.EXPLODE:
				a.addToBottom(new DamageAction(p, damage.get(0), AbstractGameAction.AttackEffect.FIRE));
				AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(this.hb.cX, this.hb.cY));
				break;
			case MoveBytes.ADD_DAZED:
				AbstractCard dazed = CardLibrary.getCard(Dazed.ID).makeCopy();
				a.addToBottom(new MakeTempCardInDrawPileAction(dazed, 2, true, true));
				break;
			case MoveBytes.STR_GAIN:
				for(AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters){
					a.addToBottom(new ApplyPowerAction(mon, m, new StrengthPower(mon, 3), 3));
				}
				break;
			case MoveBytes.BLOCK:
				a.addToBottom(new GainBlockAction(m, m, LARGE_BLOCK));
		}
		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}

	@Override
	protected void getMove(int roll) {
		if(this.turn % 3 == 0) {
			if(roll <= 33){
				this.setMove(MoveBytes.GAIN_THORNS, Intent.DEFEND_BUFF);
			}else if(roll <= 66){
				this.setMove(MoveBytes.EXPLODE, Intent.ATTACK, this.damage.get(0).base);
			}else{
				this.setMove(MoveBytes.ADD_DAZED, Intent.DEBUFF);
			}
		}
		if(this.turn % 3 == 1) {
			this.setMove(MoveBytes.BLOCK, Intent.DEFEND);
		}
		if(this.turn % 3 == 2) {
			this.setMove(MoveBytes.STR_GAIN, Intent.BUFF);
		}
		this.turn++;
	}



	private int getNextOpenMinionSlot() {
		boolean[] slots = {true, true, true};
		for(Minion minion : minions) {
			slots[minion.slot] = false;
		}

		for(int i = 0; i < slots.length; i ++){
			if(slots[i]){
				return i;
			}
		}

		return -1;
	}

	private class MoveBytes {
		private static final byte GAIN_THORNS = 0;
		private static final byte EXPLODE = 1;
		private static final byte ADD_DAZED = 2;
		private static final byte STR_GAIN = 3;
		private static final byte BLOCK = 4;

	}

	private class Minion {
		public AbstractMonster minion;
		public int slot;

		public Minion(int slot) {
			this.slot = slot;
		}

		public Minion() {
			this(0);
		}

		public void setMinion(AbstractMonster m){
			this.minion = m;
		}
	}
}
