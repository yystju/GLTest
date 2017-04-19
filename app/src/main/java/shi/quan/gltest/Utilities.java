package shi.quan.gltest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES31;
import android.opengl.GLUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by quan on 17/4/17.
 */

public class Utilities {
    public static String loadStringFromInputStream(InputStream ins) throws IOException {
        String result = null;

        if(ins != null) {
            ByteArrayOutputStream bouts = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024 * 8];

            int len = -1;

            while(-1 != (len = ins.read(buffer))) {
                bouts.write(buffer, 0, len);
            }

            result = new String(bouts.toByteArray());
        }

        return result;
    }

    public static FloatBuffer wrapBuffer(float[] arry) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(arry.length * (Float.SIZE / Byte.SIZE));
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = buffer.asFloatBuffer();

        floatBuffer.put(arry);
        floatBuffer.position(0);

        return floatBuffer;
    }

    public static int loadTexture(final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];

        GLES31.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle[0]);

            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_NEAREST);
            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE);
            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE);

            GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0);

            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    public static int loadTexture(InputStream stream)
    {
        final int[] textureHandle = new int[1];

        GLES31.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            final Bitmap bitmap = BitmapFactory.decodeStream(stream);

            GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle[0]);

            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_NEAREST);
            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
//            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE);
//            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE);

            GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0);

            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    public static String dumpMatrix(float[] matrix) {
        String str = "";

        for(int i = 0; i < matrix.length; ++i) {
            if(i != 0) str += ",";
            if(i % 4 == 0) str += "\n";
            str += ("" + matrix[i]);
        }

        str += "";

        return str;
    }
}
