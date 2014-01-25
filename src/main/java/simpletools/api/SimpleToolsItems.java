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
    public static Block blockTableAssembly;
    @Deprecated
    public static Block blockTableRefuel;
    public static Block blockTablePlasma;
    public static Block blockPlasmaTorch;
    
    public static Item itemAssembledToolElectric;
    public static Item itemAssembledToolPlasma;
    @Deprecated
    public static Item itemAssembledToolFuel;
    
    public static Item itemCoreMechElectric;
    public static Item itemCorePlasma;
    @Deprecated
    public static Item itemCoreMechFuel;
    
    public static Item itemAttachmentToolMotor;
    public static Item itemAttachmentToolPlasma;
    
    @Deprecated
    public static Item itemElectricBoots;
    @Deprecated
    public static Item itemElectricLegs;
    @Deprecated
    public static Item itemElectricChestPlate;
    @Deprecated
    public static Item itemElectricHelmet;
    
    public static Item itemBattteryTier1;
    public static Item itemBattteryTier2;
    public static Item itemBattteryTier3;
    public static Item itemBattteryTier4;
}
