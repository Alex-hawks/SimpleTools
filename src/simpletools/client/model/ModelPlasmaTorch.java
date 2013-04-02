package simpletools.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPlasmaTorch extends ModelBase
{
    ModelRenderer Main;
    
    public ModelPlasmaTorch()
    {
        this.textureWidth = 32;
        this.textureHeight = 32;
        
        this.Main = new ModelRenderer(this, 0, 0);
        this.Main.addBox(0F, 0F, 0F, 4, 4, 4);
        this.Main.setRotationPoint(-2F, 14F, -2F);
        this.Main.setTextureSize(this.textureWidth, this.textureHeight);
        this.Main.mirror = true;
        this.setRotation(this.Main, 0F, 0F, 0F);
    }
    
    public void renderMain()
    {
        this.Main.render(0.0625F);
    }
    
    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3,
            float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
    
    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4,
            float par5, float par6, float par7)
    {
        this.Main.render(par7);
    }
}
