package io.gitlab.dwarfyassassin.lotrucp.client.util;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class FakeArmorStandEntity extends EntityLivingBase {
	public static FakeArmorStandEntity INSTANCE = new FakeArmorStandEntity();

	public FakeArmorStandEntity() {
		super(FMLClientHandler.instance().getWorldClient());
	}

	@Override
	public ItemStack getEquipmentInSlot(int p_71124_1_) {
		return null;
	}

	@Override
	public ItemStack getHeldItem() {
		return null;
	}

	@Override
	public ItemStack[] getLastActiveItems() {
		return null;
	}

	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
	}
}
