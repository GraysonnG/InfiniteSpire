package infinitespire.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CubicDiamond extends Relic {

	public static final String ID = "Cubic Diamond";
	public static final String NAME = "Cubic Diamond";
	
	public CubicDiamond() {
		super(ID, "cubicdiamond", RelicTier.SPECIAL, LandingSound.CLINK);
	}

	@Override
	public AbstractRelic makeCopy() {
		return new CubicDiamond();
	}
	
	private AbstractRelic selectRelic(AbstractRelic original) {
		AbstractRelic relicToAdd = RelicLibrary.starterList.get(AbstractDungeon.cardRandomRng.random(RelicLibrary.starterList.size() - 1));;
		
		if(relicToAdd.relicId.equals(original.relicId)) {
			relicToAdd = selectRelic(original);
		}
		
		return relicToAdd;
	}

	//fix this to make it work right
	@Override
	public void onEquip() {
		AbstractRelic relicToAdd;
		for(AbstractRelic relic : AbstractDungeon.player.relics) {
			if(relic.tier == RelicTier.STARTER) {
				relicToAdd = selectRelic(relic);
				relicToAdd.instantObtain(AbstractDungeon.player, 0, false);
				relicToAdd.playLandingSFX();
				relicToAdd.flash();
				break;
			}
		}
	}
}
