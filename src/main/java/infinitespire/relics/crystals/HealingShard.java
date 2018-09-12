package infinitespire.relics.crystals;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.InfiniteRelic;

public class HealingShard extends InfiniteRelic{
    public static final String ID = InfiniteSpire.createID("HealingShard");

    public HealingShard(){
        super(ID, "healingshard", RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void onVictory() {
        super.onVictory();
        this.flash();
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        final AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth > 0) {
            p.heal(this.counter);
        }
    }
}
