package infinitespire.rooms;

import coloredmap.ColoredRoom;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.Nightmare;
import infinitespire.rewards.BlackCardRewardItem;
import infinitespire.rewards.VoidShardReward;

@ColoredRoom
public class NightmareEliteRoom extends MonsterRoomElite {
	public boolean shouldBeAlpha;

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
		shouldBeAlpha = AbstractDungeon.monsterRng.randomBoolean(0.1f * Nightmare.timesDefeated) || Settings.isDebug;

		if(AbstractDungeon.bossCount <= 2) shouldBeAlpha = false;

		AbstractDungeon.lastCombatMetricKey = Nightmare.ID + (shouldBeAlpha ? "_Alpha" : "");
		MonsterGroup group = new MonsterGroup(new Nightmare(shouldBeAlpha));

		this.monsters = group;
		this.monsters.init();
		waitTimer = 0.1f;
	}

	@Override
	public void dropReward() {
		super.dropReward();
		float cardChance = 0.1f * (Nightmare.timesNotReceivedBlackCard + 1);
		if(shouldBeAlpha) cardChance *= 2f;

		this.rewards.add(new VoidShardReward(shouldBeAlpha ? 6 : 3));

		if(AbstractDungeon.miscRng.randomBoolean(cardChance)) {
			this.rewards.add(new BlackCardRewardItem());
			Nightmare.timesNotReceivedBlackCard = 0;
		}
	}
}
