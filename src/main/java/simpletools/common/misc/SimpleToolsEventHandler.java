package simpletools.common.misc;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import simpletools.api.IAssembledTool;

public class SimpleToolsEventHandler
{
    @ForgeSubscribe
    public void playerBreakingBlock(BreakSpeed event)
    {
        ItemStack usedIS = event.entityPlayer.inventory.getCurrentItem();
        if (usedIS != null && usedIS.getItem() instanceof IAssembledTool && ((IAssembledTool) usedIS.getItem()).canDoWork(usedIS))
        {
            event.newSpeed = this.getCurrentPlayerStrVsBlock(event.entityPlayer, event.block, false, event.metadata);
        }
    }

    public float getCurrentPlayerStrVsBlock(EntityPlayer player, Block par1Block, boolean par2, int meta)
    {
        ItemStack stack = player.inventory.getCurrentItem();
        float f = (stack == null ? 1.0F : stack.getItem().getStrVsBlock(stack, par1Block, meta));

        if (f > 1.0F)
        {
            int i = EnchantmentHelper.getEfficiencyModifier(player);
            ItemStack itemstack = player.inventory.getCurrentItem();

            if (i > 0 && itemstack != null && itemstack.getItem() instanceof IAssembledTool)
            {
                IAssembledTool tool = (IAssembledTool) itemstack.getItem();
                float f1 = (float)(i * i + 1);

                boolean canHarvest = tool.canBreakBlock(itemstack, player.worldObj, par1Block.blockID, meta, player);

                System.out.println("canHarvest: " + canHarvest);
                if (!canHarvest && f <= 1.0F)
                {
                    f += f1 * 0.08F;
                }
                else
                {
                    f += f1;
                }
            }
        }

        if (player.isPotionActive(Potion.digSpeed))
        {
            f *= 1.0F + (float)(player.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
        }

        if (player.isPotionActive(Potion.digSlowdown))
        {
            f *= 1.0F - (float)(player.getActivePotionEffect(Potion.digSlowdown).getAmplifier() + 1) * 0.2F;
        }

        if (player.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(player))
        {
            f /= 5.0F;
        }

        if (!player.onGround)
        {
            f /= 5.0F;
        }

        return (f < 0 ? 0 : f);
    }
}
