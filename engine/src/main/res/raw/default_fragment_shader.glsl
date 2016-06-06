#version 100

// Precisione di calcolo, da parte della GPU, dei "float".
//
//  - "highp" per la posizione dei vertici (non sempre supportato).
//  - "mediump" per le coordinate delle texture.
//  - "lowp" per i colori.

precision mediump float;

uniform vec4 color;
uniform sampler2D texture;

varying vec2 v_TextureCoords;

void main() // Tipo di ritorno: "vec4"
{
    gl_FragColor = texture2D(texture, v_TextureCoords); // color * texture2D(texture, v_TextureCoords);

    // return gl_FragColor;
}