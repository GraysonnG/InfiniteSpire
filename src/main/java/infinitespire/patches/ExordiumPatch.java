package infinitespire.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.InfiniteSpire;
import infinitespire.rooms.PerkRoom;

@Deprecated

public class ExordiumPatch {
	@SpirePatch(cls="com.megacrit.cardcrawl.dungeons.Exordium", method="ctor")
	public static class Param1 {
		public static void Postfix(Exordium __instance, AbstractPlayer player, ArrayList<String> emptyList) {
	//		Settings.isEndless = true;
	//		if(InfiniteSpire.isRerun) {
	//			AbstractDungeon.currMapNode.room = new PerkRoom();
	//			
	//			StringBuilder builder = new StringBuilder();
	//			
	//			for(AbstractRelic relic : AbstractDungeon.player.relics) {
	//				builder.append("Removing " + relic.relicId + " from " + relic.tier);
	//				
	//				switch(relic.tier) {
	//				case BOSS:
	//					AbstractDungeon.bossRelicPool.remove(relic.relicId);
	//					
	//					break;
	//				case COMMON:
	//					AbstractDungeon.commonRelicPool.remove(relic.relicId);
	//					break;
	//				case DEPRECATED:
	//					break;
	//				case RARE:
	//					AbstractDungeon.rareRelicPool.remove(relic.relicId);
	//					break;
	//				case SHOP:
	//					AbstractDungeon.shopRelicPool.remove(relic.relicId);
	//					break;
	//				case SPECIAL:
	//					break;
	//				case STARTER:
	//					break;
	//				case UNCOMMON:
	//					AbstractDungeon.uncommonRelicPool.remove(relic.relicId);
	//					break;
	//				default:
	//					break;
	//				
	//				}
	//				InfiniteSpire.logger.info(builder.toString());
	//				builder.setLength(0);
	//			}
	//		}
		}
		//@SpirePatch(cls="com.megacrit.cardcrawl.dungeons.Exordium", method="ctor",)
		public static class Param2{
			
		}
	}
}
