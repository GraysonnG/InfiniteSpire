package infinitespire.cards.black;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import infinitespire.AutoLoaderIgnore;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;
import infinitespire.actions.KillAllMonstersAction;

@AutoLoaderIgnore
public class SealOfDarkness extends BlackCard {

    public static final String ID = InfiniteSpire.createID("SealOfDarkness");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String TEXTURE = "img/infinitespire/cards/seal.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;

    public SealOfDarkness() {
        super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.SKILL, CardTarget.ALL_ENEMY);
        this.isInnate = true;
        this.purgeOnUse = true;
        FleetingField.fleeting.set(this, true);
        this.upgraded = true;
        this.upgrade();
    }

    @Override
    public void update() {
        this.retain = true;
        super.update();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public void useWithEffect(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new MindblastEffect(abstractPlayer.hb.cX, abstractPlayer.hb.cY, false)));
        AbstractDungeon.actionManager.addToBottom(new KillAllMonstersAction());
    }
}
