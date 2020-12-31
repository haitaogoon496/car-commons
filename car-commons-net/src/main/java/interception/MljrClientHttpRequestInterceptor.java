package interception;

import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * mljr 私有拦截器接口，目的为了避免 eureka 默认拦截器的注入，使用此包配置 RestTemplate 均实现此接口
 * @author lingyu.shang
 */
public interface MljrClientHttpRequestInterceptor extends ClientHttpRequestInterceptor {
}
