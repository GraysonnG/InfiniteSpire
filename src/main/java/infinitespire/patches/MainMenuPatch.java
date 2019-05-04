package infinitespire.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.scenes.TitleBackground;
import com.megacrit.cardcrawl.scenes.TitleCloud;
import infinitespire.InfiniteSpire;

import java.io.IOException;
import java.util.ArrayList;

public class MainMenuPatch {

    @SpirePatch(cls = "com.megacrit.cardcrawl.scenes.TitleBackground", method = SpirePatch.CONSTRUCTOR)
    public static class ArtPatch{
        @SpirePostfixPatch
        public static void BackgroundTexturePatch(TitleBackground __instance) {
            try{
                SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
                config.load();
                if(!config.has("isGuardianDead")){
                    config.setBool("isGuardianDead", false);
                }
                if(config.has("voidShardCount")) {
                    InfiniteSpire.voidShardCount = config.getInt("voidShardCount");
                }
                InfiniteSpire.hasDefeatedGuardian = config.getBool("isGuardianDead");
                config.save();
            }catch (IOException e){
                return;
            }

            if(InfiniteSpire.hasDefeatedGuardian)
                setMainMenuBG(__instance);
        }
    }

    @SpirePatch(clz = TitleBackground.class, method = "render")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void renderVoidShardCount(TitleBackground instance, SpriteBatch sb) {

            sb.setColor(Color.WHITE.cpy());
            Texture tex = InfiniteSpire.Textures.getUITexture("topPanel/avhari/voidShard.png");
            TextureAtlas.AtlasRegion shardTexture = new TextureAtlas.AtlasRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
            sb.draw(shardTexture, Settings.WIDTH - (tex.getWidth() + 10f) - (tex.getWidth() / 2f), Settings.HEIGHT - (tex.getHeight() + 16f));
            FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont_N, "" + InfiniteSpire.voidShardCount, Settings.WIDTH - (tex.getWidth() + 10f), Settings.HEIGHT - (tex.getHeight() + 10f));
        }
    }

    public static void setMainMenuBG(TitleBackground __instance){
        TextureAtlas newAtlas = new TextureAtlas(Gdx.files.internal("img/infinitespire/ui/mainMenu/title.atlas"));

        if(__instance == null){
            __instance = CardCrawlGame.mainMenuScreen.bg;
        }

        ReflectionHacks.setPrivate(__instance, TitleBackground.class, "sky", newAtlas.findRegion("jpg/sky"));
        ReflectionHacks.setPrivate(__instance, TitleBackground.class, "mg3Bot", newAtlas.findRegion("mg3Bot"));
        ReflectionHacks.setPrivate(__instance, TitleBackground.class, "mg3Top", newAtlas.findRegion("mg3Top"));
        ReflectionHacks.setPrivate(__instance, TitleBackground.class, "topGlow", newAtlas.findRegion("mg3TopGlow1"));
        ReflectionHacks.setPrivate(__instance, TitleBackground.class, "topGlow2", newAtlas.findRegion("mg3TopGlow2"));
        ReflectionHacks.setPrivate(__instance, TitleBackground.class, "botGlow", newAtlas.findRegion("mg3BotGlow"));

        ArrayList<TitleCloud> newTopClouds = new ArrayList<>();
        ArrayList<TitleCloud> newMidClouds = new ArrayList<>();

        for(int i = 1; i < 7; ++i){
            newTopClouds.add(new TitleCloud(newAtlas.findRegion("topCloud" + i),
                    MathUtils.random(10.0f, 50.0f) * Settings.scale,
                    MathUtils.random(-1920.0f, 1920.0f) * Settings.scale));
        }

        for(int i = 1; i < 13; i++){
            newMidClouds.add(new TitleCloud(newAtlas.findRegion("midCloud" + i),
                    MathUtils.random(-50.0f, -10.0f) * Settings.scale,
                    MathUtils.random(-1920.0f, 1920.0f) * Settings.scale));
        }

        ReflectionHacks.setPrivate(__instance, TitleBackground.class,
                "topClouds", newTopClouds);
        ReflectionHacks.setPrivate(__instance, TitleBackground.class,
                "midClouds", newMidClouds);
    }
}
