package net.byloth.engine.graphics.opengl;

import net.byloth.engine.ISpaceable;

/**
 * Created by Matteo on 31/03/2017.
 */

public abstract class ParticleGLView extends GLView
{
    private Particle[] particles;

    // TODO #1: Terminare l'implmentazione di questa classe.
    //          E' necessario capire come rappresentare la classe
    //           che dovrà gestire, nella maniera più ottimale,
    //           il rendering e l'update di molte particelle
    //           contemporaneamente senza andre a sovraccaricare
    //           troppo il dispositivo, inutilmente.

    public abstract class Particle implements ISpaceable
    {
        // TODO #2: Convertire questa classe in un'interfaccia?
    }
}
