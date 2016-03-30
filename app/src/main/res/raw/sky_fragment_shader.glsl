#version 100

// Precisione di calcolo, da parte della GPU, dei "float".
//
//  - "highp" per la posizione dei vertici (non sempre supportato).
//  - "mediump" per le coordinate delle texture.
//  - "lowp" per i colori.

precision lowp float;

const float middleValue = 0.333;

uniform vec2 screenResolution;

uniform vec3 topColor;
uniform vec3 middleColor;
uniform vec3 bottomColor;

void main() // Tipo di ritorno: "vec4"
{
    float y;

    float colorWeight;

    vec3 firstColor;
    vec3 secondColor;

    vec3 finalColor;

    y = gl_FragCoord.y / screenResolution.y;

    if (y < middleValue)
    {
        firstColor = bottomColor;
        secondColor = middleColor;

        colorWeight = y / middleValue;
    }
    else
    {
        firstColor = middleColor;
        secondColor = topColor;

        colorWeight = (y - middleValue) / (1.0 - middleValue);
    }

    finalColor = firstColor * (1.0 - colorWeight) + secondColor * colorWeight;

    gl_FragColor = vec4(finalColor, 1.0);

    // return gl_FragColor;
}
