package infinitespire.monsters;

import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class LordOfAnnihilation extends AbstractMonster{
    public static final String ID = "LordOfAnnihilation";

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);

    public static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final int BASE_MAX_HP = 2000;

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
    }

    @Override
    public void usePreBattleAction() {
        //- start music?

        AbstractPlayer player = AbstractDungeon.player;
        GameActionManager manager = AbstractDungeon.actionManager;

        manager.addToBottom(new ApplyPowerAction(this, this, new IntangiblePower(this, 1), 1));
        manager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, this.maxHealth / 2), this.maxHealth / 2));

        //- Gain revival

        //- Gain poison mitigation (fuck ur poison decks)

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
                    /*p.ID.equals( revival power ) ||*/
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
        //if this has power

        return false;
    }

    @Override
    public void die() {
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
                break;
            case MoveBytes.ATTACK_2:
                break;
            case MoveBytes.ATTACK_DEFEND:
                break;
            case MoveBytes.ATTACK_BUFF:
                break;
            case MoveBytes.DEFEND:
                break;
            case MoveBytes.BUFF:
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
    }

    private void phase1(int i){
        if(turn % 3 == 0) {
            //- Attack (50% your maxHp rounded up or minimum 32 damage)
        } else if (turn % 3 == 1) {
            //- Defend (25% of maxHP)
        } else {
            //- Buffs (gives himself 1 intangible and 10 str) <--- blow him up here
        }
    }

    private void phase2(int i) {
        if(turn % 3 == 0) {
            //- Attack (50% your maxHp) (8 hits)
        } else if (turn % 3 == 1) {
            //- Attack + Defend (25% your maxHP, 25% of maxHP)
        } else {
            //- Attack + Buff (25% your maxHP, gain 2 str) Gain revival
        }
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
}
