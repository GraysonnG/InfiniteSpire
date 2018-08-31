package infinitespire.interfaces;

import infinitespire.abstracts.Quest;

public interface OnQuestRemovedSubscriber {
    void receiveQuestRemoved(Quest quest);
}
