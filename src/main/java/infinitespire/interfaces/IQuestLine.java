package infinitespire.interfaces;

import infinitespire.abstracts.Quest;
import infinitespire.quests.QuestLog;

public interface IQuestLine {
    Quest getNextQuestInLine();

    default void addNextStep(QuestLog log, int index){
        if(getNextQuestInLine() != null) {
            Quest nextStep = getNextQuestInLine().createNew();
            nextStep.isNew = false;
            log.add(index + 1, nextStep);
        }
    }
}
