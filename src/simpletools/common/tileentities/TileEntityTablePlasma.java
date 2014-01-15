package simpletools.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import simpletools.api.IPlasmaStorage;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.item.IEnergyItem;
import calclavia.lib.network.IPacketReceiver;
import calclavia.lib.prefab.tile.IRedstoneProvider;
import calclavia.lib.prefab.tile.TileElectrical;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityTablePlasma extends TileElectrical
implements IRedstoneProvider, IPacketReceiver, IInventory, IEnergyInterface, IEnergyContainer
{
    /**
     *      0: Battery to power the plasma machine
     * </br>1: Tool 
     * </br>2: Deuterium, Tritium, or Water
     * </br>3: Empty Cells
     */
    private ItemStack[] inventory = new ItemStack[4];
    private long joules = 0;
    private int plasma = 0;
    private int rawFuel = 0;
    
    private static final long MAX_JOULES = 50000;
    /** In Milli-Buckets */
    private static final int MAX_RAW_FUEL = 10_000;
    /** In Units */
    private static final int MAX_PLASMA = 1000;
    
    private static final double RAW_FUEL_PER_DEUTERIUM = 1500;
    private static final double RAW_FUEL_PER_TRITIUM = 3000;
    /** In Milli-Buckets */
    private static final double INPUT_PER_OPERATION = 1000;
    private static final double JOULES_PER_OPERATION = 5000;
    private static final double PLASMA_PER_OPERATION = 15;
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (this.rawFuel <= MAX_RAW_FUEL - RAW_FUEL_PER_DEUTERIUM)
        {
            this.inventory[2].stackSize--;
            this.rawFuel += RAW_FUEL_PER_DEUTERIUM;
        }
        
        if (this.rawFuel <= MAX_RAW_FUEL - RAW_FUEL_PER_TRITIUM && OreDictionary.getOres("cellTritium").contains(this.inventory[2]))
        {
            this.inventory[2].stackSize--;
            this.rawFuel += RAW_FUEL_PER_TRITIUM;
        }
        
        if (this.joules >= JOULES_PER_OPERATION && this.rawFuel >= INPUT_PER_OPERATION && this.plasma + PLASMA_PER_OPERATION <= MAX_PLASMA)
        {
            this.joules -= JOULES_PER_OPERATION;
            this.rawFuel -= RAW_FUEL_PER_TRITIUM;
            this.plasma += PLASMA_PER_OPERATION;
        }
        
        if (this.inventory[1].getItem() instanceof IPlasmaStorage)
        {
            if (((IPlasmaStorage) this.inventory[1].getItem()).addPlasma(this.inventory[1], 1))
            {
                this.plasma--;
            }
        }
    }
    
    @Override
    public int getSizeInventory()
    {
        return this.inventory.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int i)
    {
        return i < this.inventory.length ? this.inventory[i] : null;
    }
    
    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack stack = this.getStackInSlot(i);
        ItemStack toReturn = null;
        if (stack != null)
        {
            if (stack.stackSize > j)
            {
                toReturn = stack.copy();
                toReturn.stackSize = j;
                this.inventory[i].stackSize -= j;
            }
            else if (stack.stackSize == j)
            {
                toReturn = stack.copy();
                toReturn.stackSize = j;
                this.inventory[i] = null;
            }
        }
        return toReturn;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return i < this.inventory.length ? this.inventory[i] : null;
    }
    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (i < this.inventory.length && i >= 0 && itemstack != null)
        {
            this.inventory[i] = itemstack;
        }
    }
    
    @Override
    public String getInvName()
    {
        return StatCollector.translateToLocal("tile.tablePlasma.name");
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }
    
    @Override
    public void openChest()
    {
    }
    
    @Override
    public void closeChest()
    {
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        if (itemstack != null)
        {
            switch (i)
            {
                case 0:
                    return itemstack.getItem() instanceof IEnergyItem;
                case 1:
                    return itemstack.getItem() instanceof IPlasmaStorage;
                case 2:
                    return OreDictionary.getOres("cellDeuterium").contains(itemstack)
                            || OreDictionary.getOres("cellTritium").contains(itemstack);
            }
        }
        return false;
    }
    
    @Override
    public long getEnergyCapacity(ForgeDirection from)
    {
        return MAX_JOULES;
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return direction.ordinal() == this.getBlockMetadata();
    }
    
    @Override
    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        if (this.worldObj.isRemote)
        {
            // client
            try
            {
                this.joules = data.readLong();
                this.plasma = data.readInt();
                this.rawFuel = data.readInt();
            }
            catch (Exception e)
            {
                // this try/catch is to get the storage from the server.
                // if it fails, we just wait for the next packet.
            }
        }
        else
        {
            // server
        }
    }
    
    @Override
    public boolean isPoweringTo(ForgeDirection side)
    {
        return false;
    }
    
    @Override
    public boolean isIndirectlyPoweringTo(ForgeDirection side)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
