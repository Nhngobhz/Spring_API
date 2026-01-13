package com.setec.confi;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class MyConfi implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/Static/**")
				.addResourceLocations("file:./myApps/Static/");
	}
	
	@Bean
    public OpenAPI customOpenAPI() {
        // Explicitly define the server URL for Swagger UI API calls
    
        Server productionServer = new Server();
        productionServer.setUrl("https://spring-api-zyxk.onrender.com"); // Set to your production server URL
        productionServer.setDescription("Production Server");

        // You can add other servers (e.g., a local HTTP one for development)
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development Server");

        return new OpenAPI()
                .servers(List.of(productionServer, devServer))
                // Add other configurations like info, security schemes, etc.
                ;
    }
}
