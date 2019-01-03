package infinitespire.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;
import infinitespire.patches.AbstractCardPatch;

import java.util.function.Predicate;

public class BottledMercury extends Relic implements CustomBottleRelic, CustomSavable<Integer> {

	public static final String ID = InfiniteSpire.createID("BottledMercury");
	private boolean cardSelected = false;
	private AbstractCard card = null;

	public BottledMercury(){
		super(ID,"bottledMercury",RelicTier.UNCOMMON, LandingSound.CLINK);
	}

	@Override
	public Predicate<AbstractCard> isOnCard() {
		return AbstractCardPatch.Field.isBottledMercuryCard::get;
	}

	@Override
	public Integer onSave() {
		return AbstractDungeon.player.masterDeck.group.indexOf(card);
	}

	@Override
	public void onLoad(Integer cardIndex) {
		if(cardIndex == null) {
			return;
		}

		if(cardIndex >=0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
			card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
			if(card != null) {
				AbstractCardPatch.Field.isBottledMercuryCard.set(card, true);
			}
		}
	}

	@Override
	public void renderTip(SpriteBatch sb){
		super.renderTip(sb);

		if (InputHelper.mX < 1400.0f * Settings.scale && card != null) {
			renderCardPreview(sb);
		}
	}

	public void renderCardPreview(SpriteBatch sb) {
		AbstractCard renderCard = card.makeStatEquivalentCopy();
		if(renderCard != null){
			renderCard.drawScale = 0.5f;
			renderCard.current_x = InputHelper.mX + (renderCard.hb.width / 2f) + 10f * Settings.scale +
				this.hb.width + 280.0f * Settings.scale;
			renderCard.current_y = InputHelper.mY - renderCard.hb.height / 2f;
			renderCard.render(sb);
		}
	}

	@Override
	public void onEquip() {
		cardSelected = false;
		if(AbstractDungeon.isScreenUp) {
			AbstractDungeon.dynamicBanner.hide();
			AbstractDungeon.overlayMenu.cancelButton.hide();
			AbstractDungeon.previousScreen = AbstractDungeon.screen;
		}
		AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
		AbstractDungeon.gridSelectScreen.open(
			CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck),
			1,
			"Pick a Card.",
			false,
			false,
			false,
			false
		);
	}

	@Override
	public void onUnequip() {
		if(card != null) {
			AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card);
			if(cardInDeck != null) {
				AbstractCardPatch.Field.isBottledMercuryCard.set(cardInDeck, false);
			}
		}
	}

	@Override
	public void update() {
		super.update();
		if(!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			cardSelected = true;
			card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
			AbstractCardPatch.Field.isBottledMercuryCard.set(card, true);
			AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
			AbstractDungeon.gridSelectScreen.selectedCards.clear();
		}
	}

	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		GameActionManager manager = AbstractDungeon.actionManager;
		AbstractPlayer player = AbstractDungeon.player;

		if(AbstractCardPatch.Field.isBottledMercuryCard.get(c)) {
			this.flash();
			manager.addToBottom(new RelicAboveCreatureAction(player, this));
			switch(c.type){
				case ATTACK:
					manager.addToBottom(new ApplyPowerAction(player, player, new StrengthPower(player, 1), 1));
					break;
				case SKILL:
					manager.addToBottom(new ApplyPowerAction(player, player, new DexterityPower(player, 1), 1));
					break;
				case POWER:
					manager.addToBottom(new ApplyPowerAction(player, player, new FocusPower(player, 2), 2));
					break;
				default:
					break;
			}
		}
	}
}
