package com.springboot.employees.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String ROOT_PATTERN = "/api/v1/**";

	@Value("${app.public-key}")
	private String publicKey;
	
	
	@Override
	public void configure(final ResourceServerSecurityConfigurer resources) {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET, ROOT_PATTERN).access("#oauth2.hasScope('read')")
				.antMatchers(HttpMethod.POST, ROOT_PATTERN).access("#oauth2.hasScope('write')")
				.antMatchers(HttpMethod.PATCH, ROOT_PATTERN).access("#oauth2.hasScope('write')")
				.antMatchers(HttpMethod.PUT, ROOT_PATTERN).access("#oauth2.hasScope('write')")
				.antMatchers(HttpMethod.DELETE, ROOT_PATTERN).access("#oauth2.hasScope('write')")
				.antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/swagger-ui/**").permitAll()
				.antMatchers("/v3/api-docs/**").permitAll();
	}

	@Bean
	JwtAccessTokenConverter tokenEnhancer() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setVerifierKey(publicKey);
		return converter;
	}

    @Bean
    JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }
	
}