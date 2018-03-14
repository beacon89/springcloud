package cn.com.beacon.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by summer on 2017/5/11.
 */
@FeignClient(name = "cloud-service", fallback = HelloRemoteHystrix.class, decode404 = false)
public interface HelloRemote {

	@RequestMapping(value = "/hello")
	public String hello(@RequestParam(value = "name") String name);

}
