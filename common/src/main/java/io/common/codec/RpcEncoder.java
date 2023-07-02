package io.common.codec;

import io.common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcEncoder extends MessageToByteEncoder {

    Logger logger = LoggerFactory.getLogger(RpcEncoder.class);

    private final Class<?> genericClass;

    private final Serializer serializer;

    public RpcEncoder(Class<?> genericClass, Serializer serializer){
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf out) throws Exception {
        if (genericClass.isInstance(o)){
            try {
                byte[] bytes = serializer.serialize(o);
                out.writeInt(bytes.length);
                out.writeBytes(bytes);
            } catch (Exception e) {
                logger.warn("写入错误,[o:{}]",o);
            }
        }
    }
}
