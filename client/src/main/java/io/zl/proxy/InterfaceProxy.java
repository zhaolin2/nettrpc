package io.zl.proxy;

import io.common.codec.RpcRequest;
import io.common.codec.RpcResponse;
import io.zl.connect.ConnectManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class InterfaceProxy<T> implements InvocationHandler {

    private Class<T> interfaceClazz;


    InterfaceProxy(Class<T> clazz){
        this.interfaceClazz=clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        RpcResponse response = ConnectManager.getInstance().sendRequest(request);

        return response.getResult();
    }
}
