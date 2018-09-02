package infinitespire.relics;

import basemod.BaseMod;
import basemod.interfaces.StartGameSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import infinitespire.InfiniteSpire;
import infinitespire.patches.AbstractCardPatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class BottledSoul extends AbstractRelic implements StartGameSubscriber{
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());

	public static final String ID = InfiniteSpire.createID("Bottled Soul");
	public static final String NAME = "Bottled Soul";
	private static boolean cardSelected;
	private static final int MAX_VALUE = 9999999;
	private static int cardIndex = MAX_VALUE;
	public static AbstractCard card;
	private AbstractRoom.RoomPhase prevPhase;
	
	public BottledSoul() {
		super(ID, "", RelicTier.UNCOMMON, LandingSound.CLINK);
		Texture texture = InfiniteSpire.getTexture("img/infinitespire/relics/bottledsoul.png");
		Texture outline = InfiniteSpire.getTexture("img/infinitespire/relics/bottledsoul-outline.png");
		img = texture;
		largeImg = texture;
		outlineImg = outline;
		cardSelected = true;
		BottledSoul.card = null;
		BaseMod.subscribe(this);
	}
	
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}
	
	public AbstractCard getCard() {
		return card.makeCopy();
	}

	@Override
	public void renderTip(SpriteBatch sb){
		super.renderTip(sb);
		if (InputHelper.mX < 1400.0f * Settings.scale && card != null) {
			renderCardPreview(sb);
		}
	}


	private void renderCardPreview(SpriteBatch sb) {
		AbstractCard renderableCard = card.makeStatEquivalentCopy();

		if (renderableCard != null && this.hb.hovered) {
			renderableCard.drawScale = 0.5f;
			renderableCard.current_x = InputHelper.mX + (renderableCard.hb.width / 2f) + 10f * Settings.scale +
					this.hb.width + 280.0f * Settings.scale;
			renderableCard.current_y = InputHelper.mY - renderableCard.hb.height / 2f;
			renderableCard.render(sb);
		}

	}

	@Override
	public void onMasterDeckChange() {
		cardIndex = AbstractDungeon.player.masterDeck.group.indexOf(card);
		bottleCard(card);
	}

	public void onEquip() {
		cardSelected = false;
		if(AbstractDungeon.isScreenUp) {
			AbstractDungeon.dynamicBanner.hide();
			AbstractDungeon.overlayMenu.cancelButton.hide();
			AbstractDungeon.previousScreen = AbstractDungeon.screen;
		}
		
		prevPhase = AbstractRoom.RoomPhase.valueOf(AbstractDungeon.getCurrRoom().phase.toString());
		
		AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
		
		CardGroup group = new CardGroup(CardGroupType.UNSPECIFIED);
		for(AbstractCard card : AbstractDungeon.player.masterDeck.group) {
			if(card.exhaust) {
				group.addToBottom(card);
			}
		}
		if(group.size() > 0) {
			AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(group), 1, "Select a Card.", false, false, false, false);
		}else {
			cardSelected = true;
			AbstractDungeon.getCurrRoom().phase = prevPhase;
		}
	}

	//this is what happens when you unequip the relic
	public void onUnequip() {
		cardIndex = MAX_VALUE;
		AbstractCardPatch.Field.isBottledSoulCard.set(card, false);
		cardSelected = false;
		card = null;
	}

	public static void save(SpireConfig config) {
		if(AbstractDungeon.player != null)
	    	cardIndex = AbstractDungeon.player.masterDeck.group.indexOf(card);

		if(cardIndex == -1){
			cardIndex = MAX_VALUE;
		}

	    config.setInt("bottledSoulIndex", cardIndex);
		config.setBool("bottledSoulHasCard", cardSelected);
	    InfiniteSpire.logger.info("Bottled Soul saved with index: " + cardIndex);
		InfiniteSpire.logger.info("Bottled Soul has card: " + cardSelected);
	}
	
	public static void load(SpireConfig config) {
		cardIndex = config.getInt("bottledSoulIndex");
		cardSelected = config.getBool("bottledSoulHasCard");
	}

	public static void clear(){
		cardIndex = MAX_VALUE;
		card = null;
		cardSelected = false;
	}
	
	public void update() {
		super.update();
		if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			cardSelected = true;
			card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
			bottleCard(card);
			AbstractDungeon.getCurrRoom().phase = prevPhase;
			InfiniteSpire.logger.info("Bottled Soul: " + card.name);
			AbstractDungeon.gridSelectScreen.selectedCards.clear();
		}

		if (BottledSoul.card == null && BottledSoul.cardIndex != MAX_VALUE && BottledSoul.cardIndex != -1) {
			if (AbstractDungeon.player != null) {
				card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
				bottleCard(card);
			}
		}
	}

	private void bottleCard(AbstractCard card){
		cardIndex = AbstractDungeon.player.masterDeck.group.indexOf(card);
        AbstractCardPatch.Field.isBottledSoulCard.set(card, true);
        AbstractDungeon.player.hand.refreshHandLayout();
    }

	@Override
	public AbstractRelic makeCopy() {
		return new BottledSoul();
	}

	@Override
	public void receiveStartGame() {
		try {
			SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
			config.load();
			load(config);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Bottled Soul.");
		}
	}
}
