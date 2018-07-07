package infinitespire.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SpectralDust extends Relic {

	public static final String ID = "Spectral Dust";
	
	public SpectralDust() {
		super(ID, "spectraldust", RelicTier.UNCOMMON, LandingSound.MAGICAL);
	}

	@Override
	public AbstractRelic makeCopy() {
		return new SpectralDust();
	}

	@Override
	public void onExhaust(AbstractCard card) {
		if(card.isEthereal) {
			DamageInfo dInfo = new DamageInfo(null, 3);
			AbstractDungeon.actionManager.addToTop(new DamageRandomEnemyAction(dInfo, AttackEffect.BLUNT_LIGHT));
		}
	}	
}
