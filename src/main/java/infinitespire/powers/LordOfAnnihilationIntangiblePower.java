package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import infinitespire.monsters.ShieldPylon;

public class LordOfAnnihilationIntangiblePower extends IntangiblePower {

    public LordOfAnnihilationIntangiblePower(AbstractCreature owner, int turns) {
        super(owner, turns);
        this.ID = "inf_loaIntangible";
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
