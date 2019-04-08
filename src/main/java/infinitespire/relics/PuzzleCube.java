package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;
import infinitespire.patches.AbstractCardPatch;

import java.util.ArrayList;

public class PuzzleCube extends Relic {

	public static final String ID = InfiniteSpire.createID("PuzzleCube");
	private AbstractCard card;

	public PuzzleCube(){
		super(ID, "puzzlecube", RelicTier.COMMON, LandingSound.SOLID);
	}

	@Override
	public void atTurnStart() {
		CardGroup drawPile = AbstractDungeon.player.drawPile;
		CardGroup allCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

		allCards.group.addAll(AbstractDungeon.player.discardPile.group);
		allCards.group.addAll(AbstractDungeon.player.drawPile.group);
		allCards.group.addAll(AbstractDungeon.player.hand.group);
		allCards.group.addAll(AbstractDungeon.player.exhaustPile.group);

		for(AbstractCard card : allCards.group){
			AbstractCardPatch.Field.isPuzzleCubeCard.set(card, false);
		}

		if(drawPile.size() > 0){
			this.flash();
			AbstractCard card = getCard(drawPile);
			this.card = card;
			AbstractCard cardCopy = card.makeStatEquivalentCopy();
			AbstractCardPatch.Field.isPuzzleCubeCard.set(cardCopy, true);
			AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(cardCopy));
			AbstractCardPatch.Field.isPuzzleCubeCard.set(card, true);
		}
	}

	private AbstractCard getCard(CardGroup group){
		ArrayList<AbstractCard> cards = new ArrayList<>(group.group);
		cards.removeIf(
			abstractCard ->
				abstractCard.type == AbstractCard.CardType.CURSE ||
				abstractCard.type == AbstractCard.CardType.STATUS);

		return cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
	}

	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		if(AbstractCardPatch.Field.isPuzzleCubeCard.get(c)){
			AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
			AbstractCardPatch.Field.isPuzzleCubeCard.set(card, false);
		}
	}
}
