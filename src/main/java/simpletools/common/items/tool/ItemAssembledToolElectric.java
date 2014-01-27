package simpletools.common.items.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import simpletools.api.IAssembledElectricTool;
import simpletools.api.IAssembledTool;
import simpletools.api.IAttachment;
import simpletools.api.ICore;
import simpletools.api.SimpleToolsItems;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@UniversalClass
public class ItemAssembledToolElectric extends ItemTool implements IAssembledElectricTool
{
    public static final long BASE_ENERGY_PER_USE = 2000000; // in Joules
    public static final long MIN_ENERGY_PER_USE = (BASE_ENERGY_PER_USE / 40) * 39;

    public ItemAssembledToolElectric(int itemID, String name)
    {
        super(itemID, 0, EnumToolMaterial.EMERALD, new Block[0]);
        this.setUnlocalizedName(name);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setMaxDamage(0);
        this.setCreativeTab(null);
    }

    @Override
    public long getEnergyCapacity(ItemStack theItem)
    {
        return CompatibilityModule.getMaxEnergyItem(this.getStorage(theItem));
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

    /**
     * @param attachment
     *            The attachment, passed in as an ItemStack
     * @param core
     *            The core, passed in as an ItemStack
     * @param storage
     *            The battery, fuelTank, etc passed in as an ItemStack
     * @return the resultant Item, as an ItemStack. null if it failed.
     */
    @Override
    public ItemStack onAssemble(ItemStack attachment, ItemStack core, ItemStack battery)
    {
        if (!CompatibilityModule.isHandler(battery.getItem()))
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

                    returnStack.setTagCompound(compound);

                    compound.getCompoundTag("SimpleTools").setCompoundTag("attachment", attachment.writeToNBT(new NBTTagCompound()));
                    compound.getCompoundTag("SimpleTools").setCompoundTag("battery", battery.writeToNBT(new NBTTagCompound()));

                    compound.getCompoundTag("damageVsEntity").setInteger("", attachmentTemp.getDamageVsEntities(attachment).get(null));

                }
            }
        }
        return returnStack;
    }

    @Override
    public float getStrVsBlock(ItemStack itemStack, Block block, int meta)
    {
        ItemStack attachment = this.getAttachment(itemStack);
        String toolClass = ((IAttachment) attachment.getItem()).getToolType(attachment);
        int toolLevel = ((IAttachment) attachment.getItem()).getAttachmentTier(attachment);
        int blockLevel = MinecraftForge.getBlockHarvestLevel(block, meta, toolClass);

        if (toolLevel >= blockLevel)
            return (float) ((IAttachment) attachment.getItem()).getHarvestSpeed(attachment);
        else
            return 1f;

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
        catch (Exception e)
        {
        }

        currentTips.add("Energy: " + UnitDisplay.getDisplayShort(this.getEnergy(itemStack), Unit.JOULES) + " / "
        + UnitDisplay.getDisplayShort(this.getEnergyCapacity(itemStack), Unit.JOULES));

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

    public boolean onItemUse(ItemStack i)
    {
        if (this.canDoWork(i))
        {
            long res = this.discharge(i, BASE_ENERGY_PER_USE, true);
            return res >= MIN_ENERGY_PER_USE;
        }
        return false;
    }

    @Override
    public boolean canDoWork(ItemStack i)
    {
        return this.getEnergy(i) >= BASE_ENERGY_PER_USE;
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
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int id, int x, int y, int z, EntityLivingBase par7EntityLiving)
    {
        Block block = Block.blocksList[id];
        int metadata = par2World.getBlockMetadata(x, y, z);
        double blockHardness = block.getBlockHardness(par2World, x, y, z);
        if (((IAttachment) this.getAttachment(par1ItemStack).getItem()).getToolType(this.getAttachment(par1ItemStack)).equals("shears"))
        {
            if (block.equals(Block.leaves) || block.equals(Block.web) || block.equals(Block.tallGrass) || block.equals(Block.deadBush) || block.equals(Block.vine) || block.equals(Block.tripWire)
            || block instanceof IShearable && this.onItemUse(par1ItemStack))
                return true;
        }
        if (blockHardness != 0D && this.canBreakBlock(par1ItemStack, par2World, id, metadata, par7EntityLiving)
        && !block.canHarvestBlock((EntityPlayer) par7EntityLiving, par2World.getBlockMetadata(x, y, z)))
        {
            if (this.onItemUse(par1ItemStack))
            {
                if (this.getEnchantmentLvl(Enchantment.silkTouch, par1ItemStack) > 0 && block.canSilkHarvest(par2World, (EntityPlayer) par7EntityLiving, x, y, z, metadata))
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

    @Override
    public ItemStack getCore(ItemStack assembledTool)
    {
        int tier = assembledTool.getItemDamage() / 1000;
        return new ItemStack(SimpleToolsItems.itemCoreMechElectric, 1, tier);
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
    public boolean canBreakBlock(ItemStack par1ItemStack, World par2World, int par3, int metadata, EntityLivingBase par7EntityLiving)
    {
        Block block = Block.blocksList[par3];
        ItemStack attachment = this.getAttachment(par1ItemStack);
        String toolClass = ((IAttachment) attachment.getItem()).getToolType(attachment);
        int toolLevel = ((IAttachment) attachment.getItem()).getAttachmentTier(attachment);
        int blockLevel = MinecraftForge.getBlockHarvestLevel(block, metadata, toolClass);
        return toolLevel >= blockLevel && this.canDoWork(par1ItemStack);
    }

    /**
     * @param enchant
     *            The enchantment to check for.
     * @param assembledTool
     *            The ItemStack to check on.
     * @return The level of the enchantment. -1 if the enchantment is not on the
     *         given tool
     */
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

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving)
    {
        if (par1ItemStack.getTagCompound().hasKey("damageVsEntity"))
        {
            if (!par1ItemStack.getTagCompound().getCompoundTag("damageVsEntity").hasKey(par2EntityLiving.getEntityName()))
            {
                ;
            }
            {
                BiMap<Entity, Integer> damageMap = HashBiMap.create();
                if (damageMap.containsKey(par2EntityLiving))
                {
                    int damage = damageMap.get(par2EntityLiving);
                    par1ItemStack.getTagCompound().getCompoundTag("damageVsEntity").setInteger(par2EntityLiving.getEntityName(), damage);
                }
            }
        }
        if (this.onItemUse(par1ItemStack))
            return true;
        else
            return false;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer par2EntityPlayer, EntityLivingBase entity)
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player)
    {
        if (player.worldObj.isRemote)
            return false;
        int id = player.worldObj.getBlockId(x, y, z);
        if (Block.blocksList[id] instanceof IShearable && ((IAttachment) this.getAttachment(itemstack).getItem()).getToolType(this.getAttachment(itemstack)).toLowerCase().equals("shears"))
        {
            IShearable target = (IShearable) Block.blocksList[id];
            if (target.isShearable(itemstack, player.worldObj, x, y, z) && this.onItemUse(itemstack))
            {
                ArrayList<ItemStack> drops = target.onSheared(itemstack, player.worldObj, x, y, z, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
                Random rand = new Random();

                for (ItemStack stack : drops)
                {
                    float f = 0.7F;
                    double d = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                    double d1 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                    double d2 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(player.worldObj, x + d, y + d1, z + d2, stack);
                    entityitem.delayBeforeCanPickup = 10;
                    player.worldObj.spawnEntityInWorld(entityitem);
                }

                player.addStat(StatList.mineBlockStatArray[id], 1);
            }
        }
        return false;
    }

    @Override
    public boolean isEffectiveOnBlock(ItemStack assembledTool, World world, int blockID, int metadata, EntityLivingBase entity)
    {
        Block block = Block.blocksList[blockID];
        if (((IAttachment) this.getAttachment(assembledTool).getItem()).getToolType(this.getAttachment(assembledTool)).equals("shears"))
        {
            if (block.equals(Block.leaves) || block.equals(Block.web) || block.equals(Block.tallGrass) || block.equals(Block.deadBush) || block.equals(Block.vine) || block.equals(Block.tripWire)
            || block instanceof IShearable)
                return true;
        }

        IAttachment attach = (IAttachment) this.getAttachment(assembledTool).getItem();
        String toolClass = attach.getToolType(this.getAttachment(assembledTool));
        int harvestLevel = MinecraftForge.getBlockHarvestLevel(block, metadata, toolClass);
        return harvestLevel <= attach.getAttachmentTier(this.getAttachment(assembledTool)) + 1 && !(harvestLevel < 0);
    }

    @Override
    public float[] getAdditionalDisplayData(ItemStack assembledTool)
    {
        float currEnergy = this.getEnergy(assembledTool);
        float maxEnergy = this.getEnergyCapacity(assembledTool);
        return new float[] { currEnergy / maxEnergy };
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float HitY, float hitZ)
    {
        IAttachment attach = (IAttachment) this.getAttachment(itemstack).getItem();
        return attach.canRightClick(this.getAttachment(itemstack), new Object[] { world, x, y, z, side, hitX, HitY, hitZ }, player) && this.canDoWork(itemstack);
    }

    @Override
    public float getDamageVsEntity(Entity par1Entity, ItemStack itemStack)
    {
        NBTTagCompound damageTag = itemStack.getTagCompound().getCompoundTag("damageVsEntity");
        float damage = damageTag.hasKey(par1Entity.getEntityName()) ? damageTag.getFloat(par1Entity.getEntityName()) : damageTag.hasKey("") ? damageTag.getFloat("") : 0;
        return damage;
    }
}
