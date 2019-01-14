package infinitespire.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import infinitespire.InfiniteSpire;

public class VenomPower extends TwoAmountPower implements HealthBarRenderPower, NonStackablePower {

	public static final String powerID = InfiniteSpire.createID("VenomPower");
	private AbstractCreature source;

	public VenomPower(AbstractCreature owner, AbstractCreature source, int amount){
		this.source = source;
		this.owner = owner;
		this.amount = amount;
		this.amount2 = 0;
		this.type = PowerType.BUFF;
		this.name = "Venom";
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("venom.png");
		this.updateDescription();
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if(isPlayer) {
			if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead() && this.amount > 0) {
				this.flashWithoutSound();
				AbstractDungeon.actionManager.addToBottom(new LoseHPAction(owner, owner, amount));
				this.amount2 += this.amount;
				this.amount--;
				updateDescription();
			}
		}
	}

	@Override
	public void update(int slot) {
		if(amount > 0) {
			capAmount();
		}
		super.update(slot);
	}

	@Override
	public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
		return super.atDamageFinalReceive(damage, type);
	}

	public void capAmount(){
		if(this.amount >= owner.currentHealth && owner.currentHealth != 1){
			amount = owner.currentHealth - 1;
		}else if(amount >= owner.currentHealth) {
			amount = 0;
		}
		updateDescription();
	}

	public void updateDescription(){
		StringBuilder desc = new StringBuilder("At the end of your turn, lose #b");
		desc.append(this.amount);
		desc.append(" HP, then reduce #yVenom by #b1. NL NL ");
		desc.append("When ");
		desc.append(source.name);
		desc.append(" dies, heal all damage dealt by #yVenom (Heal #g");
		desc.append(amount2);
		desc.append(" HP). NL #rVenom #rcannot #rkill #ryou.");
		this.description = desc.toString();
	}

	@Override
	public int getHealthBarAmount() {
		return this.amount;
	}

	@Override
	public Color getColor() {
		return Color.valueOf("#24007f");
	}

	@Override
	public boolean isStackable(AbstractPower power) {
		return (power instanceof VenomPower && ((VenomPower) power).source == this.source);
	}

	public void onMonsterDeath(AbstractMonster monster) {
		if (this.source == monster) {
			this.owner.heal(this.amount2);
			this.amount = 0;
			this.amount2 = 0;
		}
	}

	@SuppressWarnings("unused")
	@SpirePatch(clz= AbstractMonster.class, method = "die", paramtypez = {boolean.class})
	public static class Patch {
		@SpirePrefixPatch
		public static void onMonsterDeath(AbstractMonster monster, boolean trigger) {
			if(!monster.isDying){
				for(AbstractPower p : AbstractDungeon.player.powers) {
					if(p instanceof VenomPower) {
						((VenomPower) p).onMonsterDeath(monster);
					}
				}
			}
		}
	}
}
