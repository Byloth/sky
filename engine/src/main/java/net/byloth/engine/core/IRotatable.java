package net.byloth.engine.core;

import net.byloth.engine.Vector3;

/**
 * Created by Matteo on 05/01/2017.
 */

public interface IRotatable
{
    float getXAxisRotation();
    IRotatable setXAxisRotation(double xAxisRotationValue);

    float getYAxisRotation();
    IRotatable setYAxisRotation(double yAxisRotationValue);

    float getZAxisRotation();
    IRotatable setZAxisRotation(double zAxisRotationValue);

    Vector3 getRotation();
    IRotatable setRotation(Vector3 rotationValues);
}
