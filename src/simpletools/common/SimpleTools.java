package simpletools.common;

import java.io.File;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import simpletools.common.block.BlockTableAssembly;
import simpletools.common.items.ItemAssembledToolElectric;
import simpletools.common.items.ItemAttachmentToolMotor;
import simpletools.common.items.ItemCoreMotor;
import simpletools.common.misc.STTickHandler;
import simpletools.common.misc.SimpleToolsCreativeTab;
import simpletools.common.misc.SimpleToolsEventHandler;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = SimpleTools.MOD_ID, name = SimpleTools.MOD_NAME, dependencies = SimpleTools.DEPENDENCIES, version = SimpleTools.VERSION)
@NetworkMod(clientSideRequired = SimpleTools.USES_CLIENT, serverSideRequired = SimpleTools.USES_SERVER, channels = SimpleTools.CHANNELS,
packetHandler = PacketManager.class, connectionHandler = ConnectionHandler.class)
public class SimpleTools 
{
	//	@Mod Prerequisites
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 0;
	public static final int REVIS_VERSION = 1;
	public static final int BUILD_VERSION = 1;

	//	@Mod
	public static final String MOD_ID 		= "UE-SimpleTools";
	public static final String MOD_NAME 	= "Simple Tools";
	public static final String VERSION 		= MAJOR_VERSION + "." + MINOR_VERSION + "." + REVIS_VERSION + "." + BUILD_VERSION;
	public static final String DEPENDENCIES	= "after:UniversalElectricity";
	public static final boolean useMetadata	= true;
	
	//	@NetworkMod
	public static final boolean USES_CLIENT	= true;
	public static final boolean USES_SERVER	= false;
	public static final String CHANNELS		= "SimplePowerTools";
	
	@Metadata(SimpleTools.MOD_ID)
	public static ModMetadata meta;
	
	private static final int FIRST_BLOCK_ID = 4040;
	private static final int FIRST_TOOL_ID 	= 16000;
	private static final int FIRST_CORE_ID 	= 16010;
	private static final int FIRST_BIT_ID 	= 16020;
	
	@Instance(SimpleTools.MOD_ID)
	public static SimpleTools INSTANCE;
	public static SimpleToolsEventHandler EVENT_HANDLER = new SimpleToolsEventHandler();
	
	@SidedProxy(clientSide = "simpletools.client.SimpleToolsClientProxy", serverSide = "simpletools.common.SimpleToolsCommonProxy")
	public static SimpleToolsCommonProxy proxy;
	
	public static final String[] SUPPORTED_LANGUAGES = { "en_US" };
	
	public static final File CONFIG_FILE = new File(Loader.instance().getConfigDir(), "UniversalElectricity" + File.separator + "SimpleTools.cfg");
	public static final Configuration CONFIG = new Configuration(CONFIG_FILE);
	
	public static final String RESOURCE_PATH = "/mods/simpletools/";
	public static final String LANGUAGE_PATH = RESOURCE_PATH + "languages/";
	public static final String TEXTURE_PATH = RESOURCE_PATH + "textures/";
	public static final String BLOCK_TEXTURES = TEXTURE_PATH + "blocks/";
	public static final String ITEM_TEXTURES = TEXTURE_PATH + "items/";
	public static final String TEXTURE_NAME_PREFIX = "simpletools:";
	
	public static Block tableAssembly;
	public static Block tableRefuel;
	public static Block tablePlasma;

	public static Item assembledToolElectric;
	public static Item assembledToolPlasma;
	public static Item assembledToolFuel;
	public static Item coreMechElectric;
	public static Item corePlasma;
	public static Item coreMechFuel;
	public static Item attachmentToolMotor;
	public static Item attachmentToolPlasma;
	public static Item attachmentMWeaponMotor;
	public static Item attachmentMWeaponPlasma;
	
	private static void configLoad(Configuration config)
	{
		config.load();

		tableAssembly			= new BlockTableAssembly(config.getBlock("Assembly_Table", FIRST_BLOCK_ID).getInt());
//1		tableRefuel				= 
//2		tablePlasma				=
		
		assembledToolElectric	= new ItemAssembledToolElectric(config.getItem("ElectricAssembledTool", FIRST_TOOL_ID).getInt(), "ElectricAssembledTool");
//1		assembledToolPlasma		=
//2		assembledToolFuel
		
		coreMechElectric 		= new ItemCoreMotor(config.getItem("Electric_Motor_Core", FIRST_CORE_ID + 1).getInt(), "CoreElectric");
//1		corePlasma				=
//2		coreMechFuel			=
		
		attachmentToolMotor		= new ItemAttachmentToolMotor(config.getItem("Motor_Tool_Attachment", FIRST_BIT_ID + 4).getInt(), "MotorTool");
//1		attachmentToolPlasma	=
//3		attachmentWeaponPlasma	=
		
		config.save();
	}
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		meta.modId 			= SimpleTools.MOD_ID;
		meta.name 			= SimpleTools.MOD_NAME;
		meta.description	= "Adding (somewhat) simple modular tools to Universal Electricity";
		
		meta.url 			= "http://universalelectricity.com";
		meta.updateUrl		= "";	// TODO: Add the update URL
		
		meta.logoFile		= "";	// TODO: Add the Logo
		meta.version 		= SimpleTools.VERSION;
		meta.authorList 	= Arrays.asList(new String[] { "Alex_hawks" });
		meta.credits		= "Please see the website.";
		meta.autogenerated 	= false;
		
		configLoad(CONFIG);
		GameRegistry.registerBlock(tableAssembly, ItemBlock.class, "tableAssembly", this.MOD_ID);
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);
	}
	
	@Init
	public void init(FMLInitializationEvent event) 
	{
		proxy.init();
		for (int i = 0; i < SUPPORTED_LANGUAGES.length; i++)
			LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + SUPPORTED_LANGUAGES[i] + ".properties", SUPPORTED_LANGUAGES[i], false);
		MinecraftForge.EVENT_BUS.register(this.EVENT_HANDLER);
		Item.itemsList[this.assembledToolElectric.itemID] = this.assembledToolElectric;
		TickRegistry.registerTickHandler(STTickHandler.INSTANCE, Side.CLIENT);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		SimpleToolsCreativeTab.INSTANCE.setItemStack(new ItemStack(this.tableAssembly));
	}
}
