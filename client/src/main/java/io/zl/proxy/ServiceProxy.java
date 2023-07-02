package io.zl.proxy;

import java.lang.reflect.Proxy;

public class ServiceProxy {

    private String remoteAddress;

    public ServiceProxy(String registryAddress){
        this.remoteAddress = registryAddress;
    }

    public <T,P> T createService(Class<T> interfaceClass){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},new InterfaceProxy(interfaceClass));
    }


}
