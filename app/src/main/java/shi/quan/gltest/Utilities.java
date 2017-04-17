package shi.quan.gltest;

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
}
