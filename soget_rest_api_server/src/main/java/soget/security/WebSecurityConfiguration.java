package soget.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import soget.repository.UserRepository;

@Configuration
public class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	UserRepository userRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService());
	}

	@Bean
	UserDetailsService userDetailsService() {
		
		return new UserDetailsService(){

			@Override
			public UserDetails loadUserByUsername(String username)
					throws UsernameNotFoundException {
				soget.model.User account = userRepository.findByUserId(username);
				if(account !=null){
					return new User(account.getUserId(),account.getPassword(),true,true,true,true,AuthorityUtils.createAuthorityList("USER","write"));
				} else {
					throw new UsernameNotFoundException("could not find the user '"+username+"'");
				}
			}};
	}
}
