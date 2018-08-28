package infinitespire.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;

import java.util.*;

public class Guardian extends AbstractMonster{
    public static final String ID = "Guardian";
    private static final String NAME = "Guardian of the Spire";

    private final ArrayList<Integer> THRESHOLDS;
    private static final int[] ATTACKS = {15, 20, 25, 50};
    private static final int[] DEFENDS = {5, 10, 20, 50};
    private static final int NUKE = 999999;
    private Queue<Move> moveQueue = new LinkedList<Move>();

    private int phase;
    private int turn = 0;

    public Guardian() {
        super(NAME, ID, 10000, 0.0f, 0.0f, 160f, 300f, null);
        this.type = EnemyType.BOSS;
        this.dialogX = -160.0f * Settings.scale;
        this.dialogY = 40f * Settings.scale;
        this.img = InfiniteSpire.getTexture("img/infinitespire/monsters/guardian/guardian.png");
        this.setHp(10000);

        phase = 0;

        ArrayList<Integer> th = new ArrayList<Integer>();

        th.add(7500);
        th.add(5000);
        th.add(2500);

        THRESHOLDS = th;
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        for(int i = THRESHOLDS.size() -1; i >= 0; i--) {
            if(this.currentHealth < THRESHOLDS.get(i)){
                this.phase++;
            }
        }
    }

    @Override
    public void takeTurn() {
        switch(this.nextMove){

        }
        this.turn++;
    }

    @Override
    protected void getMove(int i) {
        switch(phase){
            case 0:
                phase1(i);
                break;
            case 1:
                phase2(i);
                break;
            case 2:
                phase3(i);
                break;
            case 3:
                phase4(i);
                break;
        }
    }

    private void phase1(int i){
        if(this.turn % 3 == 0){

        }
    }

    private void phase2(int i){

    }

    private void phase3(int i){

    }

    private void phase4(int i){

    }

    private class Move {
        private Move(Intent i, String name) {

        }

        public Move getMove(int phase){
            return null;
        }
    }
}
