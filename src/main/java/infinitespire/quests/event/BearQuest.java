package infinitespire.quests.event;

import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.MaskedBandits;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.*;
import infinitespire.helpers.CardHelper;
import infinitespire.interfaces.IAutoQuest;

import java.lang.reflect.*;
import java.util.ArrayList;

public class BearQuest extends Quest implements IAutoQuest {

    public static final String ID = BearQuest.class.getName();
    private static final Color COLOR = new Color(1.0f, 0.75f, 0f, 1f);
    private static boolean addedOption = false;

    public BearQuest() {
        super(ID, COLOR, 1, Quest.QuestType.BLUE, Quest.QuestRarity.SPECIAL);
    }

    @Override
    public boolean shouldBegin() {
        if(AbstractDungeon.player.hasRelic(GamblingChip.ID) &&
                AbstractDungeon.eventList.contains("Masked Bandits") &&
                !AbstractDungeon.player.hasRelic(RedMask.ID)) {
            return true;
        }

        return false;
    }

    @Override
    public void update() {
        if(AbstractDungeon.getCurrRoom() == null) return;
        if(!addedOption && AbstractDungeon.getCurrRoom().event instanceof MaskedBandits){
            ButtonEffect.hasChosen = false;
            if(AbstractDungeon.player.hasRelic(GamblingChip.ID)){
                AbstractDungeon.getCurrRoom().event.roomEventText.addDialogOption("[Give Bear his Chip] Gain a reward.");
            }else{
                AbstractDungeon.getCurrRoom().event.roomEventText.addDialogOption("[Disabled] Requires Gambling Chip.", true);
            }
            addedOption = true;
        }
    }

    @Override
    public void giveReward() {
        ArrayList<AbstractCard> randomBlackCards = CardHelper.getBlackRewardCards();
        AbstractDungeon.cardRewardScreen.open(randomBlackCards, null, "Select a Card.");
    }

    @Override
    public Quest createNew() {
        ButtonEffect.state = ButtonEffect.State.INTRO;
        this.addedOption = false;
        return this;
    }

    @Override
    public String getRewardString() {
        return "Pick a Black Card";
    }

    @Override
    public String getTitle() {
        return "Give Bear his Chip.";
    }

    @Override
    public Quest getCopy() {
        return new BearQuest();
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.events.city.MaskedBandits", method = "buttonEffect")
    public static class ButtonEffect {

        public static boolean hasChosen = false;
        public static State state = State.INTRO;
        public static enum State {
                INTRO,
                CLICK1,
                CLICK2;
        }

        public static SpireReturn<?> Prefix(MaskedBandits __instance, int buttonPressed){
            if(buttonPressed == 2 || state != State.INTRO){
                switch(state) {
                    case INTRO:
                    __instance.roomEventText.updateBodyText("My lucky chip! NL How can we ever repay you! NL Here take this mask as a symbol that you will always be one of us!");
                    __instance.roomEventText.updateDialogOption(0, "[Take Reward] Gain Red Mask.");
                    __instance.roomEventText.clearRemainingOptions();
                    state = State.CLICK1;
                    return SpireReturn.Return(null);
                    case CLICK1:
                    AbstractDungeon.player.loseRelic(GamblingChip.ID);
                    AbstractDungeon.combatRewardScreen.open();
                    AbstractDungeon.combatRewardScreen.rewards.clear();
                    AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(
                            AbstractDungeon.player.hasRelic(RedMask.ID) ? new Circlet() : new RedMask()));
                    AbstractDungeon.combatRewardScreen.positionRewards();
                    hasChosen = true;

                    for (Quest q : InfiniteSpire.questLog) {
                        if (q instanceof BearQuest) {
                            q.incrementQuestSteps();
                        }
                    }

                    state = State.CLICK2;

                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

                    return SpireReturn.Return(null);
                    case CLICK2:
                        break;
                }
                return SpireReturn.Return(null);
            }
            if(hasChosen){
                try {
                    Method m = SuperclassFinder.getSuperClassMethod(AbstractEvent.class, "openMap");
                    m.setAccessible(true);
                    m.invoke(__instance);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
                    e.printStackTrace();
                }


                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }
}
