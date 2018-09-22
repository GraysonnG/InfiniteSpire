package infinitespire.relics.crystals;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.InfiniteRelic;

public class FocusingShard extends InfiniteRelic{
    public static final String ID = InfiniteSpire.createID("FocusingShard");

    public FocusingShard(){
        super(ID, "focusingshard", LandingSound.CLINK);
    }

    @Override
    public void atBattleStartPreDraw() {
        GameActionManager manager = AbstractDungeon.actionManager;
        AbstractPlayer player = AbstractDungeon.player;
        CardGroup drawPile = player.drawPile.getUpgradableCards();

        for(int i = 0; i < this.counter; i++) {
            AbstractCard card = drawPile.group.get(AbstractDungeon.miscRng.random(drawPile.size() - 1));
            card.upgrade();
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
        }
    }
}
