package simpletools.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import simpletools.common.SimpleTools;
import simpletools.common.interfaces.ICore;
import simpletools.common.misc.SimpleToolsCreativeTab;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCorePlasma extends Item implements ICore
{
    private Icon[] icons = new Icon[4];
    
    public ItemCorePlasma(int itemID, String name)
    {
        super(itemID);
        this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
        this.setUnlocalizedName(name);
        this.setMaxStackSize(16);
    }
    
    @Override
    public int getCoreType(ItemStack i)
    {
        return 1;
    }
    
    @Override
    public int getCoreTier(ItemStack i)
    {
        return i.getItemDamage();
    }
    
    @Override
    public byte getCoreUID(ItemStack i)
    {
        return (byte) (i.getItemDamage() + 4);
    }
    
    @Override
    public String getCoreFinerType(ItemStack i)
    {
        return "plasma";
    }
    
    @Override
    public Item getAssmebledToolItem(ItemStack i)
    {
        return SimpleTools.assembledToolPlasma;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return this.getUnlocalizedName() + "." + this.getCoreTier(itemStack);
    }
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs,
            List par3List)
    {
        for (int i = 0; i < 4; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
    
    @Override
    public boolean isDamageable()
    {
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateIcons(IconRegister iconRegister)
    {
        for (int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon((this
                    .getUnlocalizedName() + "." + i).replace("item.",
                    SimpleTools.TEXTURE_NAME_PREFIX));
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta)
    {
        return this.icons[meta];
    }
}
