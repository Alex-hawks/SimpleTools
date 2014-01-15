package simpletools.common.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import simpletools.api.IAssembledTool;

public class SimpleToolsEventHandler
{
    @ForgeSubscribe
    public void playerBreakingBlock(BreakSpeed event)
    {
        ItemStack usedIS = event.entityPlayer.inventory.getCurrentItem();
        if (usedIS != null && usedIS.getItem() instanceof IAssembledTool)
        {
            IAssembledTool tool = (IAssembledTool) usedIS.getItem();
            World world = event.entityPlayer.worldObj;
            int id = event.block.blockID;
            int meta = event.metadata;
            if (tool.canBreakBlock(usedIS, world, id, meta, event.entityPlayer) && tool.isEffectiveOnBlock(usedIS, world, id, meta, event.entityPlayer))
            {
                event.newSpeed = usedIS.getItem().getStrVsBlock(usedIS, event.block, event.metadata);
            }
            else
            {
                event.newSpeed = 1.0f;
            }
        }
    }
}
