package tterrag.epa.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class EPAEventHandler
{
	@ForgeSubscribe
	public void handleTooltip(ItemTooltipEvent event)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Minecraft.getMinecraft().currentScreen instanceof GuiCrafting)
		{
			GuiCrafting gui = (GuiCrafting) Minecraft.getMinecraft().currentScreen;
			ItemStack stackInResult = (ItemStack) gui.inventorySlots.getSlot(0).getStack();
			if (stackInResult != null && stackInResult.getItem() == event.itemStack.getItem())
				event.toolTip.add("WARNING: Stored energy will be lost");
		}
	}
}
