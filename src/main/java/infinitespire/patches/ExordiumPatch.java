package infinitespire.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;

import infinitespire.InfiniteSpire;
import infinitespire.rooms.PerkRoom;

@SpirePatch(cls="com.megacrit.cardcrawl.dungeons.Exordium", method="ctor")
public class ExordiumPatch {
	public static void Postfix(Exordium __instance, AbstractPlayer player, ArrayList<String> emptyList) {
		if(InfiniteSpire.isRerun) {
			AbstractDungeon.currMapNode.room = new PerkRoom();
		}
	}
}
