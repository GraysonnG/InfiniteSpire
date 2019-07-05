package infinitespire.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;

public class QuestCommand extends ConsoleCommand {

	public static final String COMMAND_VALUE = "quest";

	public QuestCommand() {
		requiresPlayer = true;
		minExtraTokens = 1;
		simpleCheck = true;
		followup.put("add", QuestAdd.class);
	}

	@Override
	protected void execute(String[] strings, int i) {
		cmdQuestHelp();
	}

	public static void cmdQuestHelp() {
		DevConsole.couldNotParse();
		DevConsole.log("options are:");
		DevConsole.log("add [name]");
	}
}
