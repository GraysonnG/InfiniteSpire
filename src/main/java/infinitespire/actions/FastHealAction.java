package infinitespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class FastHealAction extends AbstractGameAction {
    public FastHealAction(AbstractCreature target, AbstractCreature source, int amount){
        this.setValues(target, source, amount);
        this.actionType = ActionType.HEAL;
        this.duration = 0.1f;

    }

    public void update(){
        if(this.duration == 0.1f){
            this.target.heal(this.amount);
        }
        this.tickDuration();
    }
}
