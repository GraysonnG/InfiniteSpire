package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

public class BurningSword extends Relic {

	public static final String ID = InfiniteSpire.createID("Burning Sword");
	
	public BurningSword() {
		super(ID, "burningsword", RelicTier.COMMON, LandingSound.MAGICAL);
	}

	@Override
	public AbstractRelic makeCopy() {
		return new BurningSword();
	}

	@Override
	public void atBattleStart() {
		AbstractPlayer p = AbstractDungeon.player;
		AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, 2), 2));
	}
}
