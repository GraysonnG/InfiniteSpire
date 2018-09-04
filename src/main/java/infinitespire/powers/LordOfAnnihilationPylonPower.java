package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.monsters.LordOfAnnihilation;
import infinitespire.monsters.ShieldPylon;
import infinitespire.util.TextureLoader;

public class LordOfAnnihilationPylonPower extends AbstractPower{
    public LordOfAnnihilationPylonPower(LordOfAnnihilation owner){
        this.name = "Pylon Shield";
        this.ID = "is_PylonPower";
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.img = TextureLoader.getTexture("img/infinitespire/powers/pylonPower.png");
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
        this.description = "As long as there is at least #b1 #yShield #yPylon, gain #b1 #yIntangible at the end of the turn.";
    }
}
