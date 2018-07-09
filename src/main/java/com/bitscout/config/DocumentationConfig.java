/*
 * Copyright 2017 jmrozanec
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bitscout.config;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile({"prod", "dev"})
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
/*
    Kudos to: http://fizzylogic.nl/2015/07/29/quickly-generate-api-docs-for-your-spring-boot-application-using-springfox/
*/
public class DocumentationConfig {
    private static String title = "Instance Spinner";
    private static String description = "Instance Spinner REST API";
    private static String version = "0.1.0";
    private static String termsOfServiceUrl = "This code is made available with an Apache 2.0 license";
    private static String contact = "jmrozanec@github";
    private static String license = "Apache 2.0";
    private static String licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0";

    @Bean
    public Docket documentation() {
        //Swagger enable: Kudos to: http://stackoverflow.com/questions/27442300/disabling-swagger-with-spring-mvc
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .groupName("instance-spinner-rest-api")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/rest/**"))
                .build()
                .apiInfo(metadata());
    }

    @VisibleForTesting
    ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .contact(contact)
                .termsOfServiceUrl(termsOfServiceUrl)
                .build();
    }
}
