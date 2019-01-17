package infinitespire.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class SpireBlightPower extends AbstractPower {

	public static final String powerID = InfiniteSpire.createID("SpireBlightPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public SpireBlightPower(AbstractCreature owner) {
		this.owner = owner;
		this.amount = -1;
		this.name = strings.NAME;
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("spireblight.png");
		this.type = PowerType.DEBUFF;
		this.updateDescription();
	}
	
	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0];
	}
	
	@Override
	public float atDamageFinalGive(float damageAmount, DamageInfo.DamageType type) {
		if(type == DamageInfo.DamageType.NORMAL) {
			return (float) Math.ceil(damageAmount *= 0.75f);
		}
		
		return damageAmount;
	}

	@Override
	public float modifyBlock(float blockAmount) {
		blockAmount *= 0.75f;
		return blockAmount;
	}
}
