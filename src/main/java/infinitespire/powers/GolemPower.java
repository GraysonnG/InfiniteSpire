package infinitespire.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class GolemPower extends AbstractPower {

	public static final String powerID = InfiniteSpire.createID("GolemPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	private static float PERCENTAGE_GAIN = 0.05f;
	private int count;
	
	public GolemPower(AbstractCreature owner) {
		this.owner = owner;
		this.amount = 0;
		this.name = strings.NAME;
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("golem.png");
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.updateDescription();
	}
	
	@Override
	public void atStartOfTurn() {
		if(count == 20) return;
		this.flash();
		count += 1;
		updateDescription();
	}
	
	@Override
	public float atDamageFinalGive(float damageAmount, DamageType info) {
		if(info == DamageInfo.DamageType.NORMAL) {
			return (float) Math.ceil(damageAmount *= (1.0f + (count * PERCENTAGE_GAIN)));
		}
		return damageAmount;
	}
	
	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0] + Math.round((count * PERCENTAGE_GAIN) * 100) + strings.DESCRIPTIONS[1];
	}
}
