package infinitespire.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import infinitespire.InfiniteSpire;

public class RainbowPotion extends CustomPotion {

	public static final String ID = InfiniteSpire.createID("RainbowPotion");
	public static final PotionRarity RARITY = PotionRarity.UNCOMMON;
	public static final PotionSize SIZE = PotionSize.T;
	public static final PotionColor COLOR = PotionColor.WEAK;

	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
	private static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
	public static final String NAME = potionStrings.NAME;

	public RainbowPotion() {
		super(NAME, ID, RARITY, SIZE, COLOR);
		this.description = DESCRIPTIONS[0];
		this.tips.add(new PowerTip(this.name, this.description));
		//set Texture
	}

	@Override
	public void use(AbstractCreature abstractCreature) {

	}

	@Override
	public int getPotency(int i) {
		return 1;
	}

	@Override
	public AbstractPotion makeCopy() {
		return new RainbowPotion();
	}
}
