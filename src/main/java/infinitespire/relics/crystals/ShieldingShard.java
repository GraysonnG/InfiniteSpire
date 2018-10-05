package infinitespire.relics.crystals;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.InfiniteRelic;

public class ShieldingShard extends InfiniteRelic{

	public static final String ID = InfiniteSpire.createID("ShieldingShard");

	public ShieldingShard(){
		super(ID, "focusingshard", LandingSound.CLINK);
		this.counter = 4;
	}

	@Override
	public void onVictory() {
		if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
			this.counter += 2;
		}
	}

	@Override
	public void atBattleStart() {
		AbstractPlayer p = AbstractDungeon.player;
		AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(p, p, counter));
	}
}
