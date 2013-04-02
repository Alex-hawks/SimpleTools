package simpletools.common.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import simpletools.common.interfaces.IAssembledTool;

public class SimpleToolsEventHandler
{
    /*
     * @ForgeSubscribe public void someEntityEvent(EntityEvent event) { if
     * (!(event instanceof CheckSpawn || event instanceof LivingUpdateEvent ||
     * event instanceof EnteringChunk || event instanceof PlaySoundAtEntityEvent
     * || event instanceof EntityJoinWorldEvent)) {
     * System.out.println(event.getClass().getSimpleName()); if (event.entity
     * instanceof EntityLiving) { System.out.println("Health: " +
     * ((EntityLiving)event.entity).getHealth()); if (event instanceof
     * AttackEntityEvent) { try { AttackEntityEvent newEvent =
     * (AttackEntityEvent) event; System.out.println("Damage: " +
     * newEvent.entityPlayer
     * .inventory.getDamageVsEntity(newEvent.entityLiving)); } catch (Exception
     * e) { e.printStackTrace(); } } } } }
     */
    /*
     * @ForgeSubscribe public void playerAttack(AttackEntityEvent event) {
     * ItemStack usedIS = event.entityPlayer.inventory.getCurrentItem(); if
     * (usedIS != null && usedIS.getItem() instanceof IAssembledTool &&
     * event.target instanceof EntityLiving) { event.setCanceled(true); Entity
     * target = event.target; EntityPlayer player = event.entityPlayer; int
     * damage = 0; if (usedIS.getTagCompound().hasKey("useDamageVsEntityTag") &&
     * usedIS.getTagCompound().getBoolean("useDamageVsEntityTag")) { if
     * (usedIS.getTagCompound().hasKey("damageVsEntity")) { NBTTagCompound
     * damageTag = usedIS.getTagCompound().getCompoundTag("damageVsEntity");
     * damage = damageTag.hasKey(target.getEntityName()) ?
     * damageTag.getInteger(target.getEntityName()) : damageTag.getInteger("");
     * } } target.attackEntityFrom(DamageSource.causePlayerDamage(player),
     * damage); } }
     */
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
            if (tool.canBreakBlock(usedIS, world, id, meta, event.entityPlayer)
                    && tool.isEffectiveOnBlock(usedIS, world, id, meta,
                            event.entityPlayer))
            {
                event.newSpeed = usedIS.getItem().getStrVsBlock(usedIS,
                        event.block, event.metadata);
            }
            else
            {
                event.newSpeed = 1.0f;
            }
        }
    }
}
