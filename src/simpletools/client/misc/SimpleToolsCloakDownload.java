package simpletools.client.misc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

import net.minecraft.client.renderer.IImageBuffer;

public class SimpleToolsCloakDownload implements IImageBuffer
{
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;
    
    @Override
    public BufferedImage parseUserSkin(BufferedImage par1BufferedImage)
    {
        if (par1BufferedImage == null)
            return null;
        
        this.imageWidth = par1BufferedImage.getWidth(null);
        this.imageHeight = par1BufferedImage.getHeight(null);
        BufferedImage bufferedimage1 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
        Graphics graphics = bufferedimage1.getGraphics();
        graphics.drawImage(par1BufferedImage, 0, 0, (ImageObserver) null);
        graphics.dispose();
        this.imageData = ((DataBufferInt) bufferedimage1.getRaster().getDataBuffer()).getData();
        boolean flag = false;
        int i;
        int j;
        int k;
        
        for (i = 32; i < 64; i++)
        {
            for (j = 0; j < 16; j++)
            {
                k = this.imageData[i + j * 64];
                
                if ((k >> 24 & 0xFF) >= 128)
                {
                    continue;
                }
                flag = true;
            }
            
        }
        
        if (!flag)
        {
            for (i = 32; i < 64; i++)
            {
                for (j = 0; j < 16; j++)
                {
                    k = this.imageData[i + j * 64];
                    
                    if ((k >> 24 & 0xFF) >= 128)
                    {
                        continue;
                    }
                    flag = true;
                }
            }
            
        }
        
        return bufferedimage1;
    }
}
