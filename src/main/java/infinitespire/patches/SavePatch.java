package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import infinitespire.InfiniteSpire;

public class SavePatch {
	@SpirePatch(cls = "com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue", method = "loadSaveString",
			paramtypes= {"java.lang.String"})
	public static class LoadGame1 {

		public static void Prefix(String filePath) {
			InfiniteSpire.loadData();
		}
	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue", method = "loadSaveFile",
			paramtypes= {"java.lang.String"})
	public static class LoadGame2 {
		public static void Prefix(String filePath) {
			InfiniteSpire.loadData();
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue", method = "save")
	public static class SaveGame {
		
		public static void Prefix(SaveFile save) {
			InfiniteSpire.saveData();
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue", method = "deleteSave")
	public static class DeleteSave {
		public static void Prefix(AbstractPlayer p) {
			InfiniteSpire.clearData();
		}
	}
}
