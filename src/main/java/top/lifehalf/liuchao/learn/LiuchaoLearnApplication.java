package top.lifehalf.liuchao.learn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@RestController
@MapperScan("top.lifehalf.liuchao.learn.mapper")
//@EnableRedisHttpSession
public class LiuchaoLearnApplication {

    @GetMapping("/")
    public String hello() {
//        return "<a href='/logout'>注销<a/>" +
        return "<a href='/csrfLogout.html'>注销<a/>" +
                "hello, spring security" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @GetMapping("/session/invalid")
    public String sessionInvalid() {
        return "session invalid";
    }

    public static void main(String[] args) {
        SpringApplication.run(LiuchaoLearnApplication.class, args);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password("123").roles("USER").build());
        manager.createUser(User.withUsername("admin").password("123").roles("USER", "ADMIN").build());
        return manager;
    }

//    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
