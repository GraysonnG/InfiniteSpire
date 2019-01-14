package infinitespire.powers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
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
		this.ID = PowerID;
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/bossarmor.png");
		this.type = PowerType.BUFF;
		this.priority = 99999; //nobody make anything bigger than this

		this.mitigationAmount = amountToMitigate;
		this.turnsOfMitigation = turnsOfMitigation;
		this.thresholdDamage = thresholdDamage;

		this.updateDescription();
	}

	@Override
	public void updateDescription() {
		if (Settings.language == Settings.GameLanguage.FRA){
				this.name = "Armure de boss";
			this.description = "Apr\u00e8s avoir prit #b" + thresholdDamage + " d\u00e9gats ce tour, " + owner.name +
				" gagnera Mitigation pour #b" + turnsOfMitigation + (turnsOfMitigation > 1 ? " tours." : " tour.");
		} else {
				this.name = "Boss Armor";
			this.description = "After taking #b" + thresholdDamage + " damage this turn, " + owner.name +
				" will gain Mitigation for #b" + turnsOfMitigation + (turnsOfMitigation > 1 ? " turns." : " turn.");
		}

	}

	public BossArmorPower(int thresholdDamage, float amountToMitigate, AbstractCreature owner){
		this(thresholdDamage, 1, amountToMitigate, owner);
	}

	@Override
	public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {

		if(owner.hasPower(MitigationPower.PowerID)) return damage;

		float newDamage = damage;

		if(newDamage > this.amount){
			float amountOver = newDamage - amount;
			newDamage -= amountOver;
			amountOver *= this.mitigationAmount;
			newDamage += amountOver;
		}

		return newDamage;
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {

		InfiniteSpire.logger.info("damageAmount:" + damageAmount);

		if(owner.hasPower(MitigationPower.PowerID)) return damageAmount;


		amount -= damageAmount;


		if(amount <= 0){
			amount = -1;
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner,
				new MitigationPower(turnsOfMitigation, 0.1f, owner), turnsOfMitigation));
		}
		return damageAmount;
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
