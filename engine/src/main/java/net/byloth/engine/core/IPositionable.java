package net.byloth.engine.core;

import net.byloth.engine.Vector2;
import net.byloth.engine.Vector3;

/**
 * Created by Matteo on 05/01/2017.
 */

public interface IPositionable
{
    float getXAxisPosition();
    IPositionable setXAxisPosition(double xAxisPositionValue);

    float getYAxisPosition();
    IPositionable setYAxisPosition(double yAxisPositionValue);

    float getZAxisPosition();
    IPositionable setZAxisPosition(double zAxisPositionValue);

    Vector3 getPosition();
    IPositionable setPosition(Vector2 positionValues);
    IPositionable setPosition(Vector3 positionValues);
}
