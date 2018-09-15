package infinitespire.relics.crystals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.InfiniteRelic;

public class WardingShard extends InfiniteRelic {

    public static final String ID = InfiniteSpire.createID("WardingShard");

    public WardingShard(){
        super(ID, "wardingshard", LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                new DexterityPower(AbstractDungeon.player, counter), counter));
    }
}
