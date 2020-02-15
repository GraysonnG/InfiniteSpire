package infinitespire.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import infinitespire.patches.DiscoverPatch;

public class DiscoverRandomCardAction extends AbstractGameAction {
	private boolean retrieveCard = false;
	private AbstractCard prohibited;

	public DiscoverRandomCardAction() {
		actionType = ActionType.CARD_MANIPULATION;
		duration = Settings.ACTION_DUR_FAST;
		this.amount = 3;
		this.prohibited = null;
	}

	@Override
	public void update() {
		if(duration == Settings.ACTION_DUR_FAST) {
			DiscoverPatch.lookingForCount = amount;
			DiscoverPatch.lookingForProhibit = prohibited;
			DiscoverPatch.randomCardPool = true;
			DiscoverPatch.lookingForColor = AbstractCard.CardColor.COLORLESS;

			AbstractDungeon.cardRewardScreen.discoveryOpen();
			tickDuration();
			return;
		}

		if(!retrieveCard) {
			if(AbstractDungeon.cardRewardScreen.discoveryCard != null) {
				AbstractCard card = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
				card.current_x = -1000f * Settings.scale;

				if(AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
					AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
				}else{
					AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
				}
				card.setCostForTurn(-1);
				card.isCostModifiedForTurn = true;
				AbstractDungeon.cardRewardScreen.discoveryCard = null;
			}
			retrieveCard = true;
		}
		tickDuration();
	}
}
