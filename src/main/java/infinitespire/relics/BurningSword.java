package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;
import infinitespire.powers.CriticalPower;

public class BurningSword extends Relic {

	public static final String ID = InfiniteSpire.createID("Burning Sword");
	public static int attacksPlayed = 0;
	
	public BurningSword() {
		super(ID, "burningsword", RelicTier.BOSS, LandingSound.MAGICAL);
		attacksPlayed = 0;
	}

	@Override
	public AbstractRelic makeCopy() {
		return new BurningSword();
	}

	@Override
	public void atTurnStart() {
		attacksPlayed = 0;
		AbstractDungeon.actionManager.addToBottom(
			new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new CriticalPower(AbstractDungeon.player), 1));
	}

	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		if(c.type == AbstractCard.CardType.ATTACK && attacksPlayed == 0){
			AbstractCard burn = new Burn();
			burn.upgrade();
			AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(burn));
			attacksPlayed++;
		}
	}
}
