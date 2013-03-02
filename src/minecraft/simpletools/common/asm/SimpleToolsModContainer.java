package simpletools.common.asm;

import java.util.Arrays;

import simpletools.common.SimpleTools;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class SimpleToolsModContainer extends DummyModContainer
{
	//	Data for Mod Metadata, but not @Mod or @NetworkMod
	public static final String DESCRIPTION	= "Adding (somewhat) simple modular tools to Universal Electricity";
	public static final String[] authors 	= { "Alex_hawks" };
	
	public SimpleToolsModContainer() 
	{
		super(new ModMetadata());
		ModMetadata modMeta 	= super.getMetadata();
		
		modMeta.modId 			= SimpleTools.MOD_ID;
		modMeta.name 			= SimpleTools.MOD_NAME;
		modMeta.description		= this.DESCRIPTION;
		
		modMeta.url 			= "http://universalelectricity.com";
		modMeta.updateUrl		= "";	// TODO: Add the update URL
		
		modMeta.logoFile		= "";	// TODO: Add the Logo
		modMeta.version 		= SimpleTools.VERSION;
		modMeta.authorList 		= Arrays.asList(new String[] { "Alex_hawks" });
		modMeta.credits			= 	"In no particular order:" +
									"Calclavia: For the UE API, which was and is a great springboard to start from." +
									"ChickenBones: For understanding ASM, which lead to me being able to learn how to make the required changes to a class, without base-edits." +
									"mDiyo: For creating InfiTools, whic was part of the inspiration of this mod. This was coded seperately from any of the mods created by mDiyo." +
									"VSWE: For creating Steve's Carts 2. SC2 is one of the things that gave me the idea for Modular Tools, in this implementation." +
									"MachineMuse: For MMMPS, a mod which showed me how to read, and write, ItemStack NBT. It was too confusing for me to copy the code though, but that doesn't matter." +
									"Eearslya: For creating the original \"Power Tools for UE\" mod. No code was copied, as it would have been more effort than it was worth." +
									"Forge Dev. Team: Need I write more?" +
									"Mojang: For Minecraft, an awesome game to both play and mod.";
	}
}
