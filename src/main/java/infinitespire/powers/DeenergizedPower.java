package infinitespire.powers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class DeenergizedPower extends AbstractPower {

	public static final String powerID = "is_Deenergized";

	public DeenergizedPower(AbstractPlayer owner, int amount) {
		this.ID = powerID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/energydown.png");
		this.type = PowerType.DEBUFF;
	}

	@Override
	public void updateDescription() {
		if (Settings.language == Settings.GameLanguage.FRA){
						this.name = "D\u00e9sexcit\u00e9";
						this.description = "Au d\u00e9but dechaque tour, perdez #b" + amount + "\u00e9nergie.";
		} else {
						this.name = "De-Energized";
						this.description = "At the start of each turn, lose #b" + amount + " energy.";
		}

	}

	@Override
	public void onEnergyRecharge() {
		this.flash();
		AbstractDungeon.player.loseEnergy(this.amount);
	}
}
