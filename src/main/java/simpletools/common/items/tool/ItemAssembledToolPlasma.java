package simpletools.common.items.tool;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import simpletools.api.IAssembledElectricTool;
import simpletools.api.IAssembledTool;
import simpletools.api.IAttachment;
import simpletools.api.ICore;
import simpletools.api.IPlasmaStorage;
import simpletools.api.SimpleToolsItems;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@UniversalClass
public class ItemAssembledToolPlasma extends Item implements IAssembledElectricTool, IPlasmaStorage
{
    private static final long JOULES_PER_USE = 50000;
    private static final int PLASMA_PER_USE = 100;

    public ItemAssembledToolPlasma(int par1, String name)
    {
        super(par1);
        this.setUnlocalizedName(name);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setMaxDamage(0);
        this.setCreativeTab(null);
    }

    @Override
    public ItemStack onAssemble(ItemStack attachment, ItemStack core, ItemStack storage)
    {
        if (!CompatibilityModule.isHandler(storage.getItem()))
            return null;

        ItemStack returnStack = null;

        if (attachment.getItem() instanceof IAttachment && core.getItem() instanceof ICore)
        {
            IAttachment attachmentTemp = (IAttachment) attachment.getItem();
            ICore coreTemp = (ICore) core.getItem();

            if (coreTemp.getCoreType(core) == attachmentTemp.getToolAttachmentType(attachment))
            {
                if (coreTemp.getCoreTier(core) >= attachmentTemp.getMinimumTier(attachment))
                {
                    int coreMeta = coreTemp.getCoreTier(core) * 1000;
                    int attachMeta = attachmentTemp.getAttachmentUID(attachment);
                    int meta = coreMeta + attachMeta;
                    returnStack = new ItemStack(this, 1, meta);

                    NBTTagCompound compound = new NBTTagCompound();
                    compound.setCompoundTag("SimpleTools", new NBTTagCompound());
                    compound.setCompoundTag("damageVsEntity", new NBTTagCompound());
                    compound.setBoolean("useDamageVsEntityTag", true);

                    compound.getCompoundTag("SimpleTools").setInteger("plasma", 0);
                    compound.getCompoundTag("SimpleTools").setInteger("maxPlasma", (coreTemp.getCoreTier(core) + 2) * 5000);
                    compound.getCompoundTag("SimpleTools").setCompoundTag("attachment", attachment.writeToNBT(new NBTTagCompound()));
                    compound.getCompoundTag("SimpleTools").setCompoundTag("battery", storage.writeToNBT(new NBTTagCompound()));

                    compound.getCompoundTag("damageVsEntity").setInteger("", attachmentTemp.getDamageVsEntities(attachment).get(null));

                    returnStack.setTagCompound(compound);
                }
            }
        }
        return returnStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack itemStack, EntityPlayer player, List currentTips, boolean advancedToolTips)
    {
        ItemStack attachment = null, battery = null;
        try
        {
            NBTTagCompound batt = itemStack.getTagCompound().getCompoundTag("SimpleTools").getCompoundTag("battery");
            battery = ItemStack.loadItemStackFromNBT(batt);

            NBTBase attach = itemStack.getTagCompound().getCompoundTag("SimpleTools").getCompoundTag("attachment");
            attachment = ItemStack.loadItemStackFromNBT((NBTTagCompound) attach);
        }
        catch (Exception e) { }

        currentTips.add("Energy: " + UnitDisplay.getDisplayShort(this.getEnergy(itemStack), Unit.JOULES) + " / "
        + UnitDisplay.getDisplayShort(this.getEnergyCapacity(itemStack), Unit.JOULES));
        currentTips.add("Plasma: " + this.getPlasma(itemStack) + " / " + this.getMaxPlasma(itemStack));

        if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.keyCode))
        {
            if (this.getCore(itemStack) != null)
            {
                currentTips.add("Core Module: " + this.getCore(itemStack).getDisplayName());
            }
            if (attachment != null)
            {
                currentTips.add("Tool Module: " + attachment.getDisplayName());
            }
            if (battery != null)
            {
                currentTips.add("Batt Module: " + battery.getDisplayName());
            }
        }
        else
        {
            currentTips.add("Hold Sneak for more information");
        }
    }

    @Override
    public ItemStack getCore(ItemStack assembledTool)
    {
        int tier = assembledTool.getItemDamage() / 1000;
        return new ItemStack(SimpleToolsItems.itemCorePlasma, 1, tier);
    }

    @Override
    public ItemStack getStorage(ItemStack assembledTool)
    {
        ItemStack battery = null;
        try
        {
            NBTTagCompound batt = assembledTool.getTagCompound().getCompoundTag("SimpleTools").getCompoundTag("battery");
            battery = ItemStack.loadItemStackFromNBT(batt);
        }
        catch (Throwable e)
        {
        }
        return battery;
    }

    @Override
    public ItemStack getAttachment(ItemStack assembledTool)
    {
        ItemStack attachment = null;
        try
        {
            NBTBase attach = assembledTool.getTagCompound().getCompoundTag("SimpleTools").getCompoundTag("attachment");
            attachment = ItemStack.loadItemStackFromNBT((NBTTagCompound) attach);
            if (!assembledTool.getEnchantmentTagList().equals(null))
            {
                attachment.setTagCompound(new NBTTagCompound());
                attachment.getTagCompound().setTag(assembledTool.getEnchantmentTagList().getName(), assembledTool.getEnchantmentTagList());
            }
        }
        catch (Throwable e)
        {
        }
        return attachment;
    }

    @Override
    public boolean canDoWork(ItemStack assembledTool)
    {
        return this.getEnergy(assembledTool) >= JOULES_PER_USE && this.getPlasma(assembledTool) >= PLASMA_PER_USE;
    }

    @Override
    public boolean canBreakBlock(ItemStack assembledTool, World world, int blockID, int metadata, EntityLivingBase entity)
    {
        Block block = Block.blocksList[blockID];

        int pick = MinecraftForge.getBlockHarvestLevel(block, metadata, "pickaxe");
        int axe = MinecraftForge.getBlockHarvestLevel(block, metadata, "axe");
        int shovel = MinecraftForge.getBlockHarvestLevel(block, metadata, "shovel");
        int all = Math.max(pick, Math.max(axe, shovel));

        int tool = ((IAttachment) this.getAttachment(assembledTool).getItem()).getAttachmentTier(this.getAttachment(assembledTool));

        return tool >= all;
    }

    @Override
    public boolean isEffectiveOnBlock(ItemStack assembledTool, World world, int blockID, int metadata, EntityLivingBase entity)
    {
        return this.canBreakBlock(assembledTool, world, blockID, metadata, entity);
    }

    @Override
    public float[] getAdditionalDisplayData(ItemStack assembledTool)
    {
        float[] toReturn = new float[2];

        float currEnergy = this.getEnergy(assembledTool);
        float maxEnergy = this.getEnergyCapacity(assembledTool);
        float currPlasma = this.getPlasma(assembledTool);
        float maxPlasma = this.getMaxPlasma(assembledTool);

        toReturn[0] = currEnergy / maxEnergy;
        toReturn[1] = currPlasma / maxPlasma;

        return toReturn;
    }

    @Override
    public long getEnergy(ItemStack theItem)
    {
        return CompatibilityModule.getEnergyItem(this.getStorage(theItem));
    }

    @Override
    public void setEnergy(ItemStack itemStack, long energy)
    {
        //itemStack.stackTagCompound.getCompoundTag("SimpleTools").setLong("electricity", energy);
        // TODO Find a workaround for TE and IC2 Items
    }

    public long getEnergyCapacity(ItemStack theItem)
    {
        return CompatibilityModule.getMaxEnergyItem(this.getStorage(theItem));
    }

    @Override
    public long recharge(ItemStack itemStack, long energy, boolean doRecharge)
    {
        ItemStack is = this.getStorage(itemStack);
        long toReturn = CompatibilityModule.chargeItem(is, energy, doRecharge);
        itemStack.stackTagCompound.getCompoundTag("SimpleTools").setCompoundTag("battery", is.writeToNBT(new NBTTagCompound()));
        return toReturn;
    }

    @Override
    public long discharge(ItemStack itemStack, long energy, boolean doDischarge)
    {
        ItemStack is = this.getStorage(itemStack);
        long toReturn = CompatibilityModule.dischargeItem(is, energy, doDischarge);
        itemStack.stackTagCompound.getCompoundTag("SimpleTools").setCompoundTag("battery", is.writeToNBT(new NBTTagCompound()));
        return toReturn;
    }

    @Override
    public int getPlasma(ItemStack assembledTool)
    {
        try
        {
            return assembledTool.stackTagCompound.getCompoundTag("SimpleTools").getInteger("plasma");
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    @Override
    public int getMaxPlasma(ItemStack assembledTool)
    {
        try
        {
            return assembledTool.stackTagCompound.getCompoundTag("SimpleTools").getInteger("maxPlasma");
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    @Override
    public void setPlasma(ItemStack stack, int i)
    {
        try
        {
            stack.stackTagCompound.getCompoundTag("SimpleTools").setInteger("plasma", Math.min(this.getMaxPlasma(stack), Math.max(i,  0)));
        }
        catch (Exception e)
        {

        }
    }

    public boolean onItemUse(ItemStack i)
    {
        if (this.canDoWork(i))
        {
            this.discharge(i, JOULES_PER_USE, true);
            this.setPlasma(i, this.getPlasma(i) - PLASMA_PER_USE);
            return true;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(ItemStack stack, int pass)
    {
        // pass: 0 is core, 1 is attachment
        if (pass == 0)
        {
            try
            {
                int coreMeta = ((IAssembledTool) stack.getItem()).getCore(stack).getItemDamage();
                return ((IAssembledTool) stack.getItem()).getCore(stack).getItem().getIconFromDamage(coreMeta);
            }
            catch (Throwable e)
            {
            }
        }
        else if (pass == 1)
        {
            try
            {
                int attachMeta = ((IAssembledTool) stack.getItem()).getAttachment(stack).getItemDamage();
                return ((IAssembledTool) stack.getItem()).getAttachment(stack).getItem().getIconFromDamage(attachMeta);
            }
            catch (Throwable e)
            {
            }
        }
        return null;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity)
    {
        IAttachment attach = (IAttachment) this.getAttachment(itemstack).getItem();
        if (attach.canRightClick(this.getAttachment(itemstack), entity, null) && this.canDoWork(itemstack))
        {
            attach.onRightClick(this.getAttachment(itemstack), entity, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean addPlasma(ItemStack stack, int i)
    {
        if (this.getPlasma(stack) + i <= this.getMaxPlasma(stack))
        {
            this.setPlasma(stack, this.getPlasma(stack) + i);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float HitY, float hitZ)
    {
        IAttachment attach = (IAttachment) this.getAttachment(itemStack).getItem();
        boolean canUse = attach.canRightClick(this.getAttachment(itemStack), new Object[] { world, x, y, z, side, hitX, HitY, hitZ }, player) && this.canDoWork(itemStack);

        if (canUse && this.canDoWork(itemStack))
        {
            byte plasma = attach.onRightClick(this.getAttachment(itemStack), new Object[] { world, x, y, z, side, hitX, HitY, hitZ }, player);
            this.discharge(itemStack, JOULES_PER_USE, true);
            this.setPlasma(itemStack, this.getPlasma(itemStack) + (plasma * 200));
        }
        return canUse;
    }

    @Override
    public float getStrVsBlock(ItemStack assembledTool, Block block, int meta)
    {
        int pick = MinecraftForge.getBlockHarvestLevel(block, meta, "pickaxe");
        int axe = MinecraftForge.getBlockHarvestLevel(block, meta, "axe");
        int shovel = MinecraftForge.getBlockHarvestLevel(block, meta, "shovel");
        int all = Math.max(pick, Math.max(axe, shovel));

        int tool = ((IAttachment) this.getAttachment(assembledTool).getItem()).getAttachmentTier(this.getAttachment(assembledTool));

        return tool >= all ? ((IAttachment) this.getAttachment(assembledTool).getItem()).getHarvestSpeed(this.getAttachment(assembledTool)) : 1.0f;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int id, int x, int y, int z, EntityLivingBase par7EntityLiving)
    {
        Block block = Block.blocksList[id];
        int metadata = par2World.getBlockMetadata(x, y, z);
        double blockHardness = block.getBlockHardness(par2World, x, y, z);
        if (((IAttachment) this.getAttachment(par1ItemStack).getItem()).getToolType(this.getAttachment(par1ItemStack)).equals("shears"))
        {
            if (block.equals(Block.leaves) || block.equals(Block.web) || block.equals(Block.tallGrass) || block.equals(Block.deadBush) || block.equals(Block.vine) || block.equals(Block.tripWire)
            || block instanceof IShearable)
                return true;
        }
        if (blockHardness != 0D && this.canBreakBlock(par1ItemStack, par2World, id, metadata, par7EntityLiving)
        && !block.canHarvestBlock((EntityPlayer) par7EntityLiving, par2World.getBlockMetadata(x, y, z)))
        {
            if (this.onItemUse(par1ItemStack))
            {
                if (this.getEnchantmentLvl(Enchantment.silkTouch, par1ItemStack) > 0)
                {
                    this.dropBlockAsItem_do(par2World, x, y, z, new ItemStack(block, 1, par2World.getBlockMetadata(x, y, z)));
                }
                else
                {
                    int fortune = this.getEnchantmentLvl(Enchantment.fortune, par1ItemStack);
                    block.dropBlockAsItem(par2World, x, y, z, par2World.getBlockMetadata(x, y, z), fortune);
                }
                return true;
            }
            else
                return false;
        }
        else if (blockHardness != 0D && this.canBreakBlock(par1ItemStack, par2World, id, metadata, par7EntityLiving))
        {
            if (this.onItemUse(par1ItemStack))
            {
                if (block.blockMaterial.isToolNotRequired())
                    return true;
            }
        }
        return false;
    }

    public int getEnchantmentLvl(Enchantment enchant, ItemStack assembledTool)
    {
        int level = -1;
        if (assembledTool.getEnchantmentTagList() != null)
        {
            for (int i = 0; i < assembledTool.getEnchantmentTagList().tagCount(); i++)
            {
                if (assembledTool.getEnchantmentTagList().tagAt(i) instanceof NBTTagCompound)
                {
                    if (((NBTTagCompound) assembledTool.getEnchantmentTagList().tagAt(i)).getShort("id") == enchant.effectId)
                    {
                        level = ((NBTTagCompound) assembledTool.getEnchantmentTagList().tagAt(i)).getShort("lvl");
                    }
                }
            }
        }
        return level;
    }

    protected void dropBlockAsItem_do(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack)
    {
        if (!par1World.isRemote && par1World.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            float var6 = 0.7F;
            double var7 = par1World.rand.nextFloat() * var6 + (1.0F - var6) * 0.5D;
            double var9 = par1World.rand.nextFloat() * var6 + (1.0F - var6) * 0.5D;
            double var11 = par1World.rand.nextFloat() * var6 + (1.0F - var6) * 0.5D;
            EntityItem var13 = new EntityItem(par1World, par2 + var7, par3 + var9, par4 + var11, par5ItemStack);
            var13.delayBeforeCanPickup = 10;
            par1World.spawnEntityInWorld(var13);
        }
    }
}
