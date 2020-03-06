package infinitespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class KillAllMonstersAction extends AbstractGameAction {

    public KillAllMonstersAction() {
        this.duration = Settings.ACTION_DUR_MED;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        tickDuration();
        if(this.duration <= 0) {
            for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                m.currentHealth = 0;
            }
            AbstractDungeon.getCurrRoom().endBattle();
        }
    }
}
