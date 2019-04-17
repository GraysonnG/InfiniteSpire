package infinitespire.monsters;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LordOfAnnihilation extends AbstractMonster{
    public static final String ID = "LordOfAnnihilation";

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);

    public static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final int BASE_MAX_HP = 10000;

    public LordOfAnnihilation() {
        super(NAME, ID, BASE_MAX_HP, 0.0f, 0.0f, 300f, 300f, null);
        this.type = EnemyType.BOSS;

    }

    @Override
    public void usePreBattleAction() {

    }

    private void endPlayerTurn(){

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

    }

    @Override
    protected void getMove(int i) {

    }

    public static class MoveBytes {
        public static byte ATTACK_1 = 0;
    }
}
