package infinitespire.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class CursedDicePower extends AbstractPower {

	public static final String powerID = InfiniteSpire.createID("DicePower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public CursedDicePower(AbstractPlayer player) {
		this.owner = player;
		this.amount = -1;
		this.name = strings.NAME;
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("dice.png");
		this.type = PowerType.BUFF;
		this.priority = 99;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0];
	}
}
