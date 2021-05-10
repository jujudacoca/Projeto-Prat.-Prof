package api.avaliadin.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebController implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/").setViewName("login");
		registry.addViewController("/cadastro").setViewName("cadastro");
		registry.addViewController("/listaUser").setViewName("listaUser");
		registry.addViewController("/cadastroLivro").setViewName("cadastroLivro");
		registry.addViewController("/cadastroSerie").setViewName("cadastroSerie");
		registry.addViewController("/cadastroFilme").setViewName("cadastroFilme");
		registry.addViewController("/cadastroItem").setViewName("cadastroItem");
		registry.addViewController("/criaAvaliacao").setViewName("criaAvaliacao");
		registry.addViewController("/paginaitem/{id}").setViewName("paginaitem");
		registry.addViewController("/403").setViewName("403");
		registry.addViewController("/alterarCadastro").setViewName("alterarCadastro");
		
	}
}
