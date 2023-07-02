package io.server.core.handler;

import io.common.codec.RpcRequest;
import io.common.codec.RpcResponse;
import io.common.util.ServiceUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    private final Map<String, Object> handlerMap;
    private final Executor serverHandlerPool;

    public RpcServerHandler(Map<String, Object> handlerMap, final Executor threadPoolExecutor) {
        this.handlerMap = handlerMap;
        this.serverHandlerPool = threadPoolExecutor;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        if (RpcRequest.BEAT.getRequestId().equals(rpcRequest.getRequestId())) {
            logger.info("ping message");
            return;
        }

        serverHandlerPool.execute(() -> {
            RpcResponse response = buildResponse(rpcRequest);

            ctx.writeAndFlush(response).addListener((future) -> {
                logger.info("消息发送成功,[future:{}]", rpcRequest.getRequestId());
            });
        });

    }

    private RpcResponse buildResponse(RpcRequest rpcRequest) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());

        String serviceKey = ServiceUtils.buildServiceKey(rpcRequest.getClassName());
        Object target = handlerMap.getOrDefault(serviceKey, null);
        if (Objects.isNull(target)) {
            response.setErrorMessage("not find handler");
            response.setResult(null);
        } else {
            Object result = null;
            try {
                result = invokeHandler(rpcRequest, target);
            } catch (InvocationTargetException e) {
                logger.warn("调用目标方法错误,[]");
            }
            response.setResult(result);
        }
        return response;
    }

    private Object invokeHandler(RpcRequest request, Object target) throws InvocationTargetException {
        Class<?> targetClass = target.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        FastClass fastClass = FastClass.create(targetClass);
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);
        return fastClass.invoke(methodIndex, target, parameters);
    }
}
