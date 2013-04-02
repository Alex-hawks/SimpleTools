package simpletools.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import simpletools.common.interfaces.IAssembledTool;
import simpletools.common.interfaces.IPlasmaStorage;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityTablePlasma extends TileEntityElectrical implements
        IRedstoneProvider, IPacketReceiver, IInventory, IElectricityStorage
{
    /**
     * 0: Battery </br>1: Tool </br>2: Deuterium AND/OR Tritium </br>3: Empty
     * Cells
     */
    private ItemStack[] inventory = new ItemStack[4];
    private double joules = 0;
    private int plasma = 0;
    private int deuterium = 0;
    private int tritium = 0;
    
    private static final double MAX_JOULES = 50000;
    private static final int MAX_PLASMA = 200;
    private static final int MAX_DEUTERIUM = 100;
    private static final int MAX_TRITIUM = 100;
    
    private static final int PLASMA_PER_DEUTERIUM = 5;
    private static final int PLASMA_PER_TRITIUM = 10;
    private static final double JOULES_PER_OPERATION = 5000;
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (this.deuterium < MAX_DEUTERIUM
                && OreDictionary.getOres("cellDeuterium").contains(
                        this.inventory[2]))
        {
            this.inventory[2].stackSize--;
            this.deuterium++;
        }
        
        if (this.tritium < MAX_TRITIUM
                && OreDictionary.getOres("cellTritium").contains(
                        this.inventory[2]))
        {
            this.inventory[2].stackSize--;
            this.tritium++;
        }
        
        if (this.joules >= JOULES_PER_OPERATION && this.tritium > 0
                && this.plasma + PLASMA_PER_TRITIUM <= MAX_PLASMA)
        {
            this.joules -= JOULES_PER_OPERATION;
            this.tritium--;
            this.plasma += PLASMA_PER_TRITIUM;
        }
        else if (this.joules >= JOULES_PER_OPERATION && this.deuterium > 0
                && this.plasma + PLASMA_PER_DEUTERIUM <= MAX_PLASMA)
        {
            this.joules -= JOULES_PER_OPERATION;
            this.deuterium--;
            this.plasma += PLASMA_PER_DEUTERIUM;
        }
        
        if (this.inventory[1].getItem() instanceof IPlasmaStorage)
        {
            if (((IPlasmaStorage) this.inventory[1].getItem()).addPlasma(
                    this.inventory[1], 1))
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
    public boolean isStackValidForSlot(int i, ItemStack itemstack)
    {
        if (itemstack != null)
        {
            switch (i)
            {
                case 0:
                    return itemstack.getItem() instanceof IItemElectric;
                case 1:
                    return itemstack.getItem() instanceof IAssembledTool;
                case 2:
                    return OreDictionary.getOres("cellDeuterium").contains(
                            itemstack)
                            || OreDictionary.getOres("cellTritium").contains(
                                    itemstack);
            }
        }
        return false;
    }
    
    @Override
    public double getMaxJoules()
    {
        return MAX_JOULES;
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return direction.ordinal() == this.getBlockMetadata();
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int packetType,
            Packet250CustomPayload packet, EntityPlayer player,
            ByteArrayDataInput dataStream)
    {
        if (this.worldObj.isRemote)
        {
            // client
            try
            {
                this.joules = dataStream.readDouble();
                this.plasma = dataStream.readInt();
                this.deuterium = dataStream.readInt();
                this.tritium = dataStream.readInt();
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
    
    @Override
    public double getJoules()
    {
        return this.joules;
    }
    
    @Override
    public void setJoules(double joules)
    {
        this.joules = joules;
    }
}
