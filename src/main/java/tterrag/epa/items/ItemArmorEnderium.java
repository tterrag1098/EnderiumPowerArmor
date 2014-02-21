package tterrag.epa.items;

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

public class ItemArmorEnderium extends ItemArmor implements IEnergyContainerItem, ISpecialArmor {
    protected int capactiy, maxReceive, maxExtract;
    public static enum ArmorType {
        HELMET,
        CHESTPLATE,
        LEGS,
        BOOTS;
    }

    public ItemArmorEnderium(int id, EnumArmorMaterial material, ArmorType type) {
        super(id, material, 0, type.ordinal());
        String texture = "", unlocalized = "";
        switch (type) {
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
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (slot == 2)
            return "enderiumpowerarmor:textures/armor/enderium_2.png";
        return "enderiumpowerarmor:textures/armor/enderium_1.png";
    }
    
    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }
    
    public ItemArmorEnderium setCapacity(int capacity) {
        this.capactiy = capacity;
        return this;
    }
    
    public void setMaxRecieve(int maxRecieve) {
        this.maxReceive = maxRecieve;
    }
    
    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }
    
    public void setMaxTransfer(int maxTransfer) {
        setMaxExtract(maxTransfer);
        setMaxRecieve(maxTransfer);
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (container.stackTagCompound == null)
            container.stackTagCompound = new NBTTagCompound();
        int energy = container.stackTagCompound.getInteger("Energy");
        int energyRecieved = Math.min(capactiy - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            energy += energyRecieved;
            container.stackTagCompound.setInteger("Energy", energy);
        }
        return energyRecieved;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy"))
            return 0;
        int energy = container.stackTagCompound.getInteger("Energy");
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            energy -= energyExtracted;
            container.stackTagCompound.setInteger("Energy", energy);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy"))
            return 0;
        return container.stackTagCompound.getInteger("Energy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return capactiy;
    }
    
    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        if (source.isUnblockable())
            return new ISpecialArmor.ArmorProperties(0, 0.0D, 0);
        double absorptionRatio = getBaseAbsorptionRatio() * 0.9D;
        int energyPerDamage = 800;
        int damageLimit = 25 * getEnergyStored(armor) / energyPerDamage;
        return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
    }
    
    
    private double getBaseAbsorptionRatio() {
        switch (armorType) {
        case 0:
            return 0.15D;
        case 1:
            return 0.4D;
        case 2:
            return 0.3D;
        case 3:
            return 0.15D;
        }
        return 0.0D;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        if (getEnergyStored(armor) >= 800)
            return (int)Math.round(20.0D * getBaseAbsorptionRatio() * 0.9D);
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        extractEnergy(stack, damage * 800, false);
    }

}
