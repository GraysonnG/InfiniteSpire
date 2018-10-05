package infinitespire.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.LordOfAnnihilation;

public class LordOfAnnihilationRetaliatePower extends AbstractPower {

    LordOfAnnihilation gOwner;

    public LordOfAnnihilationRetaliatePower(LordOfAnnihilation owner){
        this.owner = owner;
        this.gOwner = owner;
        this.amount = 0;
        this.name = "Divine Shield";
        this.ID = "is_DivineShield";
        this.img = InfiniteSpire.getTexture("img/infinitespire/powers/retaliate.png");
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if(power.type == PowerType.DEBUFF){
            gOwner.changeIntentToNuke();
        }
    }

    @Override
    public void updateDescription(){
        this.description = "When ??? is inflicted with a debuff, he will retaliate with Ultimate Memoricia.";
    }
}
