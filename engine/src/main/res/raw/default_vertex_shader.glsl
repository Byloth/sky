#version 100

// Precisione di calcolo, da parte della GPU, dei "float".
//
//  - "highp" per la posizione dei vertici (non sempre supportato).
//  - "mediump" per le coordinate delle texture.
//  - "lowp" per i colori.

precision mediump float;

attribute vec2 textureCoords;
attribute vec4 position;

uniform mat4 mvpMatrix;

varying vec2 v_TextureCoords;

void main() // Tipo di ritorno "vec4"
{
    v_TextureCoords = textureCoords;

    gl_Position = mvpMatrix * position;

    // return gl_Position;
}