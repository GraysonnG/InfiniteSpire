package infinitespire.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.quests.PickUpCardQuest;
import infinitespire.relics.BottledMercury;
import infinitespire.relics.BottledSoul;
import infinitespire.relics.PuzzleCube;
import infinitespire.util.TextureLoader;

import java.io.IOException;

public class AbstractCardPatch {

    private static final String CLS = "com.megacrit.cardcrawl.cards.AbstractCard";
    private static AbstractRelic bottledSoul = new BottledSoul();
    private static AbstractRelic bottledMercury = new BottledMercury();
    private static AbstractRelic puzzleCube = new PuzzleCube();

    @SpirePatch(cls = CLS, method=SpirePatch.CLASS)
    public static class Field {
        public static SpireField<Boolean> isBottledSoulCard = new SpireField<>(() -> false);
        public static SpireField<Boolean> isBottledMercuryCard = new SpireField<>(() -> false);
        public static SpireField<Boolean> isPuzzleCubeCard = new SpireField<>(() -> false);
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

            if(Field.isBottledMercuryCard.get(card)){
                bottledMercury.currentX = card.current_x + 390.0f * card.drawScale / 3.0f * Settings.scale;
                bottledMercury.currentY = card.current_y + 546.0f * card.drawScale / 3.0f * Settings.scale;
                bottledMercury.scale = card.drawScale;
                bottledMercury.renderOutline(sb, false);
                bottledMercury.render(sb);
            }

            if(Field.isPuzzleCubeCard.get(card)){
                puzzleCube.currentX = card.current_x + 390.0f * card.drawScale / 3.0f * Settings.scale;
                puzzleCube.currentY = card.current_y + 546.0f * card.drawScale / 3.0f * Settings.scale;
                puzzleCube.scale = card.drawScale;
                puzzleCube.renderOutline(sb, false);
                puzzleCube.render(sb);
            }

            if(AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null) {
                for (Quest quest : InfiniteSpire.questLog) {
                    if (!quest.isCompleted() && quest instanceof PickUpCardQuest && ((PickUpCardQuest) quest).cardID != null && ((PickUpCardQuest) quest).cardID.equals(card.cardID)) {
                        if (shouldRenderQuestIcon()) {
                            sb.draw(TextureLoader.getTexture("img/infinitespire/ui/topPanel/questLogIcon.png"),
                                card.current_x + 320.0f * card.drawScale / 3.0f * Settings.scale,
                                card.current_y + 480.0f * card.drawScale / 3.0f * Settings.scale,
                                32f, 32f, 64f, 64f,
                                card.drawScale * Settings.scale, card.drawScale * Settings.scale,
                                0.0f, 0, 0,
                                64, 64,
                                false, false);
                        }
                    }
                }
            }
        }

        public static boolean shouldRenderQuestIcon() {
            boolean result = true;

            if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT ||
                AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW ||
                AbstractDungeon.screen == ScreenStatePatch.QUEST_LOG_SCREEN ||
                (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID && AbstractDungeon.gridSelectScreen.forPurge || AbstractDungeon.gridSelectScreen.forTransform || AbstractDungeon.gridSelectScreen.forUpgrade)) {
                return false;
            }

            return result;
        }
    }

    @SpirePatch(cls = CLS, method = "makeStatEquivalentCopy")
    public static class MakeStatCopy {
        @SpireInsertPatch(rloc = 6, localvars = {"card"})
        public static void Insert(AbstractCard __instance, @ByRef AbstractCard[] card) {
            if(AbstractDungeon.player != null && AbstractDungeon.player.relics != null && AbstractDungeon.player.hasRelic(BottledSoul.ID)){
               loadBottledSoul();
            }

            if(Field.isBottledSoulCard.get(__instance)) {
                Field.isBottledSoulCard.set(card[0], true);
                card[0].exhaust = false;
            }

            if(Field.isBottledMercuryCard.get(__instance)) {
                Field.isBottledMercuryCard.set(card[0], true);
            }
        }
    }

    private static void loadBottledSoul(){
        try {
            SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
            config.load();
            BottledSoul.load(config);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
