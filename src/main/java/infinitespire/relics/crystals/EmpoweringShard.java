package infinitespire.relics.crystals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.InfiniteRelic;

public class EmpoweringShard extends InfiniteRelic{

    public static final String ID = InfiniteSpire.createID("EmpoweringShard");

    public EmpoweringShard(){
        super(ID, "empoweringshard", RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                new StrengthPower(AbstractDungeon.player, this.counter), this.counter));
    }
}
