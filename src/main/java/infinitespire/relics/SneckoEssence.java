package infinitespire.relics;

import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

public class SneckoEssence extends Relic {

	public static final String ID = InfiniteSpire.createID("SneckoEssence");

	public SneckoEssence() {
		super(ID, "bottledsoul", RelicTier.SPECIAL, LandingSound.HEAVY);
	}

	@Override
	public void onEquip() {
		EnergyManager energy = AbstractDungeon.player.energy;
		++energy.energyMaster;
	}

	@Override
	public void onUnequip() {
		EnergyManager energy = AbstractDungeon.player.energy;
		--energy.energyMaster;
	}
}
