package tterrag.epa;

import java.io.File;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import tterrag.epa.items.ItemArmorEnderium;
import tterrag.epa.lib.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class EnderiumPowerArmor
{
	@Instance
	public EnderiumPowerArmor instance;

	public static Item enderiumHelm;
	public static Item enderiumChest;
	public static Item enderiumLegs;
	public static Item enderiumBoots;
	
	private int starterID;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		loadConfig(event.getSuggestedConfigurationFile());
		
		enderiumHelm = new ItemArmorEnderium(starterID++, EnumArmorMaterial.DIAMOND, ItemArmorEnderium.ArmorType.HELMET);
		enderiumChest = new ItemArmorEnderium(starterID++, EnumArmorMaterial.DIAMOND, ItemArmorEnderium.ArmorType.CHESTPLATE);
		enderiumLegs = new ItemArmorEnderium(starterID++, EnumArmorMaterial.DIAMOND, ItemArmorEnderium.ArmorType.LEGS);
		enderiumBoots = new ItemArmorEnderium(starterID++, EnumArmorMaterial.DIAMOND, ItemArmorEnderium.ArmorType.BOOTS);
		
		GameRegistry.registerItem(enderiumHelm, "enderiumHelm");
		GameRegistry.registerItem(enderiumChest, "enderiumChest");
		GameRegistry.registerItem(enderiumLegs, "enderiumLegs");
		GameRegistry.registerItem(enderiumBoots, "enderiumBoots");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		// Anything else
	}
	
	private void loadConfig(File file)
	{
		Configuration config = new Configuration(file);
		config.load();
		
		starterID = config.getItem("armorID", 23483, "The starting ID for the armor, automatically incremented for all 4 items.").getInt() - 256;
		
		config.save();
	}
}
