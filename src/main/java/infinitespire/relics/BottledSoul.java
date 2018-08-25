package infinitespire.relics;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import infinitespire.patches.AbstractCardPatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import infinitespire.InfiniteSpire;

import java.io.IOException;

public class BottledSoul extends AbstractRelic implements StartGameSubscriber{
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());

	public static final String ID = "Bottled Soul";
	public static final String NAME = "Bottled Soul";
	private boolean cardSelected;
	private int cardIndex = -1;
	public AbstractCard card;
	private AbstractRoom.RoomPhase prevPhase;
	
	public BottledSoul() {
		super(ID, "", RelicTier.UNCOMMON, LandingSound.CLINK);
		Texture texture = InfiniteSpire.getTexture("img/infinitespire/relics/bottledsoul.png");
		Texture outline = InfiniteSpire.getTexture("img/infinitespire/relics/bottledsoul-outline.png");
		img = texture;
		largeImg = texture;
		outlineImg = outline;
		this.cardSelected = true;
		this.card = null;

		BaseMod.subscribe(this);
	}
	
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}
	
	public AbstractCard getCard() {
		return this.card.makeCopy();
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
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		
	}

	public void onEquip() {
		this.cardSelected = false;
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
		this.cardIndex = -1;
		AbstractCardPatch.Field.isBottledSoulCard.set(card, false);
		this.card = null;
	}

	public void save() {
	    this.cardIndex = AbstractDungeon.player.masterDeck.group.indexOf(card);
		try {
            SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
            config.setInt("bottledSoulIndex", cardIndex);
			InfiniteSpire.logger.info("Bottled Soul saved with index: " + cardIndex);
            config.save();
        }catch(IOException e){
		    e.printStackTrace();
        }
	}
	
	public void load(SpireConfig config) {
		this.cardIndex = config.getInt("bottledSoulIndex");
		this.card = AbstractDungeon.player.masterDeck.group.get(this.cardIndex);
		bottleCard(card);
	}
	
	public void update() {
		super.update();
		if(!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			this.cardSelected = true;
			this.card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
			bottleCard(card);
			AbstractDungeon.getCurrRoom().phase = prevPhase;
			InfiniteSpire.logger.info("Bottled Soul: "+ this.card.name);
			AbstractDungeon.gridSelectScreen.selectedCards.clear();
		}
	}

	private void bottleCard(AbstractCard card){
		if (cardIndex == -1) {
			this.cardIndex = AbstractDungeon.player.masterDeck.group.indexOf(card);
		}
        AbstractCardPatch.Field.isBottledSoulCard.set(card, true);
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
			this.load(config);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Bottled Soul.");
		}
	}
}
