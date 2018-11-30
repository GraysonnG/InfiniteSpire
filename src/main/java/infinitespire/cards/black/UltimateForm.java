package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;
import infinitespire.effects.uniqueVFX.UltimateFormEffect;
import infinitespire.powers.UltimateFormPower;

public class UltimateForm extends BlackCard {
	
	private static final String ID = InfiniteSpire.createID("UltimateForm");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String TEXTURE = "img/infinitespire/cards/ultimateform.png";
	private static final int COST = 5;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final int MAGIC = 2;
	
	public UltimateForm() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.POWER, CardTarget.SELF);
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
	}

	@Override
	public AbstractCard makeCopy() {
		return new UltimateForm();
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(1);
		}
	}

	@Override
	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new UltimateFormEffect(p.hb.cX, p.hb.cY), 1.0f));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new UltimateFormPower(p, this.magicNumber), this.magicNumber));
	}
}
