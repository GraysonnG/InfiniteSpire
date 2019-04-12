package infinitespire.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

public class CheckeredPen extends Relic {

	public static final String ID = InfiniteSpire.createID("CheckeredPen");

	public CheckeredPen() {
		super(ID, "checkeredpen", RelicTier.UNCOMMON, LandingSound.CLINK);
	}

	@Override
	public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
		AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(null, 1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
	}
}
