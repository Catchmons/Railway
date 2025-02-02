package com.railwayteam.railways.registry;

import com.google.common.collect.ImmutableSet;
import com.railwayteam.railways.content.distant_signals.SignalDisplaySource;
import com.railwayteam.railways.mixin.AccessorBlockEntityType;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

import static com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours.assignDataBehaviour;

public class CRExtraRegistration {
    public static boolean registeredSignalSource = false;
    public static boolean registeredVentAsCopycat = false;

    // register the source, working independently of mod loading order
    public static void register() {
        Block maybeRegistered;
        BlockEntityType<?> maybeRegisteredCopycat;
        try {
            maybeRegistered = AllBlocks.TRACK_SIGNAL.get();
        } catch (NullPointerException ignored) {
            maybeRegistered = null;
        }
        try {
            maybeRegisteredCopycat = AllBlockEntityTypes.COPYCAT.get();
        } catch (NullPointerException ignored) {
            maybeRegisteredCopycat = null;
        }
        Create.REGISTRATE.addRegisterCallback("track_signal", Registry.BLOCK_REGISTRY, CRExtraRegistration::addSignalSource);
        Create.REGISTRATE.addRegisterCallback("copycat", Registry.BLOCK_ENTITY_TYPE_REGISTRY, CRExtraRegistration::addVentAsCopycat);
        if (maybeRegistered != null) {
            addSignalSource(maybeRegistered);
        }
        if (maybeRegisteredCopycat != null) {
            addVentAsCopycat(maybeRegisteredCopycat);
        }
    }

    private static void addVentAsCopycat(BlockEntityType<?> object) {
        if (registeredVentAsCopycat) return;
        Block ventBlock;
        try {
            ventBlock = CRBlocks.CONDUCTOR_VENT.get();
        } catch (NullPointerException ignored) {
            return;
        }
        Set<Block> validBlocks = ((AccessorBlockEntityType) object).getValidBlocks();
        validBlocks = new ImmutableSet.Builder<Block>()
                .add(validBlocks.toArray(Block[]::new))
                .add(ventBlock)
                .build();
        ((AccessorBlockEntityType) object).setValidBlocks(validBlocks);
        registeredVentAsCopycat = true;
    }

    public static void addSignalSource(Block block) {
        if (registeredSignalSource) return;
        assignDataBehaviour(new SignalDisplaySource()).accept(block);
        registeredSignalSource = true;
    }
}
