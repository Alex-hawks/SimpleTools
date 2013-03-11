package simpletools.common.misc;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class STTickHandler implements ITickHandler
{
	private static final Minecraft mc = Minecraft.getMinecraft();
	public static List<String> ueDevelopers = new ArrayList();
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) 
	{
		if ((mc.theWorld != null) && (mc.theWorld.playerEntities.size() > 0)) 
		{
			@SuppressWarnings("unchecked")
			List<EntityPlayer> players = mc.theWorld.playerEntities;
			
			// the loops that goes through each player
			for (EntityPlayer player : players) 
			{
				String oldCloak = player.cloakUrl;
				String groupUrl = oldCloak;
				
				if (ueDevelopers.contains(player.username.toLowerCase()))
				{
					if (player.cloakUrl.startsWith("http://skins.minecraft.net/MinecraftCloaks/")) 
					{
						player.cloakUrl = "";
						
						if (player.cloakUrl != oldCloak) 
							mc.renderEngine.obtainImageData(player.cloakUrl, new ImageBufferDownload());
					}
				}
			}
		}
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		
	}
	
	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.CLIENT);
	}
	
	@Override
	public String getLabel()
	{
		return "SimpleToolsClientTickHandler";
	}
	
	public static void registerUEDeveloper(String userName)
	{
		if (!userName.equals(null))
			ueDevelopers.add(userName);
	}
}
