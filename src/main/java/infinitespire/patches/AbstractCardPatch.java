package infinitespire.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.relics.BottledSoul;

public class AbstractCardPatch {

    private static final String CLS = "com.megacrit.cardcrawl.cards.AbstractCard";
    private static AbstractRelic bottledSoul = new BottledSoul();

    @SpirePatch(cls = CLS, method=SpirePatch.CLASS)
    public static class Field {
        public static SpireField<Boolean> isBottledSoulCard = new SpireField<>(() -> false);
    }

    @SpirePatch(cls = CLS, method = "renderCard")
    public static class RenderPatch {
        public static void Postfix(AbstractCard card, SpriteBatch sb, boolean b1, boolean b2){
            if(Field.isBottledSoulCard.get(card)){
                bottledSoul.currentX = card.current_x + 390.0f * card.drawScale / 3.0f * Settings.scale;
                bottledSoul.currentY = card.current_y + 546.0f * card.drawScale / 3.0f * Settings.scale;
                bottledSoul.scale = card.drawScale;
                bottledSoul.renderOutline(sb, false);
                bottledSoul.render(sb);
            }
        }
    }

    @SpirePatch(cls = CLS, method = "makeStatEquivalentCopy")
    public static class MakeStatCopy {
        @SpireInsertPatch(rloc = 6, localvars = {"card"})
        public static void Insert(AbstractCard __instance, @ByRef AbstractCard[] card) {
            if(Field.isBottledSoulCard.get(__instance)) {
                Field.isBottledSoulCard.set(card[0], true);
                card[0].exhaust = false;
            }
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.CardGroup", method = "getGroupWithoutBottledCards")
    public static class GetGroupWithoutBottledCards{
        @SpirePostfixPatch
        public static CardGroup removeBottledSoulCardFromGroup(CardGroup __result, CardGroup cards){
            __result.group.removeIf(Field.isBottledSoulCard::get);
            return __result;
        }
    }
}
