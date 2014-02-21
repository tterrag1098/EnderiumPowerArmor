package tterrag.epa;

import tterrag.epa.lib.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class EnderiumPowerArmor
{
	@Instance
	public EnderiumPowerArmor instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		// Register blocks and items
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		// Anything else
	}
}
