package infinitespire.rooms;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import infinitespire.events.EndRunEvent;

public class EndRunRoom extends EventRoom {
	public AbstractRoom originalRoom;

	public EndRunRoom(AbstractRoom originalRoom) {
		this.originalRoom = originalRoom;
	}

	@Override
	public void onPlayerEntry() {
		AbstractDungeon.overlayMenu.proceedButton.hide();
		this.event = new EndRunEvent();
		this.event.onEnterRoom();
	}
}
