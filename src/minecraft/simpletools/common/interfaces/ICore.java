package simpletools.common.interfaces;

import net.minecraft.item.ItemStack;

/**
 * @author Alex_hawks
 */
public interface ICore
{
	/**
	 * @param i	The ItemStack, to provide the metadata to the method.
	 * @return	0: Mechanical
	 * 			1: Plasma
	 */
	public int getCoreType(ItemStack i);
	
	/**
	 * @param i	The ItemStack, to provide the metadata to the method.
	 * @return	0: Basic
	 * 			1: Advanced
	 * 			2: Elite
	 * 			3: Ultimate
	 */
	public int getCoreTier(ItemStack i);
	
	/**
	 * @param i	The ItemStack, to provide the metadata to the method.
	 * @return	The Unique Identifier of the core. POSITIVE VALUES ONLY!
	 */
	public byte getCoreUID(ItemStack i);
	
	/**
	 * @param i	The ItemStack, to provide the metadata to the method.
	 * @return	What the core is powered by. f.e. "electric", "fuel", "plasma"
	 */
	public String getCoreFinerType(ItemStack i);
	
	/**
	 * @param i	The ItemStack, to provide the metadata to the method.
	 * @return	true if the core uses electricity, false if it uses something else
	 */
	public boolean requiresElectricity(ItemStack i);
	
	/**
	 * @param i	The ItemStack, to provide the metadata to the method.
	 * @return	true if the core requires an secondary fuel, such as plasma 
	 */
	public boolean usesAltFuel(ItemStack i);

	/**
	 * @param i	The ItemStack, to provide the metadata to the method.
	 * @return	The Maximum storage of an alternative fuel, such as plasma in a plasma core, 
	 * 			or lubricant in an engine
	 */
	public double getMaxAltFuel(ItemStack i);

	/**
	 * @param i	The ItemStack, to provide the metadata to the method.
	 * @return	The energy (fuel, electricity, etc) required to do 1 action
	 */
	public double getPrimaryEnergyPerOperation(ItemStack i);

	/**
	 * 			Not called if usesAltFuel() is false
	 * @param i	The ItemStack, to provide the metadata to the method.
	 * @return	The amount of secondary fuel used. 
	 */
	public double getSecondaryEnergyPerOperation(ItemStack i);
}
