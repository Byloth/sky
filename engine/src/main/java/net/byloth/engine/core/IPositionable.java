package net.byloth.engine.core;

import net.byloth.engine.Vector2;
import net.byloth.engine.Vector3;

/**
 * Created by Matteo on 05/01/2017.
 */

public interface IPositionable
{
    float getXAxisPosition();
    IRotatable setXAxisPosition(double xAxisPositionValue);

    float getYAxisPosition();
    IRotatable setYAxisPosition(double yAxisPositionValue);

    float getZAxisPosition();
    IRotatable setZAxisPosition(double zAxisPositionValue);
}
