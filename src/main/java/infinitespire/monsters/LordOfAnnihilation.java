package infinitespire.monsters;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.InfiniteSpire;
import infinitespire.powers.UndyingPower;

public class LordOfAnnihilation extends LordBoss {
    public static final String ID = "LordOfAnnihilation";

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);

    public static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final int BASE_MAX_HP = 2000;
    private static final float ANIM_TIME = 10f;
    private float animTicker;

    private enum FightPhase {
        START,
        RUSH
    }

    private FightPhase phase;

    public LordOfAnnihilation() {
        super(NAME, ID, BASE_MAX_HP, 0.0f, 0.0f, 300f, 300f, 10f, (me) -> {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(me, me, new StrengthPower(me, 5), 5));
        });
        phase = FightPhase.START;
        turn = 0;

        this.loadAnimation(
            InfiniteSpire.createPath("monsters/lordbosses/fortification/LordOfFortification.atlas"),
            InfiniteSpire.createPath("monsters/lordbosses/fortification/LordOfFortification.json"),
            2.0f);

        AnimationState.TrackEntry idle = this.state.setAnimation(0, "idle", true);
        AnimationState.TrackEntry shield = this.state.setAnimation(1, "shieldidle", true);

        //THIS IS TEMPORARY UNTIL RED IS DONE WITH THE REAL SPRITE
        this.dialogX = -160.0f * Settings.scale;
        this.dialogY = 40f * Settings.scale;


        if(CardCrawlGame.isInARun()) {
            for(int d : MoveValues.getDamageValues()) {
                damage.add(new DamageInfo(this, d));
            }

            doHealthScaling(BASE_MAX_HP);
        }


    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractPlayer player = AbstractDungeon.player;
        GameActionManager manager = AbstractDungeon.actionManager;

        manager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, this.maxHealth / 2), this.maxHealth / 2));

        manager.addToBottom(new ApplyPowerAction(this, this, new UndyingPower(this)));

        //- Gain debuff mitigation

        //- Give player debuff (actually a buff because fuck ur stupid buff removal tools) that minimizes block saving to 15 block
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (currentHealth <= 0 && shouldRevive()) {
            for (AbstractPower p : powers) {
                p.onDeath();
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMonsterDeath(this);
            }

            AbstractDungeon.actionManager.addToTop(new ClearCardQueueAction());

            powers.removeIf((p) ->
                p.type == AbstractPower.PowerType.DEBUFF ||
                    p.ID.equals(CuriosityPower.POWER_ID) ||
                    p.ID.equals(UndyingPower.powerID) ||
                    p.ID.equals(GainStrengthPower.POWER_ID));

            setMove(MoveBytes.REVIVAL, Intent.UNKNOWN);
            this.rageBar.increaseCurrentRage(5.0f);
            createIntent();
            //AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0]));
            AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, MoveBytes.REVIVAL, Intent.UNKNOWN));
            applyPowers();
            this.turn = 0;
            this.phase = FightPhase.RUSH;
        }

    }

    @Override
    public void update() {
        super.update();

        if(animTicker > ANIM_TIME) {
            this.state.setAnimation(1, "shieldlift", false);
            this.state.addAnimation(1, "shieldidle", true, 0.0f);
            animTicker = 0.0f;
        }
        animTicker += Gdx.graphics.getDeltaTime();
    }

    private boolean shouldRevive(){
        return this.hasPower(UndyingPower.powerID);
    }

    @Override
    public void die() {
        InfiniteSpire.hasDefeatedLordOfAnnihilation = true;
        InfiniteSpire.lordBackgroundEffect.stopEffect();


//        InfiniteSpire.hasDefeatedGuardian = true;
//        MainMenuPatch.setMainMenuBG(null);
//        InfiniteSpire.saveData();
//        InfiniteSpire.lordBackgroundEffect.stopEffect();
//        // AbstractDungeon.topLevelEffects.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.BLUE));
//        // AbstractDungeon.topLevelEffects.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.GREEN));
//        // AbstractDungeon.topLevelEffects.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.RED));
//        // Settings.isEndless = false;
//        AbstractDungeon.topPanel.setPlayerName();
        super.die();
        this.onBossVictoryLogic();
        this.onFinalBossVictoryLogic();
    }

    @Override
    public void takeTurn() {
        AbstractPlayer player = AbstractDungeon.player;
        GameActionManager manager = AbstractDungeon.actionManager;

        switch (this.nextMove) {
            case MoveBytes.ATTACK_1:
                doAction(new DamageAction(player, damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case MoveBytes.ATTACK_2:
                for(int i = 0; i < 4; i ++) {
                    doAction(new DamageAction(player, damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                break;
            case MoveBytes.ATTACK_DEFEND:
                doAction(new GainBlockAction(this, this, MoveValues.getBlockValues(this)[1]));
                doAction(new DamageAction(player, damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case MoveBytes.ATTACK_BUFF:
                doAction(new DamageAction(player, damage.get(3), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                gainStrength(this, MoveValues.STR_GAIN_2);
                gainPrinceIdol();
                break;
            case MoveBytes.DEFEND:
                doAction(new GainBlockAction(this, this, MoveValues.getBlockValues(this)[0]));
                break;
            case MoveBytes.BUFF:
                doAction(new ApplyPowerAction(this, this, new IntangiblePower(this, 1), 1));
                gainStrength(this, MoveValues.STR_GAIN_1);
                break;
            case MoveBytes.REVIVAL:
                maxHealth = max_hp;
                //set animation to idle
                halfDead = false;
                if(this.hasPower(StrengthPower.POWER_ID)) {
                    gainStrength(this, -this.getPower(StrengthPower.POWER_ID).amount / 2);
                }
                doAction(new HealAction(this, this, maxHealth));
                doAction(new CanLoseAction());
                break;

        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (phase){
            case START:
                phase1(i);
                break;
            case RUSH:
                phase2(i);
                break;
            default:
                break;
        }
        turn++;
    }

    private void phase1(int i){
        if(turn % 3 == 0) {
            //- Attack
            this.setMove(monsterStrings.MOVES[0], MoveBytes.ATTACK_1, Intent.ATTACK, this.damage.get(0).base);
        } else if (turn % 3 == 1) {
            //- Defend
            this.setMove(monsterStrings.MOVES[3], MoveBytes.DEFEND, Intent.DEFEND);
        } else {
            //- Buffs (gives himself 1 intangible and 10 str)
            this.setMove(MoveBytes.BUFF, Intent.BUFF);
        }
    }

    private void phase2(int i) {
        if(turn % 3 == 0) {
            //- Attack (8 hits)
            this.setMove(monsterStrings.MOVES[1], MoveBytes.ATTACK_2, Intent.ATTACK, this.damage.get(1).base, 4, true);
        } else if (turn % 3 == 1) {
            //- Attack + Defend
            this.setMove(monsterStrings.MOVES[5], MoveBytes.ATTACK_DEFEND, Intent.ATTACK_DEFEND, damage.get(2).base);
        } else {
            //- Attack + Buff (gain 2 str) Gain revival
            this.setMove(monsterStrings.MOVES[4], MoveBytes.ATTACK_BUFF, Intent.ATTACK_BUFF, damage.get(3).base);
        }
    }



    private void gainStrength(AbstractCreature c, int amount) {
        doAction(new ApplyPowerAction(c, c, new StrengthPower(c, amount), amount));
    }

    private void gainPrinceIdol() {
        doAction(new ApplyPowerAction(this, this, new UndyingPower(this)));
    }

    public static class MoveBytes {
        public static final byte ATTACK_1 = 0;
        public static final byte ATTACK_2 = 1;
        public static final byte ATTACK_DEFEND = 2;
        public static final byte ATTACK_BUFF = 3;
        public static final byte DEFEND = 4;
        public static final byte BUFF = 5;
        public static final byte REVIVAL = 6;
    }

    public static class MoveValues {
        public static int[] getDamageValues() {
            int[] damageValues = new int[4];

            damageValues[0] = 35; //- 70
            damageValues[1] = 8; //- 64
            damageValues[2] = 45; //- 90
            damageValues[3] = 45; //- 90

            return damageValues;
        }

        public static int[] getBlockValues(LordOfAnnihilation boss) {
            int[] blockValues = new int[2];

            blockValues[0] = boss.maxHealth / 4;
            blockValues[1] = boss.maxHealth / 8;

            return blockValues;
        }

        public static final int STR_GAIN_1 = 10;
        public static final int STR_GAIN_2 = 2;
    }
}
