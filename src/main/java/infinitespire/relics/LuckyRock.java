package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.powers.CriticalPower;

public class LuckyRock extends Relic {

	private static final String ID = "Lucky Rock";

	public LuckyRock() {
		super(ID, "luckyrock", RelicTier.COMMON, LandingSound.SOLID);
	}

	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		if(willCrit(0.05f, info)) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CriticalPower(AbstractDungeon.player), 1));
		}
	}

	@Override
	public AbstractRelic makeCopy() {
		return new LuckyRock();
	}

	private static boolean willCrit(float chance, DamageInfo info) {
		boolean retVal = false;
		
		if(!(info.type == DamageType.NORMAL))return false;
		
		float roll = (float)Math.random();
		if(roll < chance) {
			retVal = true;
		}
		
		return retVal;
	}
}