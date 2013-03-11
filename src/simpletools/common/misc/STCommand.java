package simpletools.common.misc;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class STCommand extends CommandBase
{
	public String commandName = "stcredits";
	
	public final String[] credits = 	
		{ 	
			"In no particular order:",
			"Calclavia: For the UE API, which was and is a great springboard to start from.",
			"mDiyo: For creating InfiTools, whic was part of the inspiration of this mod. This was coded seperately from any of the mods created by mDiyo.",
			"VSWE: For creating Steve's Carts 2. SC2 is one of the things that gave me the idea for Modular Tools, in this implementation.",
			"MachineMuse: For MMMPS, a mod which showed me how to read, and write, ItemStack NBT. It was too confusing for me to copy the code though, but that doesn't matter.",
			"Eearslya: For creating the original \"Power Tools for UE\" mod. No code was copied, as it would have been more effort than it was worth. But textures were copied.",
			"Forge Dev. Team: Need I write more?",
			"Mojang: For Minecraft, an awesome game to both play and mod."
		};
	
	@Override
	public String getCommandName()
	{
		return commandName;
	}
	
	@Override
	public void processCommand(ICommandSender var1, String[] var2)
	{
		if (var1 instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) var1;
			for (String message : credits)
				player.sendChatToPlayer(message);
		}
	}
	
}
