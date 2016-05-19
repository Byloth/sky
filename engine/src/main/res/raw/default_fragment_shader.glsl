#version 100

// Precisione di calcolo, da parte della GPU, dei "float".
//
//  - "highp" per la posizione dei vertici (non sempre supportato).
//  - "mediump" per le coordinate delle texture.
//  - "lowp" per i colori.

precision lowp float;

uniform vec4 color;

void main() // Tipo di ritorno: "vec4"
{
    gl_FragColor = color;

    // return gl_FragColor;
}