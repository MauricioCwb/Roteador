package Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import Beans.Mbean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan
public class Application {
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public Docket api() { 
    	@SuppressWarnings("deprecation")
		ApiInfo apiInfo = new ApiInfo(
				"Rotas Admin",
				"Administra lista Roteiros",
				"Versão API 1.0",
				"Termos de uso",
				"mauriciocwb@gmail.com",
				"API License",
				"/roteiros"
				);
        
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("localhost:8080"))
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(apiInfo);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    	return args -> {

    		String pathArquivo;
    		if(args.length <= 1 || "".equals(args[1])) {
    			pathArquivo = "C:\\Projetos\\Roteador\\src\\main\\java\\backend\\src\\main\\java\\Resource\\input-routes.csv";
    		} else {
    			pathArquivo = args[1];
    		}

    		Mbean mbean = new Mbean();
    		mbean.inicializar(pathArquivo);
    	};
    }

}