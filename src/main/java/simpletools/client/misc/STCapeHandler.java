package simpletools.client.misc;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import simpletools.common.SimpleTools;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.ReflectionHelper;
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

    private static final ResourceLocation developerCloak = new ResourceLocation(SimpleTools.PREFIX, SimpleTools.CLOAK_PATH + "Dev-Cape.png");
    private static final ResourceLocation assistantCloak = new ResourceLocation(SimpleTools.PREFIX, SimpleTools.CLOAK_PATH + "Assistant-Cape.png");

    private STCapeHandler()
    {
        // Major Coders
        this.registerUEDeveloper("Alex_hawks"); // Alex_hawks
        this.registerUEDeveloper("calclavia"); // Calclavia
        this.registerUEDeveloper("Darkguardsman"); // Darkguardsman
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        if (mc.theWorld != null && mc.theWorld.playerEntities.size() > 0)
        {
            @SuppressWarnings("unchecked")
            List<EntityPlayer> players = mc.theWorld.playerEntities;

            // the loops that goes through each player
            for (EntityPlayer ePlayer : players)
            {
                if (ePlayer instanceof AbstractClientPlayer)
                {
                    AbstractClientPlayer player = (AbstractClientPlayer) ePlayer;

                    //  For if they want to keep their custom cape. It will only replace their cape, if it is their own
                    if (AbstractClientPlayer.getCapeUrl(player.username).startsWith("http://skins.minecraft.net/MinecraftCloaks/" + player.username))
                    {
                        if (ueDevelopers.contains(StringUtils.stripControlCodes(player.username).toLowerCase()))
                        {
                            ReflectionHelper.setPrivateValue(AbstractClientPlayer.class, player, developerCloak, "locationCape", "field_" + "110313_e");
                        }
                        else if (ueAssistants.contains(StringUtils.stripControlCodes(player.username).toLowerCase()))
                        {
                            ReflectionHelper.setPrivateValue(AbstractClientPlayer.class, player, assistantCloak, "locationCape", "field_" + "110313_e");
                        }
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
