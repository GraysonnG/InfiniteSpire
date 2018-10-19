package infinitespire.patches;

@Deprecated
public class DrugDealerPatch {

//    @SpirePatch(cls = "com.megacrit.cardcrawl.events.city.DrugDealer", method = "buttonEffect")
//    public static class ButtonEffect {
//
//        @SpirePrefixPatch
//        public static SpireReturn<Void> case2LogicOverride(DrugDealer __instance, int buttonPressed){
//            int screenNum = (int) ReflectionHacks.getPrivate(__instance, DrugDealer.class, "screenNum");
//            float drawX = (float) ReflectionHacks.getPrivate(__instance, AbstractEvent.class, "drawX");
//            float drawY = (float) ReflectionHacks.getPrivate(__instance, AbstractEvent.class, "drawY");
//
//            if(screenNum == 0 && buttonPressed == 2){
//                __instance.imageEventText.updateBodyText(DrugDealer.DESCRIPTIONS[3]);
//
//                boolean hasStr = AbstractDungeon.player.hasRelic(MutagenicStrength.ID);
//                boolean hasDex = AbstractDungeon.player.hasRelic(MutagenicDexterity.ID);
//
//                if(!(hasStr && hasDex)) {
//                    boolean roll = AbstractDungeon.miscRng.randomBoolean();
//
//                    if(hasStr){
//                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, new MutagenicDexterity());
//                    }else if(hasDex){
//                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, new MutagenicStrength());
//                    }else{
//                        if(roll){
//                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, new MutagenicDexterity());
//                        }else{
//                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, new MutagenicStrength());
//                        }
//                    }
//                }else{
//                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, new Circlet());
//                }
//
//
//
//                __instance.imageEventText.updateDialogOption(0, DrugDealer.OPTIONS[3]);
//                __instance.imageEventText.clearRemainingOptions();
//
//                ReflectionHacks.setPrivate(__instance, DrugDealer.class, "screenNum", 1);
//
//                return SpireReturn.Return(null);
//            }
//
//            return SpireReturn.Continue();
//        }
//    }
}
