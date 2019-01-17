package infinitespire.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class DeenergizedPower extends AbstractPower {

	public static final String powerID = InfiniteSpire.createID("DeenergizedPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public DeenergizedPower(AbstractPlayer owner, int amount) {
		this.name = strings.NAME;
		this.ID = powerID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.img = InfiniteSpire.Textures.getPowerTexture("energydown.png");
		this.type = PowerType.DEBUFF;
	}

	@Override
	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
	}

	@Override
	public void onEnergyRecharge() {
		this.flash();
		AbstractDungeon.player.loseEnergy(this.amount);
	}
}
