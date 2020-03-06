package infinitespire.crossover;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.blanktheevil.rarecardssparkle.RareCardsSparkle;
import com.blanktheevil.rarecardssparkle.SparkleTimer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class RareCardsSparkleHandler {
    public static void init() {
        RareCardsSparkle.addSparkleRule(
                InfiniteSpire.createID("BlackCardsPurpleSparkle"),
                "Black",
                Color.RED.cpy(),
                ImageMaster.GLOW_SPARK,
                false,
                new SparkleTimer(0.1f,0.15f),
                card -> card instanceof BlackCard
        );
    }
}
