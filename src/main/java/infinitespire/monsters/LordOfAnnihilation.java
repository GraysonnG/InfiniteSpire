package infinitespire.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.blights.Shield;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.InfiniteSpire;
import infinitespire.powers.PrinceIdolPower;

public class LordOfAnnihilation extends AbstractMonster{
    public static final String ID = "LordOfAnnihilation";

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);

    public static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final int BASE_MAX_HP = 2000;

    private int max_hp;
    private int turn;

    private enum FightPhase {
        START,
        RUSH
    }

    private FightPhase phase;

    public LordOfAnnihilation() {
        super(NAME, ID, BASE_MAX_HP, 0.0f, 0.0f, 300f, 300f, null);
        this.type = EnemyType.BOSS;
        phase = FightPhase.START;
        turn = 0;

        //THIS IS TEMPORARY UNTIL RED IS DONE WITH THE REAL SPRITE
        this.dialogX = -160.0f * Settings.scale;
        this.dialogY = 40f * Settings.scale;
        this.img = InfiniteSpire.Textures.getMonsterTexture("guardian/guardian.png");

        if(CardCrawlGame.isInARun()) {
            for(int d : MoveValues.getDamageValues()) {
                damage.add(new DamageInfo(this, d));
            }

            doHealthScaling(BASE_MAX_HP);
        }


    }

    private void doHealthScaling(int baseMaxHp) {
        int scaling = 0;

        if(AbstractDungeon.player.hasBlight(Shield.ID)) {
            scaling = AbstractDungeon.player.getBlight(Shield.ID).counter - 2;
            if(scaling < 0) scaling = 0;
        }

        this.maxHealth = baseMaxHp * (int) Math.pow(2, scaling);
        this.currentHealth = baseMaxHp * (int) Math.pow(2, scaling);
        this.max_hp = baseMaxHp * (int) Math.pow(2, scaling);
    }

    @Override
    public void usePreBattleAction() {
        //- start music?

        InfiniteSpire.lordBackgroundEffect.beginEffect();

        AbstractPlayer player = AbstractDungeon.player;
        GameActionManager manager = AbstractDungeon.actionManager;

        manager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, this.maxHealth / 2), this.maxHealth / 2));

        manager.addToBottom(new ApplyPowerAction(this, this, new PrinceIdolPower(this)));

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
                    p.ID.equals(PrinceIdolPower.powerID) ||
                    p.ID.equals(GainStrengthPower.POWER_ID));

            setMove(MoveBytes.REVIVAL, Intent.UNKNOWN);
            createIntent();
            //AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0]));
            AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, MoveBytes.REVIVAL, Intent.UNKNOWN));
            applyPowers();
            this.turn = 0;
            this.phase = FightPhase.RUSH;
        }

    }

    private boolean shouldRevive(){
        return this.hasPower(PrinceIdolPower.powerID);
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
//        super.die();
//        this.onBossVictoryLogic();
//        this.onFinalBossVictoryLogic();
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
                for(int i = 0; i < 8; i ++) {
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
            this.setMove(monsterStrings.MOVES[1], MoveBytes.ATTACK_2, Intent.ATTACK, this.damage.get(1).base, 8, true);
        } else if (turn % 3 == 1) {
            //- Attack + Defend
            this.setMove(monsterStrings.MOVES[5], MoveBytes.ATTACK_DEFEND, Intent.ATTACK_DEFEND, damage.get(2).base);
        } else {
            //- Attack + Buff (gain 2 str) Gain revival
            this.setMove(monsterStrings.MOVES[4], MoveBytes.ATTACK_BUFF, Intent.ATTACK_BUFF, damage.get(3).base);
        }
    }

    private void doAction(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    private void gainStrength(AbstractCreature c, int amount) {
        doAction(new ApplyPowerAction(c, c, new StrengthPower(c, amount), amount));
    }

    private void gainPrinceIdol() {
        doAction(new ApplyPowerAction(this, this, new PrinceIdolPower(this)));
    }

    public static class MoveBytes {
        public static final byte ATTACK_1 = 0;
        public static final byte ATTACK_2 = 1;
        public static final byte ATTACK_DEFEND = 2;
        public static final byte ATTACK_BUFF = 3;
        public static final byte DEFEND = 4;
        public static final byte BUFF = 5;
        public static final byte REVIVAL = 6; // Lament
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
