package infinitespire.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BlueCandle;
import infinitespire.abstracts.Card;

public class Pacifist extends Card{

	public static final String ID = "Pacifist";
	public static final String NAME = "Pacifist";
	public static final String DESCRIPTION = "While this card is in your hand you cannot play Attacks.";
	private static final int COST = 2;
	private static final String IMG = "img/infinitespire/cards/beta.png";
	private static final CardType TYPE = CardType.CURSE;
	private static final CardColor COLOR = CardColor.CURSE;
	private static final CardRarity RARITY = CardRarity.CURSE;
	private static final CardTarget TARGET = CardTarget.SELF;

	public Pacifist() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
	}

	@Override
	public void upgrade() {

	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return cardPlayable(m) && hasEnoughEnergy();
	}

	@Override
	public boolean canPlay(AbstractCard card) {
		return card.type != CardType.ATTACK;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (p.hasRelic(BlueCandle.ID)) {
			this.useBlueCandle(p);
		}
	}

	@Override
	public void triggerWhenDrawn() {
		if (AbstractDungeon.player.hasRelic(BlueCandle.ID)) {
			this.cost = -2;
		}
	}


}
