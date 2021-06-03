package com.mmall.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
<mirrors>
	<mirror>
		<id>alimaven</id>
		<name>aliyun maven</name>
		<url>http://maven.aliyun.com/nexus/content/groups/public/</url>
		<mirrorOf>central</mirrorOf>
	</mirror>
</mirrors>
 */
@SpringBootApplication
@RestController
//@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RequestMapping("/")
	public String home() {
		return "hello spring boot";
	}

	@RequestMapping("/hello")
	public String hello() {
		return "hello world";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/roleAuth")
	public String role() {
		return "admin auth";
	}

	//test方法调用前进行权限检查，id小于10且用户名为当前用户的username且username为abc，传过来的user对象的username
	@PreAuthorize("#id<10 and principal.username.equals(#username) and #user.username.equals('ab')")
	@RequestMapping("/test")
	public String test(Integer id, String username, User user) {
		// ...
		return "test_auth";
	}

	//在调用test方法后进行权限检查，不能控制方法是否能被调用
	@PostAuthorize("returnObject%2==0")  //returnObject类可以取出当前的返回值，限制return的id为偶数
	public Integer test1(Integer id,String username,User uer){
		//...
		return id;
	}

	@PreFilter("filterObject%2==0")//对集合类型的参数进行过滤,得到只是偶数的List数据
	@PostFilter("filterObject%4==0")//对集合类型的返回值进行过滤，得到返回值能被4整除的List数据
	@RequestMapping("/test2")
	public List<Integer> test2(List<Integer> idList) {
		// ...
		return idList;
	}
}

