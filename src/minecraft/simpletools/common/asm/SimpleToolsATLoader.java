package simpletools.common.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class SimpleToolsATLoader implements IFMLLoadingPlugin
{
	
	@Override
	public String[] getLibraryRequestClass()
	{
		return null;
	}
	
	@Override
	public String[] getASMTransformerClass()
	{
		return null;
	}
	
	@Override
	public String getModContainerClass()
	{
		return "simpletools.common.asm.SimpleToolsModContainer";
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data)
	{
		
	}
	
}
