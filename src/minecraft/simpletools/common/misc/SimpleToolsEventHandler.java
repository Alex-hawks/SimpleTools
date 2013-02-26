package simpletools.common.misc;

import simpletools.common.interfaces.IAssembledTool;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class SimpleToolsEventHandler
{
	@ForgeSubscribe
	public void onAttack(AttackEntityEvent event)
	{
		if (event.entityPlayer.getCurrentEquippedItem().getItem() instanceof IAssembledTool && event.target instanceof EntityLiving)
		{
			int damage = 0;
			System.out.println("Before Bonus Attack: " + ((EntityLiving)event.target).getHealth());
			((EntityLiving)event.target).attackEntityFrom(DamageSource.causePlayerDamage(event.entityPlayer), damage);
			System.out.println("After Bonus Attack:  " + ((EntityLiving)event.target).getHealth());
		}
	}
}
