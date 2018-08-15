package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.abstracts.Relic;
import infinitespire.powers.JokerCardPower;

public class JokerCard extends Relic {

	public static final String ID = "Joker Card";
	
	public JokerCard() {
		super(ID, "jokercard", RelicTier.RARE, LandingSound.FLAT);
		this.counter = 0;
	}

	@Override
	public AbstractRelic makeCopy() {
		return new JokerCard();
	}

	@Override
	public void atBattleStart() {
		AbstractPlayer p = AbstractDungeon.player;
		if(this.counter == 9) {
			this.beginPulse();
			this.pulse = true;
			AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new JokerCardPower(p)));
		}
	}

	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		if(AbstractDungeon.player.hasPower("is_JokerCardPower"))
			return;
		
		this.counter++;
		if(this.counter == 10) {
			this.counter = 0;
			this.flash();
			this.pulse = false;
		}
		
		if(this.counter == 9) {
			this.beginPulse();
			this.pulse = true;
			AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new JokerCardPower(AbstractDungeon.player)));
		}
	}

	
}
