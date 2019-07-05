package infinitespire.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.KeyNotFoundException;
import infinitespire.actions.AddQuestAction;
import infinitespire.helpers.QuestHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestAdd extends ConsoleCommand {
	public QuestAdd() {
		requiresPlayer = true;
		minExtraTokens = 1;
		maxExtraTokens = 2;
		simpleCheck = true;
	}

	@Override
	protected void execute(String[] tokens, int depth) {
		String[] questNameArray = Arrays.copyOfRange(tokens, 2, tokens.length);
		try {
			String questName = QuestHelper.getQuestIdByName(questNameArray);
			AbstractDungeon.actionManager.addToBottom(
				new AddQuestAction(
					QuestHelper.getQuestByID(questName).createNew(tokens[2]),
					true));
		} catch(IllegalAccessException | InstantiationException | KeyNotFoundException e) {
			e.printStackTrace();
			DevConsole.couldNotParse();
		}
	}

	@Override
	protected ArrayList<String> extraOptions(String[] tokens, int depth) {
		ArrayList<String> options = new ArrayList<>();
		options.addAll(QuestHelper.questIDMap.keySet());
		return options;
	}
}
