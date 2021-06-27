package com.qohash.codeexam;
import com.qohash.codeexam.microservices.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/***
 * This application run with JAVA command, if the command line included parameters, then the program will
 * automatically check the default root folder from the parameters and generate output file.
 */

@SpringBootApplication
public class CodeexamApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configrable = SpringApplication.run(CodeexamApplication.class);
		if (args.length > 0) {
			FileService fileService = configrable.getBean(FileService.class);
			for (String arg : args) {
				if (arg.startsWith("--root")) {
					fileService.DEFAULT_ROOT = arg.split("=")[1];
				}
				if (arg.startsWith("--output")) {
					fileService.DEFAULT_OUTPUT = arg.split("=")[1];
				}
			}
			fileService.saveDefaultFoldersAndFilesList();
		}
	}
	@Bean
	public WebFluxConfigurer corsConfigurer() {
		return new WebFluxConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/*")
						.allowedOrigins("http://localhost:8100")
						.allowedMethods("GET", "POST");
			}
		};
	}
}
