package lotr.common.network;

import java.io.IOException;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.*;
import lotr.common.quest.LOTRMiniQuest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class LOTRPacketMiniquest implements IMessage {
	public NBTTagCompound miniquestData;
	public boolean completed;

	public LOTRPacketMiniquest() {
	}

	public LOTRPacketMiniquest(NBTTagCompound nbt, boolean flag) {
		miniquestData = nbt;
		completed = flag;
	}

	@Override
	public void fromBytes(ByteBuf data) {
		try {
			miniquestData = new PacketBuffer(data).readNBTTagCompoundFromBuffer();
		} catch (IOException e) {
			FMLLog.severe("LOTR: Error reading miniquest data");
			e.printStackTrace();
		}
		completed = data.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf data) {
		try {
			new PacketBuffer(data).writeNBTTagCompoundToBuffer(miniquestData);
		} catch (IOException e) {
			FMLLog.severe("LOTR: Error writing miniquest data");
			e.printStackTrace();
		}
		data.writeBoolean(completed);
	}

	public static class Handler implements IMessageHandler<LOTRPacketMiniquest, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketMiniquest packet, MessageContext context) {
			if (!LOTRMod.proxy.isSingleplayer()) {
				EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
				LOTRPlayerData pd = LOTRLevelData.getData(entityplayer);
				LOTRMiniQuest miniquest = LOTRMiniQuest.loadQuestFromNBT(packet.miniquestData, pd);
				if (miniquest != null) {
					LOTRMiniQuest existingQuest = pd.getMiniQuestForID(miniquest.questUUID, packet.completed);
					if (existingQuest == null) {
						if (packet.completed) {
							pd.addMiniQuestCompleted(miniquest);
						} else {
							pd.addMiniQuest(miniquest);
						}
					} else {
						existingQuest.readFromNBT(packet.miniquestData);
					}
				}
			}
			return null;
		}
	}

}
