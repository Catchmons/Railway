package com.railwayteam.railways.content.custom_bogeys;

import com.railwayteam.railways.registry.CRBogeyStyles;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import net.minecraft.world.phys.Vec3;

public class TripleAxleBogeyBlock extends CRBogeyBlock {
    public TripleAxleBogeyBlock(Properties props) {
        super(props, CRBogeyStyles.HEAVYWEIGHT, BogeySizes.SMALL);
    }

    @Override
    public Vec3 getConnectorAnchorOffset() {
        return new Vec3(0, 6 / 32f, 42 / 32f);
    }

    @Override
    public double getWheelPointSpacing() {
        return 3;
    }
}
