package infinitespire.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;

import java.util.*;

public class Guardian extends AbstractMonster{
    public static final String ID = "Guardian";
    private static final String NAME = "The Lord of Annihilation";

    private final ArrayList<Integer> THRESHOLDS;
    private static final int[] ATTACKS = {15, 20, 25, 50};
    private static final int[] DEFENDS = {5, 10, 20, 50};
    private static final int NUKE = 100;

    private int phase;
    private int turn = 0;
    private boolean hasSpawnedPylons = false;

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
        if(this.currentHealth <= THRESHOLDS.get(phase)) {
            makeThresholdIntent(phase);
            phase++;
        }
    }

    public void changeIntentToNuke(){
        //change intent to nuke
    }

    private void makeThresholdIntent(int prevPhase){
        switch (phase){
            case 1:
                //give him intent that gives him power that switches his intent for big nukey boi
                break;
            case 2:
                //give him intent to spawn pylon bois
                break;
            case 3:
                //give him intent to buffwipe both of you and give himself super low
                break;
        }
    }

    @Override
    public void takeTurn() {
        switch(this.nextMove){
            //----------------------Attacks--------------------------
            case 1: //small attack with debuff
                break;
            case 2: //medium attack
                break;
            case 3: //large attack
                break;
            case 4: //nuke
                break;
            //----------------------Defends--------------------------
            case 5: //small defend with debuff
                break;
            case 6: //medium defend
                break;
            case 7: //large defend
                break;
            //-----------------------MISC----------------------------
            case 8: //give debuff nuke buff
                break;
            case 9: //spawn pylons and give pylon buff
                break;
            case 10: //removes buffs from you and him give himself super slow
                break;
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
            //big attack
        }else{
            if(AbstractDungeon.monsterRng.randomBoolean()){
                if(phase < 1){

                }else{ //attack

                }
            }else{
                if(phase < 1){

                }else{ //defend

                }
            }
        }
    }

    private void phase2(int i){
        phase1(i);
    }

    private void phase3(int i){
        if(!hasSpawnedPylons){
            //intend to spawn pylons and give pylon buff
        }
        //defends for like 20
    }

    private void phase4(int i){
        if(this.turn % 3 == 0){
            //big attack
        } else {
            if(AbstractDungeon.monsterRng.randomBoolean()) {
                //medium attack
            } else {
                //small attack with some debuff
            }
        }
    }
}
