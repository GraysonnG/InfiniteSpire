package infinitespire.powers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class PrinceIdolPower extends AbstractPower {

	public static final String powerID = InfiniteSpire.createID("PrinceIdolPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public PrinceIdolPower(AbstractMonster m) {
		this.owner = m;
		this.amount = -1;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("idol.png");
		this.updateDescription();
	}

	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0];
	}
}