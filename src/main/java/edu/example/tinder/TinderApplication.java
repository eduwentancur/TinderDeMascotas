package edu.example.tinder;

import edu.example.tinder.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TinderApplication extends WebSecurityConfigurerAdapter{
        
        @Autowired
        private UsuarioServicio usuarioServico;
        
	public static void main(String[] args) {
		SpringApplication.run(TinderApplication.class, args);
	}
        
        @Autowired
        public void confiureGlobal(AuthenticationManagerBuilder auth) throws Exception{
            auth.userDetailsService(usuarioServico).passwordEncoder(new BCryptPasswordEncoder());
        }
        
}
