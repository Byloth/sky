package net.byloth.engine.graphics.opengl;

import net.byloth.engine.ISpaceable;

import java.util.List;

/**
 * Created by Matteo on 31/03/2017.
 */

// TODO: Deve davvero estendere la classe TextureGLView?
public abstract class ParticleGLView<TParticle extends ParticleGLView.Particle> extends TextureGLView
{
    private List<TParticle> particles;

    @Override
    public ParticleGLView setTextureVertex(float[] textureVertex)
    {
        throw new UnsupportedOperationException();
    }
    public ParticleGLView setTextureVertex(float[][] textureVertex)
    {
        // TODO: ???

        return this;
    }

    // TODO #1: Terminare l'implmentazione di questa classe.
    //          E' necessario capire come rappresentare la classe
    //           che dovrà gestire, nella maniera più ottimale,
    //           il rendering e l'update di molte particelle
    //           contemporaneamente senza andre a sovraccaricare
    //           troppo il dispositivo, inutilmente.

    public abstract class Particle implements ISpaceable
    {
        // TODO #2: Convertire questa classe in un'interfaccia?
        public abstract int getType();
    }
}
