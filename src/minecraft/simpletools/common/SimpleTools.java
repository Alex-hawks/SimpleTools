package simpletools.common;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import simpletools.common.block.BlockTableAssembly;
import simpletools.common.items.ItemAssembledTool;
import simpletools.common.items.ItemAttachmentToolMotor;
import simpletools.common.items.ItemCoreMotor;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "UE-SimpleTools", name = "Simple Tools", dependencies = "after:BasicComponents")
@NetworkMod(clientSideRequired = true, channels = "SimplePowerTools", connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class SimpleTools 
{
	private static final int FIRST_BLOCK_ID = 4040;
	private static final int FIRST_ITEM_ID = 16000;
	
	public static final String[] SUPPORTED_LANGUAGES = { "en_US" };
	
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 0;
	public static final int REVIS_VERSION = 1;
	public static final int BUILD_VERSION = 1;
	public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVIS_VERSION + "." + BUILD_VERSION;
	
	public static final File CONFIG_FILE = new File(Loader.instance().getConfigDir(), "UniversalElectricity" + File.separator + "SimpleTools.cfg");
	public static final Configuration CONFIG = new Configuration(CONFIG_FILE);
	
	public static final String RESOURCE_PATH = "/simpletools/";
	public static final String LANGUAGE_PATH = RESOURCE_PATH + "language";
	public static final String TEXTURE_PATH = RESOURCE_PATH + "textures";
	public static final String BLOCK_TEXTURES = TEXTURE_PATH + "/block.png";
	public static final String ITEM_TEXTURES = TEXTURE_PATH + "items.png";
	
	public static Block tableAssembly;
	public static Block tableRefuel;
	public static Block tablePlasma;

	public static Item assembledTool;
	public static Item coreMotor;
	public static Item corePlasma;
	public static Item coreEngine;
	public static Item attachmentToolMotor;
	public static Item attachmentToolPlasma;
	public static Item attachmentMWeaponMotor;
	public static Item attachmentMWeaponPlasma;
	public static Item attachmentRWeaponMotor;
	public static Item attachmentRWeaponPlasma;
	
	private static void configLoad(Configuration config)
	{
		config.load();

		tableAssembly			= new BlockTableAssembly(config.getBlock("Assembly_Table", FIRST_BLOCK_ID).getInt());
//1		tableRefuel				= 
//2		tablePlasma				=
		
		assembledTool			= new ItemAssembledTool(config.getItem("Tool", FIRST_ITEM_ID).getInt(), "AssembledTool");
		
		coreMotor 				= new ItemCoreMotor(config.getItem("Motor_Core", FIRST_ITEM_ID + 1).getInt(), "CoreMotor");
//2		corePlasma				=
//3		coreEngine				=
		
		attachmentToolMotor		= new ItemAttachmentToolMotor(config.getItem("Motor_Tool_Attachment", FIRST_ITEM_ID + 4).getInt(), "MotorTool");
//5		attachmentToolPlasma	=
//6		attachmentMWeaponMotor	=
//7		attachmentMWeaponPlasma =
//8		attachmentRWeaponMotor	=
//9		attachmentRWeaponPlasma	=
		
		config.save();
	}
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		configLoad(CONFIG);
	}
	
	@Init
	public void init(FMLInitializationEvent event)
	{
		
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
