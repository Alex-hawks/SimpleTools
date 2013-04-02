package simpletools.common.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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

import simpletools.common.SimpleTools;
import simpletools.common.interfaces.IAssembledElectricTool;
import simpletools.common.interfaces.IAssembledTool;
import simpletools.common.interfaces.IAttachment;
import simpletools.common.interfaces.ICore;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAssembledToolElectric extends ItemTool implements
        IAssembledElectricTool
{
    private final double joulesPerUse = 2000; // in Joules
    
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
    public double getMaxJoules(ItemStack itemStack)
    {
        try
        {
            return itemStack.stackTagCompound.getCompoundTag("SimpleTools")
                    .getDouble("maxEnergy");
        }
        catch (Exception e)
        {
            return 0D;
        }
    }
    
    @Override
    public double getVoltage(ItemStack itemStack)
    {
        try
        {
            NBTTagCompound info = itemStack.stackTagCompound.getCompoundTag(
                    "SimpleTools").getCompoundTag("battery");
            ItemStack batteryStack = ItemStack.loadItemStackFromNBT(info);
            return ((IItemElectric) batteryStack.getItem())
                    .getVoltage(batteryStack);
        }
        catch (Exception e)
        {
            return 35D;
        }
    }
    
    @Override
    public double getJoules(ItemStack itemStack)
    {
        try
        {
            return itemStack.stackTagCompound.getCompoundTag("SimpleTools")
                    .getDouble("electricity");
        }
        catch (Exception e)
        {
            return 0D;
        }
    }
    
    @Override
    public void setJoules(double joules, ItemStack itemStack)
    {
        itemStack.stackTagCompound.getCompoundTag("SimpleTools").setDouble(
                "electricity", joules);
    }
    
    @Override
    public ElectricityPack onReceive(ElectricityPack electricityPack,
            ItemStack itemStack)
    {
        try
        {
            double rejectedElectricity = Math.max(
                    this.getJoules(itemStack) + electricityPack.getWatts()
                            - this.getMaxJoules(itemStack), 0);
            double joulesToStore = electricityPack.getWatts()
                    - rejectedElectricity;
            this.setJoules(this.getJoules(itemStack) + joulesToStore, itemStack);
            return ElectricityPack.getFromWatts(joulesToStore,
                    this.getVoltage(itemStack));
        }
        catch (Exception e)
        {
            return electricityPack;
        }
    }
    
    @Override
    public ElectricityPack onProvide(ElectricityPack electricityPack,
            ItemStack itemStack)
    {
        try
        {
            double electricityToUse = Math.min(this.getJoules(itemStack),
                    electricityPack.getWatts());
            this.setJoules(this.getJoules(itemStack) - electricityToUse,
                    itemStack);
            return ElectricityPack.getFromWatts(electricityToUse,
                    this.getVoltage(itemStack));
        }
        catch (Exception e)
        {
            return new ElectricityPack(0, 0);
        }
    }
    
    @Override
    public ElectricityPack getReceiveRequest(ItemStack itemStack)
    {
        try
        {
            return ElectricityPack.getFromWatts(this.getMaxJoules(itemStack)
                    - this.getJoules(itemStack), this.getVoltage(itemStack));
        }
        catch (Exception e)
        {
            return new ElectricityPack(0, 0);
        }
    }
    
    @Override
    public ElectricityPack getProvideRequest(ItemStack itemStack)
    {
        return new ElectricityPack(0, 0);
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
    public ItemStack onCreate(ItemStack attachment, ItemStack core,
            ItemStack battery)
    {
        ItemStack returnStack = null;
        if (attachment.getItem() instanceof IAttachment
                && core.getItem() instanceof ICore)
        {
            IAttachment attachmentTemp = (IAttachment) attachment.getItem();
            ICore coreTemp = (ICore) core.getItem();
            
            if (coreTemp.getCoreType(core) == attachmentTemp
                    .getToolAttachmentType(attachment))
            {
                if (coreTemp.getCoreTier(core) >= attachmentTemp
                        .getMinimumTier(attachment))
                {
                    int coreMeta = coreTemp.getCoreTier(core) * 1000;
                    int attachMeta = attachmentTemp
                            .getAttachmentUID(attachment);
                    int meta = coreMeta + attachMeta;
                    returnStack = new ItemStack(this, 1, meta);
                    
                    NBTTagCompound compound = new NBTTagCompound();
                    compound.setCompoundTag("SimpleTools", new NBTTagCompound());
                    compound.setCompoundTag("damageVsEntity",
                            new NBTTagCompound());
                    compound.setBoolean("useDamageVsEntityTag", true);
                    
                    compound.getCompoundTag("SimpleTools").setDouble(
                            "electricity",
                            ((IItemElectric) battery.getItem())
                                    .getJoules(battery));
                    compound.getCompoundTag("SimpleTools").setDouble(
                            "maxEnergy",
                            ((IItemElectric) battery.getItem())
                                    .getMaxJoules(battery));
                    compound.getCompoundTag("SimpleTools").setCompoundTag(
                            "attachment",
                            attachment.writeToNBT(new NBTTagCompound()));
                    compound.getCompoundTag("SimpleTools")
                            .setCompoundTag("battery",
                                    battery.writeToNBT(new NBTTagCompound()));
                    
                    compound.getCompoundTag("damageVsEntity").setInteger(
                            "",
                            attachmentTemp.getDamageVsEntities(attachment).get(
                                    null));
                    
                    returnStack.setTagCompound(compound);
                }
            }
        }
        return returnStack;
    }
    
    @Override
    public float getStrVsBlock(ItemStack itemStack, Block block, int meta)
    {
        ItemStack attachment = this.getAttachment(itemStack);
        String toolClass = ((IAttachment) attachment.getItem())
                .getToolType(attachment);
        int toolLevel = ((IAttachment) attachment.getItem())
                .getAttachmentTier(attachment);
        int blockLevel = MinecraftForge.getBlockHarvestLevel(block, meta,
                toolClass);
        
        if (toolLevel >= blockLevel)
            return (float) ((IAttachment) attachment.getItem())
                    .getHarvestSpeed(attachment);
        else
            return 1f;
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack itemStack, EntityPlayer player,
            List currentTips, boolean advancedToolTips)
    {
        ItemStack attachment = null, battery = null;
        try
        {
            NBTTagCompound batt = itemStack.getTagCompound()
                    .getCompoundTag("SimpleTools").getCompoundTag("battery");
            battery = ItemStack.loadItemStackFromNBT(batt);
            
            NBTBase attach = itemStack.getTagCompound()
                    .getCompoundTag("SimpleTools").getCompoundTag("attachment");
            attachment = ItemStack
                    .loadItemStackFromNBT((NBTTagCompound) attach);
        }
        catch (Exception e)
        {
        }
        
        currentTips.add("Energy: "
                + ElectricityDisplay.getDisplayShort(this.getJoules(itemStack),
                        ElectricUnit.JOULES)
                + " / "
                + ElectricityDisplay.getDisplayShort(
                        this.getMaxJoules(itemStack), ElectricUnit.JOULES));
        
        if (Keyboard
                .isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.keyCode))
        {
            if (this.getCore(itemStack) != null)
            {
                currentTips.add("Core Module: "
                        + this.getCore(itemStack).getDisplayName());
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
            this.setJoules(this.getJoules(i) - this.joulesPerUse, i);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean canDoWork(ItemStack i)
    {
        return this.getJoules(i) >= this.joulesPerUse;
    }
    
    @Override
    public int getItemEnchantability()
    {
        return 0;
    }
    
    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack,
            ItemStack par2ItemStack)
    {
        return false;
    }
    
    @Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World,
            int id, int x, int y, int z, EntityLiving par7EntityLiving)
    {
        Block block = Block.blocksList[id];
        int metadata = par2World.getBlockMetadata(x, y, z);
        double blockHardness = block.getBlockHardness(par2World, x, y, z);
        if (((IAttachment) this.getAttachment(par1ItemStack).getItem())
                .getToolType(this.getAttachment(par1ItemStack))
                .equals("shears"))
        {
            if (block.equals(Block.leaves) || block.equals(Block.web)
                    || block.equals(Block.tallGrass)
                    || block.equals(Block.deadBush) || block.equals(Block.vine)
                    || block.equals(Block.tripWire)
                    || block instanceof IShearable)
                return true;
        }
        if (blockHardness != 0D
                && this.canBreakBlock(par1ItemStack, par2World, id, metadata,
                        par7EntityLiving)
                && !block.canHarvestBlock((EntityPlayer) par7EntityLiving,
                        par2World.getBlockMetadata(x, y, z)))
        {
            if (this.onItemUse(par1ItemStack))
            {
                if (this.getEnchantmentLvl(Enchantment.silkTouch, par1ItemStack) > 0)
                {
                    this.dropBlockAsItem_do(par2World, x, y, z, new ItemStack(
                            block, 1, par2World.getBlockMetadata(x, y, z)));
                }
                else
                {
                    int fortune = this.getEnchantmentLvl(Enchantment.fortune,
                            par1ItemStack);
                    block.dropBlockAsItem(par2World, x, y, z,
                            par2World.getBlockMetadata(x, y, z), fortune);
                }
                return true;
            }
            else
                return false;
        }
        else if (blockHardness != 0D
                && this.canBreakBlock(par1ItemStack, par2World, id, metadata,
                        par7EntityLiving))
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
        return new ItemStack(SimpleTools.coreMechElectric, 1, tier);
    }
    
    @Override
    public ItemStack getStorage(ItemStack assembledTool)
    {
        ItemStack battery = null;
        try
        {
            NBTTagCompound batt = assembledTool.getTagCompound()
                    .getCompoundTag("SimpleTools").getCompoundTag("battery");
            battery = ItemStack.loadItemStackFromNBT(batt);
            ((IItemElectric) battery.getItem()).setJoules(
                    this.getJoules(assembledTool), battery);
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
            NBTBase attach = assembledTool.getTagCompound()
                    .getCompoundTag("SimpleTools").getCompoundTag("attachment");
            attachment = ItemStack
                    .loadItemStackFromNBT((NBTTagCompound) attach);
            if (!assembledTool.getEnchantmentTagList().equals(null))
            {
                attachment.setTagCompound(new NBTTagCompound());
                attachment.getTagCompound().setTag(
                        assembledTool.getEnchantmentTagList().getName(),
                        assembledTool.getEnchantmentTagList());
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
                int coreMeta = ((IAssembledTool) stack.getItem())
                        .getCore(stack).getItemDamage();
                return ((IAssembledTool) stack.getItem()).getCore(stack)
                        .getItem().getIconFromDamage(coreMeta);
            }
            catch (Throwable e)
            {
            }
        }
        else if (pass == 1)
        {
            try
            {
                int attachMeta = ((IAssembledTool) stack.getItem())
                        .getAttachment(stack).getItemDamage();
                return ((IAssembledTool) stack.getItem()).getAttachment(stack)
                        .getItem().getIconFromDamage(attachMeta);
            }
            catch (Throwable e)
            {
            }
        }
        return null;
    }
    
    @Override
    public boolean canBreakBlock(ItemStack par1ItemStack, World par2World,
            int par3, int metadata, EntityLiving par7EntityLiving)
    {
        Block block = Block.blocksList[par3];
        ItemStack attachment = this.getAttachment(par1ItemStack);
        String toolClass = ((IAttachment) attachment.getItem())
                .getToolType(attachment);
        int toolLevel = ((IAttachment) attachment.getItem())
                .getAttachmentTier(attachment);
        int blockLevel = MinecraftForge.getBlockHarvestLevel(block, metadata,
                toolClass);
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
            for (int i = 0; i < assembledTool.getEnchantmentTagList()
                    .tagCount(); i++)
            {
                if (assembledTool.getEnchantmentTagList().tagAt(i) instanceof NBTTagCompound)
                {
                    if (((NBTTagCompound) assembledTool.getEnchantmentTagList()
                            .tagAt(i)).getShort("id") == enchant.effectId)
                    {
                        level = ((NBTTagCompound) assembledTool
                                .getEnchantmentTagList().tagAt(i))
                                .getShort("lvl");
                    }
                }
            }
        }
        return level;
    }
    
    protected void dropBlockAsItem_do(World par1World, int par2, int par3,
            int par4, ItemStack par5ItemStack)
    {
        if (!par1World.isRemote
                && par1World.getGameRules().getGameRuleBooleanValue(
                        "doTileDrops"))
        {
            float var6 = 0.7F;
            double var7 = par1World.rand.nextFloat() * var6 + (1.0F - var6)
                    * 0.5D;
            double var9 = par1World.rand.nextFloat() * var6 + (1.0F - var6)
                    * 0.5D;
            double var11 = par1World.rand.nextFloat() * var6 + (1.0F - var6)
                    * 0.5D;
            EntityItem var13 = new EntityItem(par1World, par2 + var7, par3
                    + var9, par4 + var11, par5ItemStack);
            var13.delayBeforeCanPickup = 10;
            par1World.spawnEntityInWorld(var13);
        }
    }
    
    @Override
    public boolean hitEntity(ItemStack par1ItemStack,
            EntityLiving par2EntityLiving, EntityLiving par3EntityLiving)
    {
        if (par1ItemStack.getTagCompound().hasKey("damageVsEntity"))
        {
            if (!par1ItemStack.getTagCompound()
                    .getCompoundTag("damageVsEntity")
                    .hasKey(par2EntityLiving.getEntityName()))
            {
                ;
            }
            {
                BiMap<Entity, Integer> damageMap = HashBiMap.create();
                if (damageMap.containsKey(par2EntityLiving))
                {
                    int damage = damageMap.get(par2EntityLiving);
                    par1ItemStack
                            .getTagCompound()
                            .getCompoundTag("damageVsEntity")
                            .setInteger(par2EntityLiving.getEntityName(),
                                    damage);
                }
            }
        }
        if (this.onItemUse(par1ItemStack))
            return true;
        else
            return false;
    }
    
    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack,
            EntityLiving entity)
    {
        IAttachment attach = (IAttachment) this.getAttachment(itemstack)
                .getItem();
        if (attach.canRightClick(this.getAttachment(itemstack), entity, null)
                && this.canDoWork(itemstack))
        {
            attach.onRightClick(this.getAttachment(itemstack), entity, null);
            return true;
        }
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateIcons(IconRegister par1IconRegister)
    {
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z,
            EntityPlayer player)
    {
        if (player.worldObj.isRemote)
            return false;
        int id = player.worldObj.getBlockId(x, y, z);
        if (Block.blocksList[id] instanceof IShearable
                && ((IAttachment) this.getAttachment(itemstack).getItem())
                        .getToolType(this.getAttachment(itemstack))
                        .toLowerCase().equals("shears"))
        {
            IShearable target = (IShearable) Block.blocksList[id];
            if (target.isShearable(itemstack, player.worldObj, x, y, z)
                    && this.onItemUse(itemstack))
            {
                ArrayList<ItemStack> drops = target.onSheared(itemstack,
                        player.worldObj, x, y, z,
                        EnchantmentHelper.getEnchantmentLevel(
                                Enchantment.fortune.effectId, itemstack));
                Random rand = new Random();
                
                for (ItemStack stack : drops)
                {
                    float f = 0.7F;
                    double d = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                    double d1 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                    double d2 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(player.worldObj, x
                            + d, y + d1, z + d2, stack);
                    entityitem.delayBeforeCanPickup = 10;
                    player.worldObj.spawnEntityInWorld(entityitem);
                }
                
                player.addStat(StatList.mineBlockStatArray[id], 1);
            }
        }
        return false;
    }
    
    @Override
    public boolean isEffectiveOnBlock(ItemStack assembledTool, World world,
            int blockID, int metadata, EntityLiving entity)
    {
        Block block = Block.blocksList[blockID];
        if (((IAttachment) this.getAttachment(assembledTool).getItem())
                .getToolType(this.getAttachment(assembledTool))
                .equals("shears"))
        {
            if (block.equals(Block.leaves) || block.equals(Block.web)
                    || block.equals(Block.tallGrass)
                    || block.equals(Block.deadBush) || block.equals(Block.vine)
                    || block.equals(Block.tripWire)
                    || block instanceof IShearable)
                return true;
        }
        
        IAttachment attach = (IAttachment) this.getAttachment(assembledTool)
                .getItem();
        String toolClass = attach
                .getToolType(this.getAttachment(assembledTool));
        int harvestLevel = MinecraftForge.getBlockHarvestLevel(block, metadata,
                toolClass);
        return harvestLevel <= attach.getAttachmentTier(this
                .getAttachment(assembledTool)) + 1 && !(harvestLevel < 0);
    }
    
    @Override
    public String getStoredForDisplay(ItemStack assembledTool)
    {
        String currEnergy = ElectricityDisplay.getDisplayShort(
                this.getJoules(assembledTool),
                ElectricityDisplay.ElectricUnit.JOULES);
        String maxEnergy = ElectricityDisplay.getDisplayShort(
                this.getMaxJoules(assembledTool),
                ElectricityDisplay.ElectricUnit.JOULES);
        if (this.getJoules(assembledTool) != Double.POSITIVE_INFINITY)
            return currEnergy + " of " + maxEnergy;
        else
            return "Infinite";
    }
    
    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player,
            World world, int x, int y, int z, int side, float hitX, float HitY,
            float hitZ)
    {
        IAttachment attach = (IAttachment) this.getAttachment(itemstack)
                .getItem();
        return attach
                .canRightClick(this.getAttachment(itemstack), new Object[] {
                        world, x, y, z, side, hitX, HitY, hitZ }, player)
                && this.canDoWork(itemstack);
    }
    
    @Override
    public int getDamageVsEntity(Entity par1Entity, ItemStack itemStack)
    {
        NBTTagCompound damageTag = itemStack.getTagCompound().getCompoundTag(
                "damageVsEntity");
        int damage = damageTag.hasKey(par1Entity.getEntityName()) ? damageTag
                .getInteger(par1Entity.getEntityName())
                : damageTag.hasKey("") ? damageTag.getInteger("") : 0;
        return damage;
    }
}
