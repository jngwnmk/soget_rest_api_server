package soget.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter{

    @Override 
    public void configure(HttpSecurity http) throws Exception {
         // @formatter:off
         http
         .authorizeRequests().antMatchers("/user/register").permitAll()
         .and()
         .authorizeRequests().antMatchers("/manager").permitAll()
         .and()
         .requestMatchers().antMatchers("/**")    
         .and()
         .authorizeRequests()
         .anyRequest().access("#oauth2.hasScope('write')");
         
          //[4]
         // @formatter:on
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
         resources.resourceId("soget");
    }
}