package cl.tomas.naivepay.security;

import cl.tomas.naivepay.security.filter.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cl.tomas.naivepay.service.access.AccessService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccessService accessService;

    @Value("${spring.jwt.secret}")
    private String secret;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomDeniedHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accessService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        authorizationFilter.setJwtSecret(secret);

        http.cors().and().csrf().disable();
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/auth/login").permitAll();
        http.authorizeRequests().antMatchers("/auth/refresh-token").permitAll();
        http.authorizeRequests().antMatchers("/customers/get/{\\d+}").authenticated();
        http.authorizeRequests().antMatchers("/customers/update").authenticated();
        http.authorizeRequests().antMatchers("/customers/**").hasAnyAuthority("1");
        http.authorizeRequests().antMatchers("/accounts/get/{\\d+}").authenticated();
        http.authorizeRequests().antMatchers("/accounts/update").authenticated();
        http.authorizeRequests().antMatchers("/accounts/create").authenticated();
        http.authorizeRequests().antMatchers("/accounts/**").hasAnyAuthority("1");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class );
/*        http.addFilterBefore(new CorsFilter(corsConfigurationSource("*")), AbstractPreAuthenticatedProcessingFilter.class);*/
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/h2/**");
    }

}
