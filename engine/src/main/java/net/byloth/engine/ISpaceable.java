package net.byloth.engine;

import net.byloth.engine.core.IPositionable;
import net.byloth.engine.core.IRotatable;
import net.byloth.engine.core.IScalable;

/**
 * Created by Matteo on 05/01/2017.
 */

public interface ISpaceable extends IPositionable, IRotatable, IScalable
{
    @Override
    ISpaceable setXAxisPosition(double xAxisPositionValue);
    @Override
    ISpaceable setYAxisPosition(double yAxisPositionValue);
    @Override
    ISpaceable setZAxisPosition(double zAxisPositionValue);

    @Override
    ISpaceable setXAxisRotation(double xAxisRotationValue);
    @Override
    ISpaceable setYAxisRotation(double yAxisRotationValue);
    @Override
    ISpaceable setZAxisRotation(double zAxisRotationValue);

    @Override
    ISpaceable setScale(double scaleValue);
}
