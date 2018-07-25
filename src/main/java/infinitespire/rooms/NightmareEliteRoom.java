package infinitespire.rooms;

import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import infinitespire.InfiniteSpire;
import infinitespire.monsters.Nightmare;

public class NightmareEliteRoom extends MonsterRoomElite {
	public NightmareEliteRoom() {
		super();
		this.mapSymbol = "NM";
		this.mapImg = InfiniteSpire.getTexture("img/ui/map/nightmareelite.png");
		this.mapImgOutline = InfiniteSpire.getTexture("img/ui/map/nightmareelite-outline.png");
	}
	
	@Override
	public void onPlayerEntry() {
		this.playBGM(null);
		MonsterGroup group = new MonsterGroup(new Nightmare());
		(this.monsters = group).init();
		waitTimer = 0.1f;
	}
}
