package com.madhax.oauthdemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    public static final String USER_USERNAME = "john.carnell";
    public static final String USER_PASSWORD = "password1";
    public static final String ADMIN_USERNAME = "william.woodward";
    public static final String ADMIN_PASSWORD = "password2";
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    private final String GET_USER_AUTHORITIES_QUERY = "select email, from auth_user_authorities aua join auth_user on auth_user.id=user_id join authority on authority.id=authorities_id where email = ?";

    private DataSource dataSource;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfigurer(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    @Bean
    @Qualifier("myUserDetailsService")
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .passwordEncoder(passwordEncoder)
//                .withUser(USER_USERNAME)
//                .password(
//                        passwordEncoder.encode(USER_PASSWORD)
//                )
//                .roles(USER_ROLE)
//                .and()
//                .withUser(ADMIN_USERNAME)
//                .password(
//                        passwordEncoder.encode(ADMIN_PASSWORD)
//                )
//                .roles(USER_ROLE, ADMIN_ROLE);

        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT email, password, enabled FROM auth_user WHERE email = ?")
                .authoritiesByUsernameQuery("select email, name from auth_user_authorities aua join auth_user on auth_user.id=user_id join authority on authority.id=authorities_id where email = ?");


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
