package infinitespire.relics;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;
import infinitespire.effects.BlackCardEffect;
import infinitespire.helpers.CardHelper;

public class DarkRift extends Relic {

	public static final String ID = InfiniteSpire.createID("DarkRift");

	public DarkRift() {
		super(ID, "darkRift", RelicTier.STARTER, LandingSound.MAGICAL);
	}

	@Override
	public void atBattleStart() {
		this.flash();
		AbstractDungeon.actionManager.addToBottom(new VFXAction(new BlackCardEffect()));
		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(CardHelper.getRandomBlackCard(), 1, true, true));
	}
}
