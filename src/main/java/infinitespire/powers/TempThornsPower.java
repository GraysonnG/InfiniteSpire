package infinitespire.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import infinitespire.InfiniteSpire;

public class TempThornsPower extends TwoAmountPower implements NonStackablePower {

	public static final String powerID = InfiniteSpire.createID("TempThorns");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public boolean justApplied;

	public TempThornsPower(AbstractCreature owner, int turnsToHave, int amountOfThorns, boolean justApplied){
		this.owner = owner;
		this.amount = amountOfThorns;
		this.amount2 = turnsToHave;
		this.name = strings.NAME;
		this.ID = powerID;
		loadRegion("thorns");
		this.justApplied = justApplied;
		this.updateDescription();
	}

	public TempThornsPower(AbstractCreature owner, int turnsToHave, int amountOfThorns) {
		this(owner, turnsToHave, amountOfThorns, true);
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		if(info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner) {
			this.flash();
			AbstractDungeon.actionManager.addToTop(new DamageAction(info.owner,
				new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
		}
		return damageAmount;
	}

	@Override
	public void updateDescription() {
		if(this.amount2 > 1) {
			this.description = strings.DESCRIPTIONS[0] + amount2 + strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2];
		}else {
			this.description = strings.DESCRIPTIONS[3] + amount + strings.DESCRIPTIONS[2];
		}
	}

	@Override
	public void atEndOfRound() {
		if(this.justApplied) {
			justApplied = false;
			return;
		}
		this.amount2 --;

		if(this.amount2 == 0) {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
		}

		this.updateDescription();
	}
}
