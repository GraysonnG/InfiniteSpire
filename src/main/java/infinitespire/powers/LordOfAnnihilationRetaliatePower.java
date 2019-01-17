package infinitespire.powers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.LordOfAnnihilation;

public class LordOfAnnihilationRetaliatePower extends AbstractPower {

    public static final String powerID = InfiniteSpire.createID("DivineShieldPower");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

    LordOfAnnihilation gOwner;
    private boolean intentChanged = false;

    public LordOfAnnihilationRetaliatePower(LordOfAnnihilation owner){
        this.owner = owner;
        this.gOwner = owner;
        this.amount = -1;
        this.name = strings.NAME;
        this.ID = powerID;
        this.img = InfiniteSpire.Textures.getPowerTexture("retaliate.png");
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void atEndOfRound() {
        intentChanged = false;
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        for(AbstractPower p : owner.powers){
            if(!intentChanged && p.type == PowerType.DEBUFF) {
                gOwner.changeIntentToNuke();
                intentChanged = true;
                return;
            }
        }
    }

    @Override
    public void updateDescription(){
        this.description = strings.DESCRIPTIONS[0];
    }
}
