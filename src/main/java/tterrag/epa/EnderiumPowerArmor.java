package tterrag.epa;

import java.io.File;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import tterrag.epa.items.ItemArmorEnderium;
import tterrag.epa.lib.Reference;
import tterrag.epa.util.EPAEventHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies="required-after:ThermalExpansion")
public class EnderiumPowerArmor
{
	@Instance
	public EnderiumPowerArmor instance;

	public static Item enderiumHelm;
	public static Item enderiumChest;
	public static Item enderiumLegs;
	public static Item enderiumBoots;
	
	public static int starterID;
	public static int realID;
	
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
		addRecipes();
		
		MinecraftForge.EVENT_BUS.register(new EPAEventHandler());
	}
	
	private void loadConfig(File file)
	{
		Configuration config = new Configuration(file);
		config.load();
		
		starterID = config.getItem("armorID", 23483, "The starting ID for the armor, automatically incremented for all 4 items.").getInt() - 256;
		realID = starterID + 256;
		
		config.save();
	}
	
	private void addRecipes()
	{
		ItemStack ingot = thermalexpansion.item.TEItems.ingotEnderium.copy();
		ItemStack capacitor = thermalexpansion.item.TEItems.capacitorResonant.copy();
			
		GameRegistry.addRecipe(new ItemStack(enderiumHelm, 1, 101), new Object[]{
			"iii",
			"ici",
			
			'i', ingot,
			'c', capacitor
		});
		
		GameRegistry.addRecipe(new ItemStack(enderiumChest, 1, 101), new Object[]{
			"i i",
			"ici",
			"iii",
			
			'i', ingot,
			'c', capacitor
		});
		
		GameRegistry.addRecipe(new ItemStack(enderiumLegs, 1, 101), new Object[]{
			"iii",
			"ici",
			"i i",
			
			'i', ingot,
			'c', capacitor
		});
		
		GameRegistry.addRecipe(new ItemStack(enderiumBoots, 1, 101), new Object[]{
			"i i",
			"ici",
			
			'i', ingot,
			'c', capacitor
		});
	}
	
}
