package infinitespire.powers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
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
		if (Settings.language == Settings.GameLanguage.FRA){
			this.name = "Atténuation";
			this.description = owner.name + " recevra #b" + (int)(100 - (100 * amountToMitigate)) + "% d\u00e9gats en moins pour " + amount + " tours.";
		} else {
			this.name = "Mitigation";
			this.description = owner.name + " will receive #b" + (int)(100 - (100 * amountToMitigate)) + "% less damage for " + amount + " turns.";
		}

	}
}
