package io.common.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import io.common.constant.CommonOptions;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ServiceUtils {
    public static final String SERVICE_CONCAT_TOKEN = "#";

    public static String buildServiceKey(String interfaceName) {
        if (interfaceName.contains(".")){
            List<String> names = Splitter.on(".").splitToList(interfaceName);
            return names.get(names.size()-1);
        }
        return interfaceName;
    }

    public  static String basePath = CommonOptions.ZK_SERVICE_PATH;


    public static String tryGetPath(String serviceName,String registryAddress){
        registryAddress="["+registryAddress+"]";
        return Joiner.on("/").join(basePath,serviceName,registryAddress);
    }

    public static List<String> tryGetTargetInfo(List<String> paths){

        return paths.stream()
                .map(path -> RegexMatchers.findFirstMatchGroup("\\[(.+)\\]\\d+", path))
                .collect(Collectors.toList());
    }
}
