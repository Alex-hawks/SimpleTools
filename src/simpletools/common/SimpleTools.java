package simpletools.common;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import simpletools.api.SimpleToolsItems;
import simpletools.common.block.BlockPlasmaTorch;
import simpletools.common.block.BlockTableAssembly;
import simpletools.common.block.BlockTablePlasma;
import simpletools.common.items.ItemAssembledToolElectric;
import simpletools.common.items.ItemAssembledToolPlasma;
import simpletools.common.items.ItemAttachmentPlasma;
import simpletools.common.items.ItemAttachmentToolMotor;
import simpletools.common.items.ItemCoreMotor;
import simpletools.common.items.ItemCorePlasma;
import simpletools.common.misc.SimpleToolsCreativeTab;
import simpletools.common.misc.SimpleToolsEventHandler;
import calclavia.lib.network.PacketHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = SimpleTools.MOD_ID, name = SimpleTools.MOD_NAME, dependencies = SimpleTools.DEPENDENCIES, version = SimpleTools.VERSION, useMetadata = SimpleTools.USE_METADATA)
@NetworkMod(clientSideRequired = SimpleTools.USES_CLIENT, serverSideRequired = SimpleTools.USES_SERVER, channels = SimpleTools.CHANNELS, packetHandler = PacketHandler.class)
public class SimpleTools
{
    // @Mod Prerequisites
    public static final int MAJOR_VERSION = 0;
    public static final int MINOR_VERSION = 0;
    public static final int REVIS_VERSION = 2;
    public static final int BUILD_VERSION = 2;
    
    // @Mod
    public static final String MOD_ID = "UE-SimpleTools";
    public static final String MOD_NAME = "Simple Tools";
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVIS_VERSION + "." + BUILD_VERSION;
    public static final String DEPENDENCIES = "after:UniversalElectricity;after:AtomicScience";
    public static final boolean USE_METADATA = true;
    
    // @NetworkMod
    public static final boolean USES_CLIENT = true;
    public static final boolean USES_SERVER = false;
    public static final String CHANNELS = "SimplePowerTools";
    
    @Metadata(SimpleTools.MOD_ID)
    public static ModMetadata meta;
    
    private static final int FIRST_BLOCK_ID = 4040;
    private static final int FIRST_TOOL_ID = 16000;
    private static final int FIRST_CORE_ID = 16005;
    private static final int FIRST_BIT_ID = 16010;
    
    @Instance(SimpleTools.MOD_ID)
    public static SimpleTools INSTANCE;
    public static SimpleToolsEventHandler EVENT_HANDLER = new SimpleToolsEventHandler();
    public static Logger STLogger = Logger.getLogger("SimpleTools");
    
    @SidedProxy(clientSide = "simpletools.client.SimpleToolsClientProxy", serverSide = "simpletools.common.SimpleToolsCommonProxy")
    public static SimpleToolsCommonProxy proxy;
    
    public static final String[] SUPPORTED_LANGUAGES = { "en_US" };
    
    public static final File CONFIG_FILE = new File(Loader.instance().getConfigDir(), "UniversalElectricity" + File.separator + "SimpleTools.cfg");
    public static final Configuration CONFIG = new Configuration(CONFIG_FILE);
    
    public static final String PREFIX = "simpletools";
    public static final String DOMAIN = PREFIX + ":";
    public static final String RESOURCE_PATH = "/assets/" + PREFIX + "/";
    public static final String LANGUAGE_PATH = RESOURCE_PATH + "languages/";
    public static final String TEXTURE_PATH = "textures/";
    public static final String BLOCK_TEXTURES = TEXTURE_PATH + "blocks/";
    public static final String ITEM_TEXTURES = TEXTURE_PATH + "items/";
    public static final String MODEL_TEXTURE_PATH = TEXTURE_PATH + "model/";
    public static final String GUI_TEXTURE_PATH = TEXTURE_PATH + "gui/";
    public static final String CLOAK_PATH = TEXTURE_PATH + "cloaks/";
    
    private static void configLoad(Configuration config)
    {
        config.load();
        
        SimpleToolsItems.tableAssembly = new BlockTableAssembly(config.getBlock("Assembly_Table", FIRST_BLOCK_ID).getInt());
        // 1 tableRefuel =
        SimpleToolsItems.tablePlasma = new BlockTablePlasma(config.getBlock("Plasma_Table", FIRST_BLOCK_ID + 2).getInt());
        SimpleToolsItems.plasmaTorch = new BlockPlasmaTorch(config.getBlock("Plasma_Torch", FIRST_BLOCK_ID + 3).getInt());
        
        SimpleToolsItems.assembledToolElectric = new ItemAssembledToolElectric(config.getItem("Electric_Assembled_Tool", FIRST_TOOL_ID).getInt(), "MechElectricAssembledTool");
        SimpleToolsItems.assembledToolPlasma = new ItemAssembledToolPlasma(config.getItem("Plasma_Assembled_Tool", FIRST_TOOL_ID + 1).getInt(), "PlasmaAssembledTool");
        // 2 assembledToolFuel
        
        SimpleToolsItems.coreMechElectric = new ItemCoreMotor(config.getItem("Electric_Motor_Core", FIRST_CORE_ID).getInt(), "CoreMechElectric");
        SimpleToolsItems.corePlasma = new ItemCorePlasma(config.getItem("Plasma_Core", FIRST_CORE_ID + 1).getInt(), "CorePlasma");
        // 2 coreMechFuel =
        
        SimpleToolsItems.attachmentToolMotor = new ItemAttachmentToolMotor(config.getItem("Motor_Tool_Attachment", FIRST_BIT_ID).getInt(), "MotorTool");
        SimpleToolsItems.attachmentToolPlasma = new ItemAttachmentPlasma(config.getItem("Plasma_Tool_Attachment", FIRST_BIT_ID + 1).getInt(), "PlasmaTool");
        
        if (config.hasChanged())
            config.save();
    }
    
    public static void log(Level level, String msg, String... replacements)
    {
        for (String replace : replacements)
        {
            msg = msg.replace("%s", replace);
        }
        STLogger.log(level, msg);
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        meta.modId = SimpleTools.MOD_ID;
        meta.name = SimpleTools.MOD_NAME;
        meta.description = "Adding (somewhat) simple modular tools to Universal Electricity";
        
        meta.url = "http://universalelectricity.com"; // TODO fix the URL
        meta.updateUrl = ""; // TODO: Add the update URL
        
        meta.logoFile = ""; // TODO: Add the Logo
        meta.version = SimpleTools.VERSION;
        meta.authorList = Arrays.asList(new String[] { "Alex_hawks" });
        meta.credits = "Please see the website.";
        meta.autogenerated = false;
        
        configLoad(CONFIG);
        
        GameRegistry.registerBlock(SimpleToolsItems.tableAssembly, ItemBlock.class, "tableAssembly", SimpleTools.MOD_ID);
        
        GameRegistry.registerBlock(SimpleToolsItems.tablePlasma, ItemBlock.class, "tablePlasma", SimpleTools.MOD_ID);
        GameRegistry.registerBlock(SimpleToolsItems.plasmaTorch, ItemBlock.class, "plasmaTorch", SimpleTools.MOD_ID);
        
        NetworkRegistry.instance().registerGuiHandler(this, proxy);
        //NetworkRegistry.instance().registerGuiHandler(this, SimpleTools.proxy);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        STLogger.setParent(FMLLog.getLogger());
        
        proxy.init();
        for (String element : SUPPORTED_LANGUAGES)
        {
            LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + element + ".properties", element, false);
        }
        MinecraftForge.EVENT_BUS.register(SimpleTools.EVENT_HANDLER);
        Item.itemsList[SimpleToolsItems.assembledToolElectric.itemID] = SimpleToolsItems.assembledToolElectric;
        
        RecipeRegistry.registerRecipes();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        SimpleToolsCreativeTab.INSTANCE.setItemStack(new ItemStack(SimpleToolsItems.tableAssembly));
        
        if (Loader.isModLoaded("NotEnoughItems"))
        {
            try
            {
                codechicken.nei.api.API.hideItem(SimpleToolsItems.assembledToolElectric.itemID);
                codechicken.nei.api.API.hideItem(SimpleToolsItems.assembledToolPlasma.itemID);
                codechicken.nei.api.API.hideItem(SimpleToolsItems.plasmaTorch.blockID);
            }
            catch (Exception e)
            {
                log(Level.WARNING, "NEI Integration has failed...");
                log(Level.WARNING, "Please give Alex_hawks the following stacktrace.");
                e.printStackTrace();
                log(Level.WARNING, "Spamming console to make more obvious...");
                for (int i = 0; i < 15; i++)
                {
                    log(Level.WARNING, "Something Broke. See above.");
                }
            }
            
        }
    }
}
