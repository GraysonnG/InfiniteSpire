package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

public class Satchel extends Relic {

	public static final String ID = InfiniteSpire.createID("Satchel");
	
	public Satchel() {
		super(ID, "satchel", RelicTier.COMMON, LandingSound.SOLID);
	}
	
	@Override
	public boolean isQuestRelic() {
		return true;
	}

	@Override
	public AbstractRelic makeCopy() {
		return new Satchel();
	}

	@Override
	public void atBattleStart() {
		AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RetainCardPower(AbstractDungeon.player, 1), 1));
	}

	
}
