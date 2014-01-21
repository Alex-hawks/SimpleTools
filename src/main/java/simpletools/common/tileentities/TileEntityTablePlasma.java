package simpletools.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import simpletools.api.IPlasmaStorage;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import calclavia.lib.network.IPacketReceiver;
import calclavia.lib.prefab.tile.TileElectrical;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityTablePlasma extends TileElectrical
implements IPacketReceiver, IInventory, IEnergyInterface, IEnergyContainer
{
    /**
     *      0: Battery to power the plasma machine
     * </br>1: Tool 
     * </br>2: Deuterium, Tritium, or Water
     * </br>3: Empty Container Items
     */

    private ItemStack[] inventory = new ItemStack[INVENTORY_SIZE];
    private long joules = 0;
    private int plasma = 0;
    private int rawFuel = 0;

    public static final long MAX_ENERGY = 500000;
    /** In Milli-Buckets */
    public static final int MAX_RAW_FUEL = 10_000;
    /** In Milli-Buckets */
    public static final int MAX_PLASMA = 50000;

    public static final int RAW_FUEL_PER_WATER = 500;
    public static final int RAW_FUEL_PER_DEUTERIUM = 1500;
    public static final int RAW_FUEL_PER_TRITIUM = 3000;
    /** In Milli-Buckets */
    public static final int INPUT_PER_OPERATION = 1000;
    public static final int JOULES_PER_OPERATION = 5000;
    public static final int PLASMA_PER_OPERATION = 1000;
    public static final int PLASMA_PER_FUELING = 200;
    public static final int INVENTORY_SIZE = 4;

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        this.updateStorage();
        
        if(this.inventory[0] != null && this.inventory[0].getItem() != null && CompatibilityModule.isEnergyContainer(this.inventory[0]))
        {
            this.energy.modifyEnergyStored(CompatibilityModule.dischargeItem(this.inventory[0], Math.min(this.getEnergyCapacity(null) / 100, this.getEnergyCapacity(null) - this.getEnergy(null)), true));
        }
        
        if (this.joules >= JOULES_PER_OPERATION && this.rawFuel >= INPUT_PER_OPERATION && this.plasma + PLASMA_PER_OPERATION <= MAX_PLASMA)
        {
            this.joules -= JOULES_PER_OPERATION;
            this.rawFuel -= JOULES_PER_OPERATION;
            this.plasma += PLASMA_PER_OPERATION;
        }

        if (this.inventory[1] != null && this.inventory[1].getItem() instanceof IPlasmaStorage)
        {
            if (((IPlasmaStorage) this.inventory[1].getItem()).addPlasma(this.inventory[1], PLASMA_PER_FUELING))
            {
                this.plasma -= PLASMA_PER_FUELING;
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
    public long getEnergyCapacity(ForgeDirection from)
    {
        return MAX_ENERGY;
    }

    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return true;
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
