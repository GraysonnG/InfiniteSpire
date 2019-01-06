package infinitespire.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TempThornsPower extends TwoAmountPower {

	public static final String powerID = "is_TempThorns";

	public TempThornsPower(AbstractCreature owner, int turnsToHave, int amountOfThorns){
		this.owner = owner;
		this.amount = amountOfThorns;
		this.amount2 = turnsToHave;
		this.name = "Temporary Thorns";
		this.ID = powerID;
		loadRegion("thorns");
		this.updateDescription();
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		if(info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner) {
			this.flash();
			AbstractDungeon.actionManager.addToTop(new DamageAction(info.owner,
				new DamageInfo(this.owner, this.amount), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
		}
		return damageAmount;
	}

	@Override
	public void updateDescription() {
		if(this.amount2 > 1) {
			this.description = "For the next #b" + amount2 + " turns. When attacked, deals #b" + amount + " damage back.";
		}else {
			this.description = "For the rest of this turn. When attacked, deals #b" + amount + " damage back.";
		}
	}

	@Override
	public void atEndOfRound() {
		this.amount2 --;

		if(this.amount2 == 0) {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
		}

		this.updateDescription();
	}
}
