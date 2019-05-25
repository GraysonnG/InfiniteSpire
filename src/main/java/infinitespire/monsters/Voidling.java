package infinitespire.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.powers.VenomPower;

public class Voidling extends AbstractMonster {
	public static final String NAME = "Voidling";
	public static final String ID = InfiniteSpire.createID("Voidling");
	public static final String SPECIAL_ENCOUNTER_ID = InfiniteSpire.createID("Three Voidlings");

	private int tackleDamage = 13;
	private int fangAttack = 5;
	private int fangPoison = 1;
	private int venomShotAmt = 4;
	private int blockAmt = 6;
	private int maxHP = 57;
	private int minHP = 48;

	public Voidling(){
		this(0.0f);
	}

	public Voidling(float xOffset) {
		super(NAME, ID, 32, 0.0f, 0.0f, 200f, 150f, null, xOffset, 0.0f);

		if(AbstractDungeon.ascensionLevel > 7){
			minHP += 2;
			maxHP += 2;
		}

		if(AbstractDungeon.ascensionLevel > 2){
			this.tackleDamage += 1;
			this.venomShotAmt += 1;
			this.fangAttack += 1;
		}

		setHp(minHP, maxHP);
		this.damage.add(new DamageInfo(this, tackleDamage));
		this.damage.add(new DamageInfo(this, 1));
		this.damage.add(new DamageInfo(this, fangAttack));

		this.loadAnimation(
			InfiniteSpire.createPath("monsters/voidling/Voidling.atlas"),
			InfiniteSpire.createPath("monsters/voidling/Voidling.json"),
			1.0f);

		AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
		e.setTime(e.getEndTime() * MathUtils.random());
	}

	@Override
	public void takeTurn() {
		AbstractPlayer player = AbstractDungeon.player;
		GameActionManager manager = AbstractDungeon.actionManager;

		switch(nextMove) {
			case MoveBytes.TACKLE:
				manager.addToBottom(new ChangeStateAction(this, "ATTACK"));
				manager.addToBottom(new DamageAction(player, damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
				break;
			case MoveBytes.DEFEND:
				manager.addToBottom(new GainBlockAction(this, this, blockAmt));
				break;
			case MoveBytes.VENOM_SHOT:
				manager.addToBottom(new DamageAction(player, damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
				manager.addToBottom(new ApplyPowerAction(player, this, new VenomPower(player, this, venomShotAmt), venomShotAmt));
				break;
			case MoveBytes.FANG_ATTACK:
				for(int i = 0; i < 2; i++) {
					manager.addToBottom(new DamageAction(player, damage.get(2), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
					manager.addToBottom(new ApplyPowerAction(player, this, new VenomPower(player, this, fangPoison), fangPoison));
				}
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}

	public void changeState(String key){
		switch (key) {
			case "ATTACK":
				this.state.setAnimation(0, "attack", false);
				this.state.addAnimation(0, "idle", true, 0.0f);
				break;
		}
	}

	public void damage(DamageInfo info) {
		super.damage(info);
		if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
			this.state.setAnimation(0, "oof", false);
			this.state.addAnimation(0, "idle", true, 0.0f);
		}
	}

	@Override
	protected void getMove(int i) {
		if(i < 30 || GameActionManager.turn == 0){
			if(!lastMove(MoveBytes.VENOM_SHOT)) {
				this.setMove(MoveBytes.VENOM_SHOT, Intent.ATTACK_DEBUFF, damage.get(1).base);
			} else if(AbstractDungeon.ascensionLevel >= 17) {
				this.setMove(MoveBytes.FANG_ATTACK, Intent.ATTACK_DEBUFF, damage.get(2).base, 2, true);
			} else {
				this.setMove(MoveBytes.TACKLE, Intent.ATTACK, damage.get(0).base);
			}
		} else if(i < 45) {
			this.setMove(MoveBytes.DEFEND, Intent.DEFEND);
		} else {
			this.setMove(MoveBytes.TACKLE, Intent.ATTACK, damage.get(0).base);
		}
	}


	public static class MoveBytes {
		public static final byte TACKLE = 0;
		public static final byte DEFEND = 1;
		public static final byte VENOM_SHOT = 2;
		public static final byte FANG_ATTACK = 3;
	}
}