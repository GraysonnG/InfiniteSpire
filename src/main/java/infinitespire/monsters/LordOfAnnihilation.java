package infinitespire.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import infinitespire.InfiniteSpire;
import infinitespire.powers.GuardianRetaliatePower;
import infinitespire.powers.LordOfAnnihilationIntangiblePower;

import java.util.ArrayList;

public class LordOfAnnihilation extends AbstractMonster{
    public static final String ID = "LordOfAnnihilation";
    private static final String NAME = "The Lord of Annihilation";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private final ArrayList<Integer> THRESHOLDS;

    //the first index of these are not used
    private static final int[] ATTACKS = {-1, 20, 35, 50, 60};
    private static final int[] DEFENDS = {-1, 20, 35, 50};

    private static final int NUKE = 100;

    private int phase;
    private IntentPhase intentPhase;
    private int turn = 0;

    private enum IntentPhase {
        NORMAL,
        THRESHOLD
    }

    public LordOfAnnihilation() {
        super(NAME, ID, 10000, 0.0f, 0.0f, 300f, 300f, null);
        this.type = EnemyType.BOSS;
        this.dialogX = -160.0f * Settings.scale;
        this.dialogY = 40f * Settings.scale;
        this.img = InfiniteSpire.getTexture("img/infinitespire/monsters/guardian/guardian.png");
        this.setHp(10000);

        phase = 0;
        this.intentPhase = IntentPhase.NORMAL;

        ArrayList<Integer> th = new ArrayList<Integer>();

        th.add((int)(maxHealth * 0.75));
        th.add((int)(maxHealth * 0.50));
        th.add((int)(maxHealth * 0.25));

        THRESHOLDS = th;

        for(int d : ATTACKS){
            this.damage.add(new DamageInfo(this, d));
        }
        this.damage.add(new DamageInfo(this, NUKE));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
                new LordOfAnnihilationIntangiblePower(this, 1),1));
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if(phase < 3 && this.currentHealth <= THRESHOLDS.get(phase)) {
            InfiniteSpire.logger.info(currentHealth  + " : " + THRESHOLDS.get(phase));
            phase++;
            this.intentPhase = IntentPhase.THRESHOLD;
            makeThresholdIntent(phase);
            this.createIntent();
            //maybe end your turn. none of the threshold intents are attacks
        }
    }

    @Override
    public void applyPowers(){

    }

    @Override
    public void die() {
        super.die();
        if(this.isDead){
            InfiniteSpire.hasDefeatedGuardian = true;
        }
    }

    public void changeIntentToNuke(){
        this.setMove(MOVES[3],(byte) 4, Intent.ATTACK_BUFF, this.damage.get(4).base);
        this.createIntent();
    }

    private void makeThresholdIntent(int curPhase){
        switch (curPhase){
            case 1:
                this.setMove((byte) 8, Intent.BUFF);
                break;
            case 2:
                this.setMove((byte) 9, Intent.UNKNOWN);
                break;
            case 3:
                this.setMove((byte) 10, Intent.MAGIC);
                break;
        }
    }

    @Override
    public void takeTurn() {
        AbstractPlayer player = AbstractDungeon.player;
        GameActionManager manager = AbstractDungeon.actionManager;

        switch(this.nextMove){
            //----------------------Attacks--------------------------
            case 1: //small attack with debuff
                manager.addToBottom(new DamageAction(player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                applyRandomDebuff();
                break;
            case 2: //medium attack
                manager.addToBottom(new DamageAction(player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 3: //large attack
                manager.addToBottom(new DamageAction(player, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 11: //larger attack
                manager.addToBottom(new DamageAction(player, this.damage.get(4), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            case 4: //nuke
                manager.addToBottom(new LoseBlockAction(player, this, player.currentBlock));
                manager.addToBottom(new LoseHPAction(player, this, player.maxHealth / 3));
                manager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 5),5));
                break;
            //----------------------Defends--------------------------
            case 5: //small defend with debuff
                manager.addToBottom(new GainBlockAction(this, this, DEFENDS[1]));
                applyRandomDebuff();
                break;
            case 6: //medium defend
                manager.addToBottom(new GainBlockAction(this, this, DEFENDS[2]));
                break;
            case 7: //large defend
                manager.addToBottom(new GainBlockAction(this, this, DEFENDS[3]));
                break;
            //-----------------------MISC----------------------------
            case 8:
                manager.addToBottom(new ApplyPowerAction(this, this, new GuardianRetaliatePower(this)));
                manager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 6),5));
                break;
            case 9:
                manager.addToBottom(new RemoveAllPowersAction(this, false));

                manager.addToBottom(new SpawnMonsterAction(
                        new ShieldPylon(this, -1), true));
                manager.addToBottom(new SpawnMonsterAction(
                        new ShieldPylon(this, 1), true));

                manager.addToBottom(new ApplyPowerAction(this, this, new LordOfAnnihilationIntangiblePower(this, 10), 10));

                break;
            case 10: //resets the players buffs and deck
                for(AbstractPower p : this.powers){
                    manager.addToBottom(new RemoveSpecificPowerAction(this, this, p));
                }
                for(AbstractPower p : player.powers){
                    manager.addToBottom(new RemoveSpecificPowerAction(this, this, p));
                }
                //do code to clear all card piles except master deck and then reconstruct the deck into the drawpile


                break;
        }
        this.intentPhase = IntentPhase.NORMAL;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch(intentPhase) {
            case NORMAL:
                switch (phase) {
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
                break;
            case THRESHOLD:
                makeThresholdIntent(this.phase);
                break;
        }
        this.turn++;
    }

    private void phase1(int i){
        if(this.turn % 3 == 0){
            this.setMove(MOVES[0], (byte) 3, Intent.ATTACK, this.damage.get(3).base);
        }else{
            if(AbstractDungeon.monsterRng.randomBoolean()){
                this.setMove((byte) 2, Intent.ATTACK, this.damage.get(2).base);
            }else{
                this.setMove(MOVES[5],(byte) 6, Intent.DEFEND);
            }
        }
    }

    private void phase2(int i){
        phase1(i);
    }

    private void phase3(int i){
        this.setMove((byte) 7, Intent.DEFEND);
    }

    private void phase4(int i){
        if(this.turn % 3 == 0){
            this.setMove(MOVES[2], (byte) 11, Intent.ATTACK, this.damage.get(4).base);
        } else {
            if(AbstractDungeon.monsterRng.randomBoolean()) {
                this.setMove((byte) 2, Intent.ATTACK, this.damage.get(2).base);
            } else {
                this.setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
            }
        }
    }

    private void applyRandomDebuff(){
        AbstractPlayer player = AbstractDungeon.player;
        GameActionManager manager = AbstractDungeon.actionManager;
        switch(AbstractDungeon.monsterRng.random(2)){
            case 0:
                manager.addToBottom(new ApplyPowerAction(player, this,
                        new WeakPower(player, 2, true),2));
                break;
            case 1:
                manager.addToBottom(new ApplyPowerAction(player, this,
                        new FrailPower(player, 2, true), 2));
                break;
            case 2:
                manager.addToBottom(new ApplyPowerAction(player, this,
                        new VulnerablePower(player, 2, true), 2));
                break;
        }
    }

    private void spawnRandomElite(){

    }
}
