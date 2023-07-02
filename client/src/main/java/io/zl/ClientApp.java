package io.zl;

import io.common.constant.CommonOptions;
import io.common.inter.HelloService;
import io.zl.connect.ConnectManager;
import io.zl.proxy.ServiceProxy;

/**
 * Hello world!
 *
 */
public class ClientApp
{
    public static void main( String[] args ) throws InterruptedException {
        //主要应该是服务代理的实现 stub
        ServiceProxy serviceProxy = new ServiceProxy(CommonOptions.registryAddress);
        HelloService service = serviceProxy.createService(HelloService.class);
        String hello = service.hello("123");
        System.out.println("response:"+hello);

        ConnectManager.getInstance().close();
    }
}
