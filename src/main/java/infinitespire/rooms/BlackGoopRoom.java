package infinitespire.rooms;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import infinitespire.InfiniteSpire;

public class BlackGoopRoom extends AbstractRoom {
	
	public BlackGoopRoom() {
		this.phase = RoomPhase.COMPLETE;
		this.mapSymbol = "GOOP";
		this.mapImg = InfiniteSpire.getTexture("img/ui/map/blackGoop.png");
		this.mapImgOutline = InfiniteSpire.getTexture("img/ui/map/blackGoop-outline.png");
	}

	@Override
	public CardRarity getCardRarity(int arg0) {
		return AbstractCard.CardRarity.RARE;
	}

	@Override
	public void onPlayerEntry() {
		AbstractDungeon.overlayMenu.proceedButton.setLabel("Finish.");
		AbstractDungeon.overlayMenu.proceedButton.show();
	}

}
