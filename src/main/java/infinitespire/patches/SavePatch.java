package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import infinitespire.InfiniteSpire;
import infinitespire.relics.BottledSoul;

import java.io.IOException;

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
		public static void Prefix(AbstractPlayer.PlayerClass pClass) {
			InfiniteSpire.clearData();
		}
	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.core.CardCrawlGame", method = "loadPlayerSave")
	public static class PlayerLoad {
		public static void Postfix(CardCrawlGame game, AbstractPlayer player){
//			for(AbstractRelic relic : player.relics){
//				if(relic.relicId.equals(BottledSoul.ID)){
//					try {
//						SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
//						config.load();
//						//((BottledSoul) relic).load(config);
//					} catch(IOException e){
//						e.printStackTrace();
//					}
//				}
//			}
		}
	}
}
