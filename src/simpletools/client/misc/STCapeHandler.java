package simpletools.client.misc;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class STCapeHandler implements ITickHandler
{
    private static final Minecraft mc = Minecraft.getMinecraft();
    /**
     * Don't add to this, use {@link STCapeHandler#registerUEDeveloper}
     */
    private static List<String> ueDevelopers = new ArrayList<String>();
    
    /**
     * Don't add to this, use {@link STCapeHandler#registerUEAssistant}
     */
    private static List<String> ueAssistants = new ArrayList<String>();
    
    public static final STCapeHandler INSTANCE = new STCapeHandler();
    private static final String developerCloakURL = "https://raw.github.com/Alex-hawks/SimpleTools/master/misc/UE-Dev-Cape.png";
    private static final String assistantCloakURL = "https://raw.github.com/Alex-hawks/SimpleTools/master/misc/UE-Assistant-Cape.png";
    
    private STCapeHandler()
    {
        // Major Coders
        this.registerUEDeveloper("Alex_hawks"); // Alex_hawks
        this.registerUEDeveloper("Mattredsox"); // Mattredsox
        this.registerUEDeveloper("calclavia"); // Calclavia
        this.registerUEDeveloper("briman0094"); // Briman
        this.registerUEDeveloper("TrainerGuy22"); // TheMike
        this.registerUEDeveloper("liquidyyy64"); // LiQuiD
        this.registerUEDeveloper("Darkguardsman"); // Darkguardsman
        this.registerUEDeveloper("aidancbrady"); // aidancbrady
        this.registerUEDeveloper("micdoodle8"); // micdoodle
        
        // Major Texture Artists
        this.registerUEDeveloper("Tifflor"); // Comply
        
        // Donators
        this.registerUEAssistant("Crimsus"); // Donated $500.00 to UE. I wanted
                                             // to give him the developer
                                             // cape...
        
        // Assistants
        // this.registerUEAssistant("b1gb0ss"); // A friend that split up the
        // Simple Tools and EE textures
    }
    
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        if (mc.theWorld != null && mc.theWorld.playerEntities.size() > 0)
        {
            @SuppressWarnings("unchecked")
            List<EntityPlayer> players = mc.theWorld.playerEntities;
            
            // the loops that goes through each player
            for (EntityPlayer player : players)
            {
                String oldCloak = player.cloakUrl;
                
                if (player.cloakUrl.startsWith("http://skins.minecraft.net/MinecraftCloaks/"))
                {
                    if (ueDevelopers.contains(StringUtils.stripControlCodes(player.username).toLowerCase()))
                    {
                        player.cloakUrl = developerCloakURL;
                    }
                    else if (ueAssistants.contains(StringUtils.stripControlCodes(player.username).toLowerCase()))
                    {
                        player.cloakUrl = assistantCloakURL;
                    }
                    
                    if (!oldCloak.equals(player.cloakUrl))
                    {
                        mc.renderEngine.obtainImageData(player.cloakUrl, new SimpleToolsCloakDownload());
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
    
    /**
     * All Coders are developers, but not all developers are coders... </br>
     * This cape goes to anyone who has had a significant positive impact on the
     * UE mod collection
     * 
     * @param userName
     *            The Screen name of the person. Plain text, and lower case...
     */
    public void registerUEDeveloper(String userName)
    {
        if (!userName.equals(null))
        {
            ueDevelopers.add(userName.toLowerCase());
        }
    }
    
    /**
     * This cape goes to anyone who has had a small positive impact, and no
     * negative impacts, on the UE Mod Collection. This includes most donators.
     * In the case of the donator, the donation receiver determines whether or
     * not the contribution was significant enough for this.
     * 
     * @param userName
     *            The Screen name of the person. Plain text, and lower case...
     */
    public void registerUEAssistant(String userName)
    {
        if (!userName.equals(null))
        {
            ueAssistants.add(userName.toLowerCase());
        }
    }
}
