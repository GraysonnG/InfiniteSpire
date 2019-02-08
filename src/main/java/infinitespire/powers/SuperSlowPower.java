package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class SuperSlowPower extends AbstractPower {

    public static final String powerID = InfiniteSpire.createID("ShreddedPower");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

    public SuperSlowPower(AbstractMonster owner, int amount){
        this.owner = owner;
        this.amount = amount;
        this.name = strings.NAME;
        this.ID =  powerID;
        this.img = InfiniteSpire.Textures.getPowerTexture("superslow.png");
        this.type = PowerType.DEBUFF;
        this.updateDescription();
    }

    @Override
    public void onAfterUseCard(final AbstractCard card, final UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new SuperSlowPower((AbstractMonster) this.owner, 1), 1));
    }

    @Override
    public float atDamageReceive(final float damage, final DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * (1.0f + this.amount * 0.1f);
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        this.description =
            strings.DESCRIPTIONS[0]
                + owner.name
                + strings.DESCRIPTIONS[1]
                + amount * 10
                + strings.DESCRIPTIONS[2];
    }
}
