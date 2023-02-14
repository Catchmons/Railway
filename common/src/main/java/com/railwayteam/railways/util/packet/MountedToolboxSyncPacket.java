package com.railwayteam.railways.util.packet;

import com.railwayteam.railways.content.conductor.ConductorEntity;
import com.railwayteam.railways.multiloader.environment.Env;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MountedToolboxSyncPacket extends SimplePacketBase {
  final int id;
  final CompoundTag nbt;

  public MountedToolboxSyncPacket(Entity target, CompoundTag nbt) {
    this.id = target.getId();
    this.nbt = nbt;
  }

  public MountedToolboxSyncPacket(FriendlyByteBuf buf) {
    id = buf.readInt();
    nbt = buf.readNbt();
  }

  @Override
  public void write(FriendlyByteBuf buffer) {
    buffer.writeInt(this.id);
    buffer.writeNbt(this.nbt);
  }

  @Override
  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(()-> Env.CLIENT.runIfCurrent(()-> ()-> this.__handle(context)));
    context.get().setPacketHandled(true);
  }

  @Environment(EnvType.CLIENT)
  private void __handle(Supplier<NetworkEvent.Context> supplier) {
    Level level = Minecraft.getInstance().level;
    if (level != null) {
      Entity target = level.getEntity(this.id);
      if (target instanceof ConductorEntity conductor) {
        conductor.getOrCreateToolboxHolder().read(this.nbt, true);
      }
    }
  }
}
