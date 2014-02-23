package tterrag.epa.items;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import thermalexpansion.ThermalExpansion;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ItemArmorEnderium extends ItemArmor implements IEnergyContainerItem, ISpecialArmor
{
	protected ArmorType type;

	private final int CHARGE_SPEED = 10000,
			          DAMAGE_BASE = 2000, 
			          DAMAGE_RAND = 200, 
			          CAPACITY = 10000000;

	public static enum ArmorType
	{
		HELMET, CHESTPLATE, LEGS, BOOTS;
	}

	public ItemArmorEnderium(int id, EnumArmorMaterial material, ArmorType type)
	{
		super(id, material, 0, type.ordinal());
		String texture = "", unlocalized = "";
		this.type = type;
		switch (type)
		{
		case BOOTS:
			texture = "enderiumpowerarmor:armorEnderiumBoots";
			unlocalized = "enderium.boots";
			break;
		case CHESTPLATE:
			texture = "enderiumpowerarmor:armorEnderiumChestplate";
			unlocalized = "enderium.chest";
			break;
		case HELMET:
			texture = "enderiumpowerarmor:armorEnderiumHelmet";
			unlocalized = "enderium.helmet";
			break;
		case LEGS:
			texture = "enderiumpowerarmor:armorEnderiumLegs";
			unlocalized = "enderium.legs";
			break;
		}
		setTextureName(texture);
		setCreativeTab(ThermalExpansion.tabItems);
		setUnlocalizedName(unlocalized);
		setMaxStackSize(1);
		setMaxDamage(101);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		if (slot == 2)
			return "enderiumpowerarmor:textures/armor/enderium_2.png";
		return "enderiumpowerarmor:textures/armor/enderium_1.png";
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return false;
	}

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate)
	{
		if (container.stackTagCompound == null)
		{
			container.stackTagCompound = new NBTTagCompound();
		}
		int energy = container.stackTagCompound.getInteger("energy");
		int energyReceived = Math.min(CAPACITY - energy, Math.min(this.CHARGE_SPEED, maxReceive));
		if (!simulate)
		{
			energy += energyReceived;
			container.stackTagCompound.setInteger("energy", energy);
		}
		container.setItemDamage(getDamageFromEnergy(container.stackTagCompound, container.getMaxDamage()));
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
	{
		return 0;
	}

	@Override
	public int getEnergyStored(ItemStack container)
	{
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("energy"))
			return 0;
		return container.stackTagCompound.getInteger("Energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return CAPACITY;
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
	{
		if (armor.getTagCompound().getInteger("energy") <= 0)
			return new ArmorProperties(0, 0.25, 0);

		if (source == DamageSource.fall)
		{
			switch(slot)
			{
			case 0:
				return new ArmorProperties(0, 1, 10000);
			default:
				return new ArmorProperties(0, 0, 10000);
			}
		}
		
		return new ArmorProperties(0, 0.25, 80);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
	{
		switch (type.ordinal())
		{
		case 0:
			return 3;
		case 1:
			return 8;
		case 2:
			return 6;
		case 3:
			return 3;
		default:
			return 0;
		}
	}

	private int getDamageFromEnergy(NBTTagCompound tag, int max)
	{
		return ((int) (Math.abs((float) tag.getInteger("energy") / CAPACITY - 1) * max)) + 1;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && stack.getTagCompound().getInteger("energy") > 0)
		{
			NBTTagCompound tag = stack.getTagCompound();

			int decrement = tag.getInteger("energy") - (getDamage(damage) * isFalling(source));
			tag.setInteger("energy", (decrement <= 0 ? 0 : decrement));
			stack.setItemDamage(getDamageFromEnergy(tag, stack.getMaxDamage()));

			stack.stackTagCompound = tag;
		}
	}
	
	private int isFalling(DamageSource source)
	{
		return source == DamageSource.fall ? 4 : 1;
	}

	private int getDamage(int damageAmount)
	{
		return (new Random().nextInt(DAMAGE_RAND) - (DAMAGE_RAND / 2) + (DAMAGE_BASE * damageAmount));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		int amnt = (par1ItemStack.getTagCompound() == null ? 0 : par1ItemStack.getTagCompound().getInteger("energy"));
		String fmt = "";
		if (Integer.toString(amnt).length() < 5)
			fmt += Integer.toString(amnt);
		else
		{
			switch (Integer.toString(amnt).length())
			{
			case 5:
				fmt = Integer.toString(amnt).substring(0, 2) + "." + Integer.toString(amnt).substring(2, 4) + "K";
				break;
			case 6:
				fmt = Integer.toString(amnt).substring(0, 3) + "." + Integer.toString(amnt).substring(3, 5) + "K";
				break;
			case 7:
				fmt = Integer.toString(amnt).substring(0, 1) + "." + Integer.toString(amnt).substring(1, 4) + "M";
				break;
			case 8:
				fmt = Integer.toString(amnt).substring(0, 2) + "M";
				break;
			}
		}
		par3List.add("Stored: " + fmt + " / 10M RF");
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		if (par1ItemStack.stackTagCompound == null)
			initTag(par1ItemStack);
	}

	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		initTag(par1ItemStack);
	}

	private void initTag(ItemStack stack)
	{
		NBTTagCompound tag = stack.getTagCompound();

		if (tag == null)
			tag = new NBTTagCompound();

		tag.setInteger("energy", 0);
		stack.setTagCompound(tag);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(this, 1, 101));
		ItemStack i = new ItemStack(this, 1, 1);
		i.stackTagCompound = new NBTTagCompound();
		i.stackTagCompound.setInteger("energy", 10000000);
		par3List.add(i);
	}

}
