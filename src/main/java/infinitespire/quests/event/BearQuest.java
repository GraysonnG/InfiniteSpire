package infinitespire.quests.event;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
    private static boolean isAbandoned = false;

    public BearQuest() {
        super(ID, COLOR, 1, Quest.QuestType.BLUE, Quest.QuestRarity.SPECIAL);
    }

    @Override
    public Texture getTexture() {
        return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/chip.png");
    }

    @Override
    public boolean shouldBegin() {
        if(!isAbandoned && AbstractDungeon.player.hasRelic(GamblingChip.ID) &&
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
            String choice1;
            String choice2;
            if (Settings.language == Settings.GameLanguage.FRA){
               choice1 = "[Donnez à l'Ours son jeton] Gagnez une récompense";
               choice2 = "[Verrouil\u00e9] Nécessite un jeton de jeu";
            } else {
              choice1 = "[Give Bear his Chip] Gain a reward.";
              choice2 = "[Disabled] Requires Gambling Chip.";
            }
            if(AbstractDungeon.player.hasRelic(GamblingChip.ID)){
                AbstractDungeon.getCurrRoom().event.roomEventText.addDialogOption(choice1);
            }else{
                AbstractDungeon.getCurrRoom().event.roomEventText.addDialogOption(choice2, true);
            }
            addedOption = true;
        }
    }

    @Override
    public void giveReward() {
      String choice;
      if (Settings.language == Settings.GameLanguage.FRA){
         choice = "Choisissez une carte.";

      } else {
        choice = "Select a Card.";
      }
        ArrayList<AbstractCard> randomBlackCards = CardHelper.getBlackRewardCards();
        AbstractDungeon.cardRewardScreen.open(randomBlackCards, null, choice);
    }

    @Override
    public Quest createNew() {
        ButtonEffect.state = ButtonEffect.State.INTRO;
        this.addedOption = false;
        return this;
    }

    @Override
    public String getRewardString() {
      if (Settings.language == Settings.GameLanguage.FRA){
          return "Choisissez une Carte Noire";

      } else {
          return "Pick a Black Card";
      }

    }

    @Override
    public String getTitle() {
      if (Settings.language == Settings.GameLanguage.FRA){
          return "Rendez à l'ours son Jeton.";

      } else {
          return "Give Bear his Chip.";
      }

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
              String roomEventBody;
              String roomEventDialog;
              if (Settings.language == Settings.GameLanguage.FRA){
                roomEventBody = "Mon jeton! NL Comment puis-je vous remerci\u00e9! NL Prenez ce masque en symbole prouvant que vous êtes l'un des notres !";
                roomEventDialog = "[Prendre la récompense] Gagnez Masque Rouge.";
              } else {
                roomEventBody = "My lucky chip! NL How can we ever repay you! NL Here take this mask as a symbol that you will always be one of us!";
                roomEventDialog = "[Take reward] Gain Red Mask.";
              }
                switch(state) {
                    case INTRO:
                    __instance.roomEventText.updateBodyText(roomEventBody);
                    __instance.roomEventText.updateDialogOption(0, roomEventDialog);
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
