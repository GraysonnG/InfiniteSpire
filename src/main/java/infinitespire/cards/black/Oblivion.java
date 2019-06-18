package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class Oblivion extends BlackCard {
	private static final String ID = InfiniteSpire.createID("Oblivion");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String TEXTURE = "img/infinitespire/cards/oblivion.png";
	private static final int COST = 1;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int MAGIC = 2;
	private static final int DAMAGE = 13;
	// BLOCK

	public Oblivion() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.ATTACK, CardTarget.ENEMY);
		this.baseDamage = DAMAGE;
		this.magicNumber = MAGIC;
		this.baseMagicNumber = MAGIC;
	}

	public void upgrade() {
		if (!upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(2);
		}
	}

	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		for(int i = 0; i < this.magicNumber; i++) {
			AbstractDungeon.actionManager.addToBottom(new ChannelAction(AbstractOrb.getRandomOrb(true)));
		}
	}
}