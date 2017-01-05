#version 100

precision lowp float;

const float middleValue = 0.333;

uniform vec2 screenResolution;

uniform vec4 topColor;
uniform vec4 middleColor;
uniform vec4 bottomColor;

void main() // Tipo di ritorno: "vec4"
{
    float y;

    float colorWeight;

    vec4 firstColor;
    vec4 secondColor;

    vec4 finalColor;

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

    gl_FragColor = finalColor;

    // return gl_FragColor;
}
