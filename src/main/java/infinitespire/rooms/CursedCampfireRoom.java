package infinitespire.rooms;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import infinitespire.InfiniteSpire;

public class CursedCampfireRoom extends AbstractRoom {

	public CursedCampfireRoom() {
		this.phase = RoomPhase.EVENT;
		this.mapSymbol = "CRSD";
		this.mapImg = InfiniteSpire.getTexture("img/ui/map/cursedCampfireImg.png");
		this.mapImgOutline = InfiniteSpire.getTexture("img/ui/map/cursedCampfireImg-outline.png");
	}
	
	
	@Override
	public CardRarity getCardRarity(int arg0) {
		return AbstractCard.CardRarity.RARE;
	}

	@Override
	public void onPlayerEntry() {
		
	}

}
