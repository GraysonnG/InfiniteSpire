package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.LordOfAnnihilation;
import infinitespire.monsters.ShieldPylon;

public class LordOfAnnihilationPylonPower extends AbstractPower{

    public static final String powerID = InfiniteSpire.createID("PylonPower");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);


    public LordOfAnnihilationPylonPower(LordOfAnnihilation owner){
        this.name = strings.NAME;
        this.ID = powerID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.img = InfiniteSpire.Textures.getPowerTexture("pylonPower.png");
        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(!isPlayer){
            int numOfPylons = 0;
            for(AbstractMonster m : AbstractDungeon.getMonsters().monsters){
                if(m instanceof ShieldPylon){
                    numOfPylons ++;
                }
            }

            if(numOfPylons > 0) {
                AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(owner, owner, new LordOfAnnihilationIntangiblePower(owner, 1), 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0];
    }
}
