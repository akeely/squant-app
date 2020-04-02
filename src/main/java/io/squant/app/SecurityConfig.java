package io.squant.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
         .antMatchers("/actuator/*").permitAll()
         .anyRequest().authenticated()
         .and()
         .oauth2Login();
    }


    @Bean
    public HttpFirewall httpFirewall() {

        return new HttpFirewallDelegate();
    }

    public static class HttpFirewallDelegate implements HttpFirewall {

        private static final Logger LOG = LoggerFactory.getLogger(HttpFirewallDelegate.class);

        private final HttpFirewall delegate = new StrictHttpFirewall();

        @Override
        public FirewalledRequest getFirewalledRequest(HttpServletRequest httpServletRequest)
                throws RequestRejectedException {

            try {
                return delegate.getFirewalledRequest(httpServletRequest);
            } catch (RequestRejectedException e) {
                LOG.error("Malicious URL: {}. Query string: {}", httpServletRequest.getRequestURL(),
                        httpServletRequest.getQueryString());
                throw e;
            }
        }

        @Override
        public HttpServletResponse getFirewalledResponse(HttpServletResponse httpServletResponse) {
            return delegate.getFirewalledResponse(httpServletResponse);
        }
    }
}
