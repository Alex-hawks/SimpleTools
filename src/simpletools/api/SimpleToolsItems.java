package simpletools.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * @author Alex_hawks
 * 
 *  This class contains the variables for all of the Simple Tools Items and Blocks.
 *  Before referencing these, check if they are not null, and you should check if 
 *  Simple Tools is loaded as well, particularly if recipes are involved.
 *  Any that aren't used will be marked as deprecated...
 */
public class SimpleToolsItems
{
    public static Block tableAssembly;
    @Deprecated
    public static Block tableRefuel;
    @Deprecated
    public static Block tablePlasma;
    public static Block plasmaTorch;
    
    public static Item assembledToolElectric;
    public static Item assembledToolPlasma;
    @Deprecated
    public static Item assembledToolFuel;
    
    public static Item coreMechElectric;
    public static Item corePlasma;
    @Deprecated
    public static Item coreMechFuel;
    
    public static Item attachmentToolMotor;
    public static Item attachmentToolPlasma;
    
    @Deprecated
    public static Item electricBoots;
    @Deprecated
    public static Item electricLegs;
    @Deprecated
    public static Item electricChestPlate;
    @Deprecated
    public static Item electricHelmet;
}
