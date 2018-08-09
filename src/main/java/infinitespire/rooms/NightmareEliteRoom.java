package infinitespire.rooms;

import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import coloredmap.ColoredRoom;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.Nightmare;

@ColoredRoom
public class NightmareEliteRoom extends MonsterRoomElite {
	public NightmareEliteRoom() {
		super();
		this.mapSymbol = "NM";
		this.phase = RoomPhase.COMBAT;
		this.mapImg = InfiniteSpire.getTexture("img/infinitespire/ui/map/nightmareelite.png");
		this.mapImgOutline = InfiniteSpire.getTexture("img/infinitespire/ui/map/nightmareelite-outline.png");
		this.eliteTrigger = true;
	}
	
	@Override
	public void onPlayerEntry() {
		this.playBGM(null);
		MonsterGroup group = new MonsterGroup(new Nightmare());
		this.monsters = group;
		this.monsters.init();
		waitTimer = 0.1f;
	}
}
