package br.com.casadocodigo.loja.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import br.com.casadocodigo.loja.daos.UserDAO;
import br.com.casadocodigo.loja.models.User;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private UserDAO userDAO;

	@Autowired
	public CustomAuthenticationProvider(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		System.out.println("jnfskdjfnsd");
		CustomAuthenticationToken token = (CustomAuthenticationToken)authentication;

        try{
           //Authenticate token against redis or whatever you want

            //This i found weird, you need a Principal in your Token...I use User
            //I found this to be very redundant in spring security, but Controller param resolving will break if you don't do this...anoying
            User user = (User)userDAO.loadUserByUsername("bruno.silvagj@gmail.com");

            //Our token resolved to a username so i went with this token...you could make your CustomToken take the principal.
        	//getCredentials returns "NO_PASSWORD"..it gets cleared out anyways.
        	//also the getAuthenticated for the thing you return should return true now
            return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
        } catch(Exception e){
        	e.printStackTrace();
            throw new BadCredentialsException("TESTES",e);
        }
	}

	@Override
	public boolean supports(Class<?> authentication) {
		System.out.println("fnsdkfjndfkj");
//		return CustomAuthenticationToken.class.isAssignableFrom(authentication);
		return true;
	}

}
