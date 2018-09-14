package infinitespire.relics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

public class MutagenicDexterity extends Relic{

    public static final String ID = InfiniteSpire.createID("MutagenicDexterity");

    public MutagenicDexterity(){
        super(ID, "mutagen2", RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        GameActionManager manager = AbstractDungeon.actionManager;
        AbstractPlayer player = AbstractDungeon.player;

        manager.addToBottom(new ApplyPowerAction(player, player, new DexterityPower(player,  3),3));
        manager.addToBottom(new ApplyPowerAction(player, player, new LoseDexterityPower(player, 3),3));
        manager.addToBottom(new RelicAboveCreatureAction(player, this));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MutagenicDexterity();
    }
}
