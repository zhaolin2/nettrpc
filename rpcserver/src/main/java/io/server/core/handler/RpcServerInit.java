package io.server.core.handler;

import io.common.codec.*;
import io.common.serializer.JsonSerializer;
import io.common.serializer.Serializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcServerInit extends ChannelInitializer {

    private Map<String,Object> handlerMap;
    private Executor executor;
    public RpcServerInit(Map<String, Object> handlerMap, Executor executor) {
        this.handlerMap = handlerMap;
        this.executor =executor;
    }
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        Serializer serializer = JsonSerializer.getInstance();

        pipeline.addLast(new IdleStateHandler(0, 0, Beat.BEAT_TIMEOUT, TimeUnit.SECONDS));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        pipeline.addLast(new RpcDecoder(RpcRequest.class, serializer));
        pipeline.addLast(new RpcEncoder(RpcResponse.class, serializer));
        pipeline.addLast(new RpcServerHandler(handlerMap, executor));    }
}
