package infinitespire.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnChannelRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

//TODO: convert to use stslib OnChannelRelic
public class SolderingIron extends Relic implements OnChannelRelic {

    public static final String ID = InfiniteSpire.createID("SolderingIron");

    public SolderingIron(){
        super(ID,"solderingiron", RelicTier.UNCOMMON, LandingSound.FLAT);
        this.counter = 0;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SolderingIron();
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onChannel(AbstractOrb abstractOrb) {
        counter++;
        if(counter % 3 == 0){
            this.flash();
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.player.increaseMaxOrbSlots(1, true);
        }
    }
}
