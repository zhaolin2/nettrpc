package io.server.impl;

import io.common.annotation.RpcService;
import io.common.inter.HelloService;

@RpcService(value = HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "replay:"+name;
    }
}
