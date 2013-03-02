package simpletools.common.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import simpletools.common.interfaces.IAssembledTool;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class SimpleToolsEventHandler
{
	@ForgeSubscribe
	public void onAttack(EntityEvent event)
	{
		if (!(event instanceof CheckSpawn || event instanceof LivingUpdateEvent || event instanceof EnteringChunk || 
				event instanceof PlaySoundAtEntityEvent || event instanceof EntityJoinWorldEvent))
		{
			System.out.println(event.getClass().getSimpleName());
			if (event.entity instanceof EntityLiving)
				System.out.println("Health: " + ((EntityLiving)event.entity).getHealth());
		}		
	}
}
