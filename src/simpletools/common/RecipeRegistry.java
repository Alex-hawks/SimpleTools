package simpletools.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import simpletools.api.SimpleToolsItems;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeRegistry
{
    public static void registerRecipes()
    {
        Item attachmentToolMotor = SimpleToolsItems.itemAttachmentToolMotor;
        Item coreMechElectric = SimpleToolsItems.itemCoreMechElectric;
        
        if (OreDictionary.getOres("plateSteel").size() > 0)
            GameRegistry.addRecipe(new ShapedOreRecipe(SimpleToolsItems.blockTableAssembly, new Object[] { "!@!", "#$#", "%%%", '!',
                "plateCopper", '@', "circuitAdvanced", '#', "plateSteel", '$', "craftingTableWood", '%', "plateTin" }));
        else
            GameRegistry.addRecipe(new ShapedOreRecipe(SimpleToolsItems.blockTableAssembly, new Object[] { "!@!", "#$#", "%%%", '!',
                "plateCopper", '@', "circuitAdvanced", '#', "plateIron", '$', "craftingTableWood", '%', "plateTin" }));

        // Drill
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 00), new Object[] { " ! ", "!@!", "!@!",
                '!', Block.stone, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 10), new Object[] { " ! ", "!@!", "!@!",
                '!', Item.ingotIron, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 20), new Object[] { " ! ", "!@!", "!@!",
                '!', Item.diamond, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 30), new Object[] { " ! ", "!@!", "!#!",
                '!', Item.emerald, '@', new ItemStack(attachmentToolMotor, 1, 20), '#', "ingotSteel" }));
        
        // Auger
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 01), new Object[] { "!! ", "!@!", " !@",
                '!', Block.stone, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 11), new Object[] { "!! ", "!@!", " !@",
                '!', Item.ingotIron, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 21), new Object[] { "!! ", "!@!", " !@",
                '!', Item.diamond, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 31), new Object[] { "!! ", "!@!", " !#",
                '!', Item.emerald, '@', new ItemStack(attachmentToolMotor, 1, 21), '#', "ingotSteel" }));
        
        // Chainsaw
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 02), new Object[] { "!! ", " @!", "!@ ",
                '!', Block.stone, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 12), new Object[] { "!! ", " @!", "!@ ",
                '!', Item.ingotIron, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 22), new Object[] { "!! ", " @!", "!@ ",
                '!', Item.diamond, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 32), new Object[] { "!! ", " @!", "!# ",
                '!', Item.emerald, '@', new ItemStack(attachmentToolMotor, 1, 22), '#', "ingotSteel" }));
        
        // ChainSword
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 03), new Object[] { "! !", "! !", "!@!",
                '!', Block.stone, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 13), new Object[] { "! !", "! !", "!@!",
                '!', Item.ingotIron, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 23), new Object[] { "! !", "! !", "!@!",
                '!', Item.diamond, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 33), new Object[] { "! !", "! !", "!@!",
                '!', Item.emerald, '@', new ItemStack(attachmentToolMotor, 1, 23) }));
        
        // Shears
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 04), new Object[] { "!!!", " @ ", "!@!",
                '!', Block.stone, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 14), new Object[] { "!!!", " @ ", "!@!",
                '!', Item.ingotIron, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 24), new Object[] { "!!!", " @ ", "!@!",
                '!', Item.diamond, '@', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attachmentToolMotor, 1, 34), new Object[] { "!!!", " # ", "!@!",
                '!', Item.emerald, '@', new ItemStack(attachmentToolMotor, 1, 23), '#', "ingotSteel" }));
        
        // Cores
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(coreMechElectric, 1, 0), new Object[] { "!@#", "!!#", " $ ", '!',
                "ingotSteel", '@', "circuitBasic", '#', "plateCopper", '$', "ingotCopper" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(coreMechElectric, 1, 1), new Object[] { "!@#", "!!#", " $ ", '!',
                "ingotSteel", '@', "circuitAdvanced", '#', "plateBronze", '$', "ingotBronze" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(coreMechElectric, 1, 2), new Object[] { "!@#", "!!#", " $ ", '!',
                "ingotSteel", '@', "circuitElite", '#', "plateSteel", '$', "ingotSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(coreMechElectric, 1, 3), new Object[] { "!@#", "!!#", " $ ", '!',
                "ingotSteel", '@', "circuitElite", '#', Block.blockDiamond, '$', Item.emerald }));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimpleToolsItems.itemBattteryTier1), new Object[] {" ! ", "!@!", "!@!", '!', "ingotTin", '@', Item.redstone}));      
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimpleToolsItems.itemBattteryTier2), new Object[] {" ! ", "!@!", "!@!", '!', "ingotTin", '@', Block.blockRedstone}));      
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimpleToolsItems.itemBattteryTier3), new Object[] {" ! ", "!@!", "!@!", '!', "ingotTin", '@', Item.glowstone}));        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimpleToolsItems.itemBattteryTier4), new Object[] {" ! ", "!@!", "!@!", '!', "ingotTin", '@', Item.ghastTear}));        
    }
}
