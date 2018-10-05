package infinitespire.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import infinitespire.InfiniteSpire;
import infinitespire.powers.PylonExplosionPower;

public class ShieldPylon extends AbstractMonster{

    private static final String NAME = "Shield Pylon";
    private static final String ID = "ShieldPylon";

    private AbstractMonster boss;

    private static final int offsetX = 1920 / 6;
    private static final int offsetY = 1080 / 8;

    public ShieldPylon(LordOfAnnihilation boss, float xOffset, float yOffset){
        super(NAME, ID, boss.maxHealth / 16, 0.0f, 0.0f, 100f, 100f, null, xOffset * offsetX, yOffset * offsetY);
        this.type = EnemyType.NORMAL;
        this.dialogX = xOffset;
        this.dialogY = 0;
        this.img = InfiniteSpire.getTexture("img/infinitespire/monsters/guardian/pylon.png");
        this.setHp(boss.maxHealth / 16);
        this.maxHealth = boss.maxHealth / 16;
        this.currentHealth = maxHealth;
        this.boss = boss;
        this.damage.add(new DamageInfo(this, 10));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PylonExplosionPower(this, (LordOfAnnihilation) boss), 1));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer player = AbstractDungeon.player;
        GameActionManager manager = AbstractDungeon.actionManager;
        manager.addToBottom(new DamageAction(player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
        applyRandomDebuff();

    }

    @Override
    protected void getMove(int i) {
        this.setMove((byte) 0, Intent.ATTACK_DEBUFF, damage.get(0).base);
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
                        new VulnerablePower(player, 1, true), 1));
                break;
        }
    }
}
