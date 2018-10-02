package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class BossArmorPower extends AbstractPower{

	public static final String PowerID = "BossArmorPower";

	private float mitigationAmount;
	private int turnsOfMitigation;
	private final int thresholdDamage;

	public BossArmorPower(int thresholdDamage, int turnsOfMitigation, float amountToMitigate, AbstractCreature owner){
		InfiniteSpire.logger.info("Applying Crit");
		this.owner = owner;
		this.amount = thresholdDamage;
		this.name = "Boss Armor";
		this.ID = PowerID;
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/jokercard.png");
		this.type = PowerType.BUFF;
		this.priority = 99999; //nobody make anything bigger than this
		this.updateDescription();

		this.mitigationAmount = amountToMitigate;
		this.turnsOfMitigation = turnsOfMitigation;
		this.thresholdDamage = thresholdDamage;
	}

	public BossArmorPower(int thresholdDamage, float amountToMitigate, AbstractCreature owner){
		this(thresholdDamage, 1, amountToMitigate, owner);
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {

		InfiniteSpire.logger.info("damageAmount:" + damageAmount);

		if(owner.hasPower(MitigationPower.PowerID)) return damageAmount;

		int newDamage = damageAmount;

		if(newDamage > this.amount){
			float amountOver = newDamage - amount;
			newDamage -= amountOver;
			amountOver *= this.mitigationAmount;
			newDamage += (int) amountOver;
		}

		amount -= damageAmount;

		InfiniteSpire.logger.info("newDamage" + newDamage);
		if(amount <= 0){
			amount = -1;
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner,
				new MitigationPower(turnsOfMitigation, 0.1f, owner), turnsOfMitigation));
		}
		return newDamage;
	}

	@Override
	public void atEndOfRound() {
		if(owner.hasPower(MitigationPower.PowerID)) {
			if(owner.getPower(MitigationPower.PowerID).amount > 0){
				return;
			}
		}

		this.amount = thresholdDamage;
	}
}
