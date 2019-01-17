package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import infinitespire.InfiniteSpire;
import infinitespire.util.TextureLoader;

public class UltimateFormPower extends AbstractPower {

	public static final String powerID = InfiniteSpire.createID("UltimateFormPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public UltimateFormPower(AbstractPlayer p, int amount) {
		this.name = strings.NAME;
		this.ID = powerID;
		this.owner = p;
		this.amount = amount;
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.img = TextureLoader.getTexture("img/infinitespire/powers/ultimateform.png");
	}
	
	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0] + this.amount + strings.DESCRIPTIONS[1] + this.amount + strings.DESCRIPTIONS[2];
	}

	@Override
	public void atStartOfTurnPostDraw() {
		this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.amount), this.amount));
        if(AbstractDungeon.player.maxOrbs > 0)
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new FocusPower(this.owner, this.amount), this.amount));
	}
	
	
}
