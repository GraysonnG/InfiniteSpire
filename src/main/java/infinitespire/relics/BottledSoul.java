package infinitespire.relics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import infinitespire.InfiniteSpire;

public class BottledSoul extends AbstractRelic {
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());

	public static final String ID = "Bottled Soul";
	public static final String NAME = "Bottled Soul";
	private boolean cardSelected;
	public AbstractCard card;
	private AbstractRoom.RoomPhase prevPhase;
	
	public BottledSoul() {
		super(ID, "", RelicTier.UNCOMMON, LandingSound.CLINK);
		Texture texture = InfiniteSpire.getTexture("img/relics/bottledsoul.png");
		Texture outline = InfiniteSpire.getTexture("img/relics/bottledsoul-outline.png");
		img = texture;
		largeImg = texture;
		outlineImg = outline;
		this.cardSelected = true;
		this.card = null;
	}
	
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}
	
	public AbstractCard getCard() {
		return this.card.makeCopy();
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
			AbstractDungeon.gridSelectScreen.open(group, 1, "Select a Card.", false, false, false, false);
		}else {
			cardSelected = true;
			AbstractDungeon.getCurrRoom().phase = prevPhase;
		}
	}
	
	public void onUnequip() {
		if(this.card != null) {
			AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(this.card);
			if(cardInDeck != null) {
				cardInDeck.exhaust = true;
			}
		}
	}
	
	public void update() {
		super.update();
		if(!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			this.cardSelected = true;
			this.card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
			//change the card to not exhaust
			this.card.exhaust = false;
			//set a variable in InfiniteSpire that will allow a patch to render the icon on the card
			AbstractDungeon.getCurrRoom().phase = prevPhase;
			//reset description and tips
		}
	}
	
	
	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		if(c != null && c == this.card) {
			this.flash();
			AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		}
	}

	@Override
	public AbstractRelic makeCopy() {
		return new BottledSoul();
	}

}
