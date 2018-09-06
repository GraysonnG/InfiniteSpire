package infinitespire.relics;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

public class SolderingIron extends Relic {

    public static final String ID = InfiniteSpire.createID("SolderingIron");

    public SolderingIron(){
        super(ID,"solderingiron", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SolderingIron();
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToBottom(new IncreaseMaxOrbAction(1));
    }
}
