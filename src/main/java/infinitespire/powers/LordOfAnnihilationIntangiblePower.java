package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.ShieldPylon;

public class LordOfAnnihilationIntangiblePower extends IntangiblePower {

    public static final String powerID = InfiniteSpire.createID("LOA_Intangible");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

    public LordOfAnnihilationIntangiblePower(AbstractCreature owner, int turns) {
        super(owner, turns);
        this.ID = powerID;
        this.updateDescription();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(info.owner instanceof ShieldPylon){
            return damageAmount;
        } else {
            return 1;
        }
    }

    @Override
    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }
}
