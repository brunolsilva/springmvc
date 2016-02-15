package br.com.casadocodigo.loja.conf;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

public class CustomFilter extends AbstractAuthenticationProcessingFilter {

	protected CustomFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {
		
		if(StringUtils.isEmpty(((HttpServletRequest)request).getHeader("TESTE"))) {
			filterChain.doFilter(request, response);
            return;
        }

        //On success keep going on the chain
        this.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            	filterChain.doFilter(request, response);
            }
        });
		
		
		super.doFilter(request, response, filterChain);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		String tokenValue = request.getHeader("TESTE");
		
		System.out.println(tokenValue);

        if(StringUtils.isEmpty(tokenValue)) {
           return null;
        }


        CustomAuthenticationToken token = new CustomAuthenticationToken(tokenValue);
        token.setDetails(authenticationDetailsSource.buildDetails(request));
        
        System.out.println(token.getDetails());
        
        System.out.println("oioadddisdoai");
        
        System.out.println(this.getAuthenticationManager().getClass());
        
        this.getAuthenticationManager().authenticate(token);
        
        System.out.println("aaaaggga");

        return this.getAuthenticationManager().authenticate(token);
	}

}
