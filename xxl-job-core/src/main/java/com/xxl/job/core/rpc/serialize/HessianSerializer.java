package com.xxl.job.core.rpc.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * hessian serialize
 *
 * @author xuxueli 2015-9-26 02:53:29
 */
public class HessianSerializer {

    private static final Logger logger = LoggerFactory.getLogger(HessianSerializer.class);

    public static <T> byte[] serialize(T obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output ho = new Hessian2Output(os);

        try {
            ho.writeObject(obj);
            ho.flush();
            return os.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            try {
                ho.close();
            } catch (IOException ignored) {
            }
            try {
                os.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        Hessian2Input hi = new Hessian2Input(is);

        try {
            return hi.readObject();
        } catch (IOException e) {
            logger.warn(">>>>>>>>>>> xxl-rpc deserialize failed, try use hessian1.");
            return hessianDeserialize(bytes, clazz);
        } finally {
            try {
                hi.close();
            } catch (Exception ignored) {
            }

            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }

    private static <T> Object hessianDeserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        HessianInput hi = new HessianInput(is);

        try {
            return hi.readObject();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            try {
                hi.close();
            } catch (Exception ignored) {
            }
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }
}
