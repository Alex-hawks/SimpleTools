package simpletools.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import simpletools.common.interfaces.IAttachment;
import simpletools.common.interfaces.ICore;
import simpletools.common.misc.SimpleToolsCreativeTab;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.prefab.ItemElectric;

public class ItemAssembledTool extends ItemTool implements IItemElectric
{
	private static final double energyPerOperation = 0;
	private static final double plasmaPerOperation = 0;

	private ItemStack core;
	private ItemStack attachment;

	private ItemElectric battery;
	private double energyStored = 0;
	private double maxEnergy;

	private ItemStack fuelTank;
	private double fuelStored = 0;
	private double maxFuel;

	private double plasmaStored;
	private double maxPlasma;
	private double fuelPerOperation;;

	public ItemAssembledTool(int itemID, String name)
	{
		super(itemID, 0, EnumToolMaterial.EMERALD, new Block[0]);
		this.damageVsEntity = 1;
		this.setItemName(name);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setIconIndex(0);
		this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		return maxEnergy;
	}

	@Override
	public double getVoltage(Object... data)
	{
		return battery.getVoltage(data);
	}

	private double getMaxPlasma()
	{
		return this.maxPlasma;
	}

	private double getMaxFuel()
	{
		return this.fuelStored;
	}

	/**
	 * @param attachment The attachment, passed in as an ItemStack
	 * @param core The core, passed in as an ItemStack
	 * @param batt The battery, passed in as an ItemElectric
	 * @return the resultant Item, as an ItemStack. null if it failed.
	 */
	public ItemStack onCreate(ItemStack attachment, ItemStack core, ItemElectric batt)
	{
		ItemStack returnStack = null;
		if (attachment.getItem() instanceof IAttachment && core.getItem() instanceof ICore)
		{
			IAttachment attachmentTemp = (IAttachment) attachment.getItem();
			ICore coreTemp = (ICore) attachment.getItem();

			if (coreTemp.getCoreType(core) == attachmentTemp.getToolAttachmentType(attachment))
			{
				if (coreTemp.getCoreTier(core) >= attachmentTemp.getMinimumTier(attachment))
				{
					int coreMeta = ((int)coreTemp.getCoreUID(core)) * 1000;
					int attachMeta = attachmentTemp.getAttachmentUID(attachment);
					int meta = coreMeta + attachMeta;
					returnStack = new ItemStack(this, 1, meta);
					this.attachment = attachment;
					this.core = core;
					this.battery = batt;
					this.energyStored = batt.getJoules();
					this.maxEnergy = batt.getMaxJoules();

					if (coreTemp.getCoreType(core) == 1)
						this.plasmaStored = 0;
				}
			}
		}
		return returnStack;
	}

	@Override
	public double getJoules(Object... data)
	{
		if (((ICore)core.getItem()).getCoreFinerType(this.core) == "electric")
			return this.energyStored;
		else return 0.0;
	}

	@Override
	public void setJoules(double joules, Object... data)
	{
		if (((ICore)core.getItem()).getCoreFinerType(this.core) == "electric")
			this.energyStored = joules;
	}

	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack)
	{
		if (((ICore)core.getItem()).getCoreFinerType(this.core) == "electric")
		{
			double rejectedElectricity = Math.max((this.getJoules(itemStack) + ElectricInfo.getJoules(amps, voltage, 1)) - this.getMaxJoules(itemStack), 0);
			this.setJoules(this.getJoules(itemStack) + ElectricInfo.getJoules(amps, voltage, 1) - rejectedElectricity, itemStack);
			return rejectedElectricity;
		}
		return ElectricInfo.getJoules(amps, voltage, 1);
	}

	@Override
	public double onUse(double joulesNeeded, ItemStack itemStack)
	{
		if (((ICore)core.getItem()).getCoreFinerType(this.core) == "electric")
		{
			double electricityToUse = Math.min(this.getJoules(itemStack), joulesNeeded);
			this.setJoules(this.getJoules(itemStack) - electricityToUse, itemStack);
			return electricityToUse;
		}
		return 0d;
	}

	@Override
	public boolean canReceiveElectricity()
	{
		return ((ICore)core.getItem()).getCoreFinerType(this.core) == "electric";
	}

	@Override
	public boolean canProduceElectricity()
	{
		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack itemStack, Block block)
	{
		return 1f;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List currentTips, boolean advancedToolTips)
	{
		if (this.core != null && this.attachment != null && this.battery != null)
		{
			if (((ICore)core.getItem()).getCoreFinerType(this.core) == "electric" && this.battery != null)
			{
				currentTips.add("Energy: " + this.energyStored + " / " + this.getMaxJoules());
			}
			else if (((ICore)core.getItem()).getCoreFinerType(this.core) == "plasma" && this.battery != null)
			{
				currentTips.add("Energy: " + this.energyStored + " / " + this.getMaxJoules());
				currentTips.add("Plasma: " + this.plasmaStored + " / " + this.getMaxPlasma());
			}
			else if (((ICore)core.getItem()).getCoreFinerType(this.core) == "fuel")
			{
				currentTips.add("Fuel: " + this.fuelStored + " / " + this.getMaxFuel());
			}

			if (advancedToolTips)
			{
				currentTips.add("Core Module: " + this.core.getDisplayName());
				currentTips.add("Tool Module: " + this.attachment.getDisplayName());
				currentTips.add("Batt Module: " + new ItemStack(battery).getDisplayName());
			}
		}
		else
		{
			currentTips.add("Creative Mode Tool");
		}
	}

	public boolean onItemUse(ItemStack i)
	{
		if (((ICore)this.core.getItem()).getCoreFinerType(i) == "electric")
		{
			if (this.energyStored >= this.energyPerOperation)
			{
				this.energyStored = this.energyStored - this.energyPerOperation;
				return true;
			}
		}
		else if (((ICore)this.core.getItem()).getCoreFinerType(i) == "plasma")
		{
			if (this.energyStored >= this.energyPerOperation && plasmaStored >= plasmaPerOperation)
			{
				this.energyStored = this.energyStored - this.energyPerOperation;
				this.plasmaStored = this.plasmaStored - this.plasmaPerOperation;
				return true;
			}
		}
		else if (((ICore)this.core.getItem()).getCoreFinerType(i) == "fuel")
		{
			if (this.fuelStored >= this.fuelPerOperation)
			{
				this.fuelStored = this.fuelStored - this.fuelPerOperation;
				return true;
			}
		}
		return false;
	}

	@Override
	public int getItemEnchantability()
	{
		return 0;
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving)
	{
		double blockHardness = (double)Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6);
		if (blockHardness != 0D)
		{
			return this.onItemUse(par1ItemStack);
		}

		return true;
	}

}
