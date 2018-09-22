package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.quests.FlawlessQuest;
import infinitespire.quests.endless.EndlessQuestPart2;

public class AbstractRoomPatch {

    public static final String CLS = "com.megacrit.cardcrawl.rooms.AbstractRoom";

    @SpirePatch(cls = CLS, method = "endBattle")
    public static class EndBattle {

        @SpirePrefixPatch
        public static void endOfBattleQuests(AbstractRoom __instance){
            if(__instance instanceof MonsterRoomBoss) {
                for (Quest quest : InfiniteSpire.questLog) {
                    if (quest instanceof FlawlessQuest && (GameActionManager.damageReceivedThisCombat - GameActionManager.hpLossThisCombat <= 0)) {
                        quest.incrementQuestSteps();
                    } else if (quest instanceof EndlessQuestPart2) {
                        quest.incrementQuestSteps();
                    }
                }
            }
        }
    }
}

