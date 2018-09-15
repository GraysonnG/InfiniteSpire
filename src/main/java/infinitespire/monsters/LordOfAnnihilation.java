package infinitespire.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.blights.Shield;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import infinitespire.InfiniteSpire;
import infinitespire.actions.FastHealAction;
import infinitespire.patches.MainMenuPatch;
import infinitespire.powers.LordOfAnnihilationIntangiblePower;
import infinitespire.powers.LordOfAnnihilationPylonPower;
import infinitespire.powers.LordOfAnnihilationRetaliatePower;
import infinitespire.powers.SuperSlowPower;

import java.util.ArrayList;

public class LordOfAnnihilation extends AbstractMonster{
    public static final String ID = "LordOfAnnihilation";
    private static final String NAME = "The Lord of Annihilation";

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private final ArrayList<Integer> THRESHOLDS;
    private static final int MAX_HP = 10000;

    //the first index of these are not used
    private static final int[] ATTACKS = {-1, 15, 25, 35, 50};
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
        super(NAME, ID, MAX_HP, 0.0f, 0.0f, 300f, 300f, null);
        this.type = EnemyType.BOSS;
        this.dialogX = -160.0f * Settings.scale;
        this.dialogY = 40f * Settings.scale;
        this.img = InfiniteSpire.getTexture("img/infinitespire/monsters/guardian/guardian.png");

        int spearStacks = 0;
        if(AbstractDungeon.player != null) {
            spearStacks = AbstractDungeon.player.getBlight(Shield.ID).counter - 2;
        }

        if(spearStacks < 0) spearStacks = 0;

        this.maxHealth = MAX_HP * (spearStacks + 1);
        this.currentHealth = maxHealth;

        phase = 0;
        this.intentPhase = IntentPhase.NORMAL;

        ArrayList<Integer> th = new ArrayList<>();

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
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
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

            endPlayerTurn();
        }

        if(phase < 3 && this.currentHealth <= 0){
            this.currentHealth = 1;
            this.phase = 3;
            this.intentPhase = IntentPhase.THRESHOLD;
            makeThresholdIntent(phase);
            this.createIntent();

            endPlayerTurn();
        }
    }

    private void endPlayerTurn(){
        AbstractDungeon.actionManager.cardQueue.clear();
        for (final AbstractCard c : AbstractDungeon.player.limbo.group) {
            AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
        }
        AbstractDungeon.player.limbo.group.clear();
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.overlayMenu.endTurnButton.disable(true);
    }


    @Override
    public void die() {
        super.die();
        InfiniteSpire.hasDefeatedGuardian = true;
        MainMenuPatch.setMainMenuBG(null);
        InfiniteSpire.saveData();
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
                break;
            case 4: //nuke
                manager.addToBottom(new LoseBlockAction(player, this, player.currentBlock));
                manager.addToBottom(new LoseHPAction(player, this, player.maxHealth / 3));
                manager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 2),2));
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
            case 12:
                manager.addToBottom(new LoseBlockAction(player, this, player.currentBlock));
                break;
            case 8: //give retaliate power
                manager.addToBottom(new ApplyPowerAction(this, this, new LordOfAnnihilationRetaliatePower(this)));
                manager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 3),3));
                break;
            case 9: //buff wipe himself and spawn pylons
                manager.addToBottom(new RemoveAllPowersAction(this, false));

                ArrayList<ShieldPylon> pylons = new ArrayList<>();

                pylons.add(new ShieldPylon(this, -0.5f , 1));
                pylons.add(new ShieldPylon(this, 0.5f, 1));
                pylons.add(new ShieldPylon(this, -1 , 0.5f));
                pylons.add(new ShieldPylon(this, 1, 0.5f));

                for(ShieldPylon pylon : pylons){
                    manager.addToBottom(new SpawnMonsterAction(pylon, true));
                    pylon.usePreBattleAction();
                }

                manager.addToBottom(new ApplyPowerAction(this, this, new LordOfAnnihilationPylonPower(this)));

                break;
            case 10: //total buff wipe, full heal, gain super slow, triggers at battle start relics
                manager.addToBottom(new RemoveAllPowersAction(player, false));
                manager.addToBottom(new RemoveAllPowersAction(this, false));
                manager.addToBottom(new ApplyPowerAction(this, this, new SuperSlowPower(this, 0),0));
                manager.addToBottom(new WaitAction(0.5f));

                for(int i = 0; i < 10; i++) {
                    manager.addToBottom(new FastHealAction(this,this, 999));
                }

                for(AbstractRelic relic : player.relics){
                    relic.atBattleStartPreDraw();
                    relic.atBattleStart();
                }

                for(AbstractCard card : player.masterDeck.group){
                    if(card.type == AbstractCard.CardType.POWER){
                        player.drawPile.addToRandomSpot(card);
                    }
                }
                break;
        }
        this.intentPhase = IntentPhase.NORMAL;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {

        if(this.turn == 0)
            this.setMove((byte) 6, Intent.DEFEND);

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
        if(this.turn % 3 == 0 && this.turn > 0){
            this.setMove(MOVES[0], (byte) 3, Intent.ATTACK, this.damage.get(3).base);
        }else{
            if(i >= 50){
                this.setMove((byte) 2, Intent.ATTACK, this.damage.get(2).base);
            }else{
                this.setMove(MOVES[5],(byte) 6, Intent.DEFEND);
            }
        }
    }

    private void phase2(int i){
        if(this.turn % 3 == 0 && this.turn > 0){
            this.setMove(MOVES[1], (byte) 3, Intent.ATTACK, this.damage.get(4).base);
        }else{
            if(i >= 50){
                this.setMove((byte) 3, Intent.ATTACK, this.damage.get(3).base);
            }else{
                this.setMove(MOVES[5],(byte) 7, Intent.DEFEND);
            }
        }
    }

    private void phase3(int i){
        this.setMove((byte) 7, Intent.DEFEND);
    }

    private void phase4(int i){
        if(this.turn % 2 == 0){
            this.setMove((byte) 12, Intent.MAGIC);
        } else {
            if(i >= 50) {
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
}
