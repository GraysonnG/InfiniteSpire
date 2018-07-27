package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.powers.GolemPower;

public class GolemsMask extends Relic{
	public static final String ID = "Golems Mask";
	public static final String NAME = "Golem's Mask";
	
	private boolean isFirstTurn;
	
	public GolemsMask() {
		super(ID, "golemsstart", RelicTier.BOSS, LandingSound.SOLID);
	}

	@Override
	public AbstractRelic makeCopy() {
		return new GolemsMask();
	}
	
	@Override
	public void atBattleStart() {
		isFirstTurn = true;
	}

	@Override
	public void atTurnStart() {
		if(isFirstTurn) {
//			AbstractDungeon.actionManager.cardQueue.clear();
//            for (final AbstractCard c : AbstractDungeon.player.limbo.group) {
//                AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
//            }
//            AbstractDungeon.player.limbo.group.clear();
//            AbstractDungeon.player.releaseCard();
//            AbstractDungeon.overlayMenu.endTurnButton.disable(true);
            
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new GolemPower(AbstractDungeon.player)));
			isFirstTurn = false;
		}
		
		this.flash();
	}
	
	
}
