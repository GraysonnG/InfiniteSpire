package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import infinitespire.InfiniteSpire;

@SpirePatch(cls = "com.megacrit.cardcrawl.core.CardCrawlGame", method = "create")
public class InitializePatch
{
    public static void Postfix(final CardCrawlGame ccg) {
        InfiniteSpire.initialize();
    }
}