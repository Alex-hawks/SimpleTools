package simpletools.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import simpletools.api.IAssembledTool;
import simpletools.api.IAttachment;
import simpletools.api.ICore;
import simpletools.api.SimpleToolsItems;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.item.IEnergyItem;
import calclavia.lib.network.IPacketReceiver;
import calclavia.lib.prefab.tile.IRedstoneProvider;
import calclavia.lib.prefab.tile.TileAdvanced;

import com.google.common.io.ByteArrayDataInput;

@SuppressWarnings("unused")
public class TileEntityTableAssembly extends TileAdvanced implements IRedstoneProvider, IPacketReceiver, ISidedInventory
{
    private ItemStack[] inventory = new ItemStack[5];
    private int playersUsing = 0;
    private boolean assembling = false;
    
    @Override
    public int getSizeInventory()
    {
        return 5;
    }
    
    @Override
    public ItemStack getStackInSlot(int i)
    {
        if (i < this.getSizeInventory())
            return this.inventory[i];
        else
            return null;
    }
    
    @Override
    public ItemStack decrStackSize(int var1, int var2)
    {
        NBTTagCompound stackTag;
        ItemStack returnStack = null;
        
        if (this.inventory[var1] != null && this.inventory[var1].stackSize > var2)
        {
            stackTag = this.inventory[var1].getTagCompound();
            this.inventory[var1].stackSize -= var2;
            returnStack = new ItemStack(this.inventory[var1].itemID, var2, this.inventory[var1].getItemDamage());
            if (!returnStack.hasTagCompound())
            {
                returnStack.setTagCompound(stackTag);
            }
        }
        else if (this.inventory[var1] != null && this.inventory[var1].stackSize == var2)
        {
            stackTag = this.inventory[var1].getTagCompound();
            returnStack = new ItemStack(this.inventory[var1].itemID, var2, this.inventory[var1].getItemDamage());
            this.inventory[var1] = null;
            if (!returnStack.hasTagCompound())
            {
                returnStack.setTagCompound(stackTag);
            }
        }
        else
        {
            returnStack = this.inventory[var1].copy();
            this.inventory[var1] = null;
        }
        return returnStack;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        return this.inventory[var1];
    }
    
    @Override
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.inventory[var1] = var2;
    }
    
    @Override
    public String getInvName()
    {
        return StatCollector.translateToLocal(SimpleToolsItems.blockTableAssembly.getUnlocalizedName() + ".name");
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }
    
    @Override
    public void openChest()
    {
        this.playersUsing++;
    }
    
    @Override
    public void closeChest()
    {
        this.playersUsing--;
    }
    
    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    @Override
    public void updateEntity()
    {
        if (this.inventory[3] == null && this.canAssemble() && !this.assembling)
        {
            this.startAssemble();
            this.assembling = true;
        }
        else if (this.inventory[3] == null && this.canAssemble() && this.assembling)
        {
            this.finishAssemble();
            this.assembling = false;
        }
        else if ((this.inventory[0] == null || this.inventory[1] == null || this.inventory[2] == null)
                && this.inventory[3] != null)
        {
            this.cancelAssemble();
            this.assembling = false;
        }
        
        if (this.inventory[4] != null && this.inventory[4].getItem() instanceof IAssembledTool
                && this.canDissassemble())
        {
            this.dissassemble();
        }
        
        if (this.inventory[3] != null && this.inventory[3].getItem() instanceof IAssembledTool && this.assembling)
        {
            IAssembledTool tool = (IAssembledTool) this.inventory[3].getItem();
            ItemStack toolIS = this.inventory[3];
            
            if (this.inventory[0] != tool.getAttachment(toolIS))
            {
                this.inventory[3] = null;
                this.startAssemble();
            }
            if (this.inventory[1] != tool.getCore(toolIS))
            {
                this.inventory[3] = null;
                this.startAssemble();
            }
            if (this.inventory[2] != tool.getStorage(toolIS))
            {
                this.inventory[3] = null;
                this.startAssemble();
            }
        }
    }
    
    @Override
    public boolean isPoweringTo(ForgeDirection side)
    {
        return this.canAssemble();
    }
    
    @Override
    public boolean isIndirectlyPoweringTo(ForgeDirection side)
    {
        return this.canAssemble();
    }
    
    public boolean canAssemble()
    {
        ItemStack slot0 = this.inventory[0];
        ItemStack slot1 = this.inventory[1];
        ItemStack slot2 = this.inventory[2];
        
        if (slot0 != null && slot1 != null && slot2 != null)
        {
            if (slot0.getItem() instanceof IAttachment && slot1.getItem() instanceof ICore)
            {
                IAttachment attachTemp = (IAttachment) slot0.getItem();
                ICore coreTemp = (ICore) slot1.getItem();
                
                if (attachTemp.getToolAttachmentType(slot0) == coreTemp.getCoreType(slot1)
                        && attachTemp.getMinimumTier(slot0) <= coreTemp.getCoreTier(slot1))
                {
                    if (coreTemp.getCoreFinerType(slot1).equals("electric") && CompatibilityModule.isHandler(slot2.getItem()))
                        return true;
                    else if (coreTemp.getCoreFinerType(slot1).equals("plasma") && CompatibilityModule.isHandler(slot2.getItem()))
                        return true;
                    
                }
            }
            return false;
        }
        return false;
    }
    
    public boolean canDissassemble()
    {
        boolean toReturn = true;
        for (int i = 0; i < this.inventory.length - 1; i++)
        {
            if (this.inventory[i] != null)
            {
                toReturn = false;
            }
        }
        return toReturn && this.inventory[4] != null && this.inventory[4].getItem() instanceof IAssembledTool;
    }
    
    public void startAssemble()
    {
        if (!this.worldObj.isRemote && this.canAssemble())
        {
            IAssembledTool result = (IAssembledTool) ((ICore) this.inventory[1].getItem())
                    .getAssmebledToolItem(this.inventory[1]);
            this.inventory[3] = result.onAssemble(this.inventory[0], this.inventory[1], this.inventory[2]);
        }
    }
    
    public void finishAssemble()
    {
        if (!this.worldObj.isRemote)
        {
            this.inventory[0] = null;
            this.inventory[1] = null;
            this.inventory[2] = null;
        }
    }
    
    public void cancelAssemble()
    {
        if (!this.worldObj.isRemote)
        {
            this.inventory[3] = null;
        }
    }
    
    public void dissassemble()
    {
        if (this.canDissassemble())
        {
            ItemStack oldTool = this.inventory[4];
            if (oldTool.getItem() instanceof IAssembledTool && oldTool.hasTagCompound())
            {
                this.inventory[0] = ((IAssembledTool) oldTool.getItem()).getAttachment(oldTool);
                this.inventory[1] = ((IAssembledTool) oldTool.getItem()).getCore(oldTool);
                this.inventory[2] = ((IAssembledTool) oldTool.getItem()).getStorage(oldTool);
                this.inventory[4] = null;
            }
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbtList = new NBTTagList();
        
        for (int i = 0; i < this.inventory.length; i++)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbt);
                nbtList.appendTag(nbt);
            }
        }
        par1NBTTagCompound.setTag("Items", nbtList);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.inventory = new ItemStack[this.getSizeInventory()];
        
        for (int var3 = 0; var3 < var2.tagCount(); var3++)
        {
            NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");
            
            if (var5 >= 0 && var5 < this.inventory.length)
            {
                this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        if (i < this.inventory.length && itemstack != null)
        {
            switch (i)
            {
                case 0:
                    return itemstack.getItem() instanceof IAttachment;
                case 1:
                    return itemstack.getItem() instanceof ICore;
                case 2:
                    return itemstack.getItem() instanceof IEnergyItem; // TODO add support for fuel
                case 3:
                    return false;
                case 4:
                    return itemstack.getItem() instanceof IAssembledTool;
            }
        }
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        switch (var1)
        {
            case 0:
                return new int[] {0, 4};
            case 1:
                return new int[] {2, 4};
            case 2:
            case 3:
            case 4:
            case 5:
                return new int[] {1, 4};
            default:
                return new int[] {4};
        }
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j)
    {
        if (j == 3)
            return false;
        for (int k : getAccessibleSlotsFromSide(i))
        {
            if (k == j && isItemValidForSlot(j, itemstack))
                return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        return j == 3;
    }

    @Override
    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        
    }
}
