package simpletools.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import simpletools.api.IPlasmaStorage;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import calclavia.lib.prefab.tile.TileElectrical;
import cpw.mods.fml.common.network.PacketDispatcher;

@UniversalClass
public class TileEntityTablePlasma extends TileElectrical
implements IInventory, IEnergyInterface, IEnergyContainer
{
    /**
     *      0: Battery to power the plasma machine
     * </br>1: Tool 
     * </br>2: Deuterium, Tritium, or Water
     * </br>3: Empty Container Items
     */

    private ItemStack[] inventory = new ItemStack[INVENTORY_SIZE];
    private int plasma = 0;
    private int rawFuel = 0;

    public static final long MAX_ENERGY = 1000000000;
    /** In Milli-Buckets */
    public static final int MAX_RAW_FUEL = 10000;
    /** In Milli-Buckets */
    public static final int MAX_PLASMA = 5000;

    public static final int RAW_FUEL_PER_WATER = 100;
    public static final int RAW_FUEL_PER_DEUTERIUM = 500;
    public static final int RAW_FUEL_PER_TRITIUM = 1000;
    /** In Milli-Buckets */
    public static final int RAW_FUEL_PER_OPERATION = 1000;
    public static final int ENERGY_PER_OPERATION = 100000000;
    public static final int PLASMA_PER_OPERATION = 100;
    public static final int PLASMA_PER_FUELING = 10;
    public static final int INVENTORY_SIZE = 4;

    public TileEntityTablePlasma()
    {
        this.energy = new EnergyStorageHandler(MAX_ENERGY);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        this.updateStorage();

        this.discharge(this.inventory[0]);

        if (this.getEnergy(null) >= ENERGY_PER_OPERATION && this.rawFuel >= RAW_FUEL_PER_OPERATION && this.plasma + PLASMA_PER_OPERATION <= MAX_PLASMA)
        {
            this.energy.extractEnergy(ENERGY_PER_OPERATION, true);
            this.rawFuel -= RAW_FUEL_PER_OPERATION;
            this.plasma += PLASMA_PER_OPERATION;
        }

        if (this.inventory[1] != null)
        {
            if (this.inventory[1].getItem() instanceof IPlasmaStorage && this.plasma >= PLASMA_PER_FUELING &&((IPlasmaStorage) this.inventory[1].getItem()).addPlasma(this.inventory[1], PLASMA_PER_FUELING))
            {
                this.plasma -= PLASMA_PER_FUELING;
            }
            
            this.recharge(this.inventory[1]);
        }
        
        if(this.ticks % 3 == 0)
            this.sendPacket();
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
                    return CompatibilityModule.isHandler(itemstack);
                case 1:
                    return itemstack.getItem() instanceof IPlasmaStorage;
                case 2:
                    if (FluidContainerRegistry.getFluidForFilledItem(itemstack) != null)
                    {
                        return isItemStackFluid(itemstack, "deuterium") || isItemStackFluid(itemstack, "tritium") || isItemStackFluid(itemstack, "water");
                    }
                case 3: return true;
            }
        }
        return false;
    }

    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return true;
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) 
    {
        if (this.worldObj.isRemote)
        {
            // client
            try
            {
                this.setEnergy(null, pkt.data.getLong("energy"));
                this.plasma = pkt.data.getInteger("plasma");
                this.rawFuel = pkt.data.getInteger("fuel");
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
    public Packet getDescriptionPacket() 
    {
        if (!this.worldObj.isRemote) 
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setLong("energy", this.getEnergy(null));
            tag.setInteger("plasma", this.plasma);
            tag.setInteger("fuel", this.rawFuel);
            
            return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tag);
        }
        return null;
    }
    
    public void sendPacket()
    {
        Packet packet = this.getDescriptionPacket();
        
        if (packet != null)
            PacketDispatcher.sendPacketToAllInDimension(packet, this.worldObj.provider.dimensionId);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("plasma", this.plasma);
        tag.setInteger("fuel", this.rawFuel);

        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.inventory.length; var3++)
        {
            if (this.inventory[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.inventory[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        tag.setTag("Items", var2);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.plasma = tag.getInteger("plasma");
        this.rawFuel = tag.getInteger("fuel");
        NBTTagList var2 = tag.getTagList("Items");
        this.inventory = new ItemStack[INVENTORY_SIZE];

        for (int i = 0; i < var2.tagCount(); i++)
        {
            NBTTagCompound tag2 = (NBTTagCompound) var2.tagAt(i);
            byte var5 = tag2.getByte("Slot");

            if (var5 >= 0 && var5 < this.inventory.length)
            {
                this.inventory[var5] = ItemStack.loadItemStackFromNBT(tag2);
            }
        }
    }

    public float getPlasma()
    {
        return this.plasma;
    }
    public float getFuel()
    {
        return this.rawFuel;
    }
    public float getMaxPlasma()
    {
        return MAX_PLASMA;
    }
    public float getMaxFuel()
    {
        return MAX_RAW_FUEL;
    }

    public static boolean isItemStackFluid(ItemStack stack, String fluid)
    {
        if (fluid == null || stack == null || FluidContainerRegistry.getFluidForFilledItem(stack) == null)
            return false;
        return fluid.equals(FluidContainerRegistry.getFluidForFilledItem(stack).getFluid().getName().toLowerCase());
    }

    public void updateStorage()
    {
        FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(this.inventory[2]);

        if (fluid == null || fluid.getFluid() == null)
            return;

        if (fluid.getFluid().getName().toLowerCase().equals("deuterium") && this.addItem(3, this.inventory[2].getItem().getContainerItemStack(this.inventory[2]), true))
        {
            if (this.rawFuel + fluid.amount / FluidContainerRegistry.BUCKET_VOLUME * RAW_FUEL_PER_DEUTERIUM <= MAX_RAW_FUEL)
            {
                this.rawFuel += fluid.amount / FluidContainerRegistry.BUCKET_VOLUME * RAW_FUEL_PER_DEUTERIUM;
                this.addItem(3, this.inventory[2].getItem().getContainerItemStack(this.inventory[2]), false);
                this.inventory[2].stackSize--;
            }
        }
        else if (fluid.getFluid().getName().toLowerCase().equals("tritium") && this.addItem(3, this.inventory[2].getItem().getContainerItemStack(this.inventory[2]), true))
        {
            if (this.rawFuel + fluid.amount / FluidContainerRegistry.BUCKET_VOLUME * RAW_FUEL_PER_TRITIUM <= MAX_RAW_FUEL)
            {
                this.rawFuel += fluid.amount / FluidContainerRegistry.BUCKET_VOLUME * RAW_FUEL_PER_TRITIUM;
                this.addItem(3, this.inventory[2].getItem().getContainerItemStack(this.inventory[2]), false);
                this.inventory[2].stackSize--;
            }
        }
        else if (fluid.getFluid().getName().toLowerCase().equals("water") && this.addItem(3, this.inventory[2].getItem().getContainerItemStack(this.inventory[2]), true))
        {
            if (this.rawFuel + fluid.amount / FluidContainerRegistry.BUCKET_VOLUME * RAW_FUEL_PER_WATER <= MAX_RAW_FUEL)
            {
                this.rawFuel += fluid.amount / FluidContainerRegistry.BUCKET_VOLUME * RAW_FUEL_PER_WATER;
                this.addItem(3, this.inventory[2].getItem().getContainerItemStack(this.inventory[2]), false);
                this.inventory[2].stackSize--;
            }
        }

        if (this.inventory[2].stackSize <= 0)
            this.inventory[2] = null;
    }

    private boolean addItem(int slot, ItemStack is, boolean simulate)
    {
        boolean canDo = false;
        if (slot >= INVENTORY_SIZE)
            return false;
        if (is == null)
            return true;
        if (this.inventory[slot] == null)
            canDo = true;
        else if (this.inventory[slot].isItemEqual(is) && ItemStack.areItemStackTagsEqual(this.inventory[slot], is) 
        && this.inventory[slot].stackSize + is.stackSize <= Math.min(this.inventory[slot].getMaxStackSize(), is.getMaxStackSize()))
            canDo = true;

        if (!simulate && canDo)
        {
            if (this.inventory[slot] == null)
            {
                this.inventory[slot] = is;
            }
            else
            {
                this.inventory[slot].stackSize += is.stackSize;
            }
        }

        return canDo;
    }
}
