package net.byloth.engine.graphics.opengl.helpers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Matteo on 26/03/2016.
 */
final public class GLES2Loader
{
    static final private String TAG = "GLES2Loader";

    private GLES2Loader() { }

    static public String loadTextResource(Context context, int resourceId)
    {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String nextLine;
        StringBuilder textResource = new StringBuilder();

        try
        {
            while ((nextLine = bufferedReader.readLine()) != null)
            {
                textResource.append(nextLine);
                textResource.append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return textResource.toString();
    }
}