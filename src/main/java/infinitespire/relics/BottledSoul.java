package infinitespire.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomBottleRelic;
import basemod.interfaces.StartGameSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.BottleRainField;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;
import infinitespire.patches.AbstractCardPatch;

import java.io.IOException;
import java.util.function.Predicate;

//WHY WONT YOU JUST WORK GAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHH
public class BottledSoul extends Relic implements CustomBottleRelic, StartGameSubscriber{

	public static final String ID = InfiniteSpire.createID("Bottled Soul");
	private static final String CONFIG_KEY = "bottledSoul";
	private boolean cardSelected = true;
	private AbstractCard card = null;

	public BottledSoul(){
		super(ID, "bottledsoul", RelicTier.UNCOMMON, LandingSound.CLINK);
		BaseMod.subscribe(this);
	}

	@Override
	public Predicate<AbstractCard> isOnCard() {
		return AbstractCardPatch.Field.isBottledSoulCard::get;
	}

	public AbstractCard getCard(){
		return card.makeCopy();
	}

	public static void save(SpireConfig config){
		if(AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(BottledSoul.ID)){
			BottledSoul relic = (BottledSoul) AbstractDungeon.player.getRelic(ID);
			config.setInt(CONFIG_KEY, AbstractDungeon.player.masterDeck.group.indexOf(relic.card));
		}else{
			config.remove(CONFIG_KEY);
		}
	}

	public static void load(SpireConfig config){
		if(AbstractDungeon.player.hasRelic(ID) && config.has(CONFIG_KEY)){
			BottledSoul relic = (BottledSoul) AbstractDungeon.player.getRelic(ID);
			int cardIndex = config.getInt(CONFIG_KEY);

			if(cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()){
				relic.card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
				if(relic.card != null){
					AbstractCardPatch.Field.isBottledSoulCard.set(relic.card, true);
					//setdescription after loading
				}
			}
		}
	}

	public static void clear(){};

	@Override
	public void onEquip() {
		cardSelected = false;

		CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
		for(AbstractCard card : AbstractDungeon.player.masterDeck.group) {
			if (card.exhaust) {
				group.addToBottom(card);
			}
		}

		if(group.size() > 0){
			if(AbstractDungeon.isScreenUp){
				AbstractDungeon.dynamicBanner.hide();
				AbstractDungeon.overlayMenu.cancelButton.hide();
				AbstractDungeon.previousScreen = AbstractDungeon.screen;
			}
			AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
			AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(group), 1, "Select a card.",
				false, false, false, false);
		}
	}

	@Override
	public void onUnequip() {
		if(card != null){
			AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card);
			if(cardInDeck != null){
				AbstractCardPatch.Field.isBottledSoulCard.set(cardInDeck, false);
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

	private void renderCardPreview(SpriteBatch sb){
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
	public void update(){
		super.update();

		if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			cardSelected = true;
			card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
			BottleRainField.inBottleRain.set(card, true);
			AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

			AbstractDungeon.gridSelectScreen.selectedCards.clear();
		}
	}

	@Override
	public void receiveStartGame() {
		try {
			SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
			config.load();

			BottledSoul.load(config);

		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
