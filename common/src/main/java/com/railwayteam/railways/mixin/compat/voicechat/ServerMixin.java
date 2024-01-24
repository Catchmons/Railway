package com.railwayteam.railways.mixin.compat.voicechat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.railwayteam.railways.annotation.ConditionalMixin;
import com.railwayteam.railways.compat.Mods;
import com.railwayteam.railways.content.conductor.ConductorPossessionController;
import de.maxhenkel.voicechat.voice.server.Server;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.UUID;

@ConditionalMixin(mods = Mods.VOICECHAT)
@Mixin(Server.class)
public class ServerMixin {
    @SuppressWarnings("unused")
    @WrapOperation(method = "processProximityPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;position()Lnet/minecraft/world/phys/Vec3;"))
    private static Vec3 useConductorSpyPosition(ServerPlayer instance, Operation<Vec3> original) {
        if (ConductorPossessionController.isPossessingConductor(instance)) {
            return instance.getCamera().position();
        } else {
            return original.call(instance);
        }
    }

    @SuppressWarnings("unused")
    @WrapOperation(method = "processProximityPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getUUID()Ljava/util/UUID;"),
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isCrouching()Z")
        ))
    private UUID useConductorSpyUUID(ServerPlayer instance, Operation<UUID> original) {
        if (ConductorPossessionController.isPossessingConductor(instance)) {
            return instance.getCamera().getUUID();
        } else {
            return original.call(instance);
        }
    }
}
