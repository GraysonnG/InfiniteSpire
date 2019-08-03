package infinitespire.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import infinitespire.actions.DiscoverBlackCardAction;

public class BlackPotion extends CustomPotion {

	public static final String ID = "infinitespire:BlackPotion";
	public static final PotionRarity RARITY = PotionRarity.RARE;
	public static final PotionSize SIZE = PotionSize.CARD;
	public static final PotionColor COLOR = PotionColor.WEAK;

	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
	public static final String NAME = potionStrings.NAME;
	private static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public BlackPotion() {
		super(NAME, ID, RARITY, SIZE, COLOR);
		this.description = DESCRIPTIONS[0];
		this.tips.add(new PowerTip(this.name, this.description));
	}

	@Override
	public void use(AbstractCreature abstractCreature) {
		AbstractDungeon.actionManager.addToBottom(new DiscoverBlackCardAction());
	}

	@Override
	public int getPotency(int i) {
		return 1;
	}

	@Override
	public AbstractPotion makeCopy() {
		return new BlackPotion();
	}

	@Override
	public int getPrice() {
		return 150;
	}
}
