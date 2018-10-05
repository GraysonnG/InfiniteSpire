package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class MitigationPower extends AbstractPower {

	public static final String PowerID = "MitigationPower";

	private float amountToMitigate;

	public MitigationPower(int turnsOfMitigation, float amountToMitigate, AbstractCreature owner){
		InfiniteSpire.logger.info("Applying Crit");
		this.owner = owner;
		this.amount = turnsOfMitigation;
		this.name = "Mitigation";
		this.ID = PowerID;
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/mitigation.png");
		this.type = PowerType.BUFF;
		this.priority = 99999;
		this.isTurnBased = true;
		this.amountToMitigate = amountToMitigate;

		this.updateDescription();
	}

	@Override
	public float atDamageFinalReceive(float damageAmount, DamageInfo.DamageType type) {

		damageAmount *= amountToMitigate;

		return damageAmount;
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if(!isPlayer){
			amount--;
			if(amount <= 0){
				AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
			}
			updateDescription();
		}
	}

	@Override
	public void updateDescription() {
		this.description = owner.name + " will receive #b" + (int)(100 - (100 * amountToMitigate)) + "% less damage for " + amount + " turns.";
	}
}
