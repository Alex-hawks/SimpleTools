package simpletools.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeRegistry
{
	public void registerRecipes()
	{
		Item attach = SimpleTools.assembledToolElectric;
		Item core = SimpleTools.coreMechElectric;
		
		//	Drill
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 00), new Object[] { " ! ", "!@!", "!@!", '!', Block.stone, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 10), new Object[] { " ! ", "!@!", "!@!", '!', Item.ingotIron, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 20), new Object[] { " ! ", "!@!", "!@!", '!', Item.diamond, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 30), new Object[] { " ! ", "!@!", "!#!", '!', Item.emerald, '@', new ItemStack(attach, 1, 20), '#', "ingotSteel" }));
		
		//	Auger
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 01), new Object[] { "!! ", "!@!", " !@", '!', Block.stone, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 11), new Object[] { "!! ", "!@!", " !@", '!', Item.ingotIron, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 21), new Object[] { "!! ", "!@!", " !@", '!', Item.diamond, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 31), new Object[] { "!! ", "!@!", " !#", '!', Item.emerald, '@', new ItemStack(attach, 1, 21), '#', "ingotSteel" }));
		
		//	Chainsaw
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 02), new Object[] { "!! ", " @!", "!@ ", '!', Block.stone, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 12), new Object[] { "!! ", " @!", "!@ ", '!', Item.ingotIron, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 22), new Object[] { "!! ", " @!", "!@ ", '!', Item.diamond, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 32), new Object[] { "!! ", " @!", "!# ", '!', Item.emerald, '@', new ItemStack(attach, 1, 22), '#', "ingotSteel" }));

		//	ChainSword
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 03), new Object[] { "! !", "! !", "!@!", '!', Block.stone, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 13), new Object[] { "! !", "! !", "!@!", '!', Item.ingotIron, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 23), new Object[] { "! !", "! !", "!@!", '!', Item.diamond, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 33), new Object[] { "! !", "! !", "!@!", '!', Item.emerald, '@', new ItemStack(attach, 1, 23) }));
		
		//	Shears
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 04), new Object[] { "!!!", " @ ", "!@!", '!', Block.stone, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 14), new Object[] { "!!!", " @ ", "!@!", '!', Item.ingotIron, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 24), new Object[] { "!!!", " @ ", "!@!", '!', Item.diamond, '@', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(attach, 1, 34), new Object[] { "!!!", " # ", "!@!", '!', Item.emerald, '@', new ItemStack(attach, 1, 23), '#', "ingotSteel" }));
		
		//	Cores
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(core, 1, 0), new Object[] { "!!!", " @ ", "!@!", '!', Block.stone, '@', "ingotSteel" }));
		
	}
}
