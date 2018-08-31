package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.abstracts.Relic;
import infinitespire.powers.BeetleShellPower;

public class BeetleShell extends Relic {

	public static final String ID = "Beetle Shell";
	
	public BeetleShell() {
		super(ID, "beetleshell", RelicTier.COMMON, LandingSound.SOLID);
		counter = 0;
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new BeetleShell();
	}
	
	@Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if (card.baseBlock > 0) {
            ++this.counter;
            if (this.counter == 10) {
                this.counter = 0;
                this.flash();
                this.pulse = false;
            }
            
            if (this.counter == 9) {
                this.beginPulse();
                this.pulse = true;
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BeetleShellPower(AbstractDungeon.player), 1, true));
            }
        }
    }

	@Override
	public void atBattleStart() {
		AbstractPlayer p = AbstractDungeon.player;
		if (this.counter == 9) {
            this.beginPulse();
            this.pulse = true;
            AbstractDungeon.player.hand.refreshHandLayout();
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p,p, new BeetleShellPower(p, true)));
		}
	}
}
