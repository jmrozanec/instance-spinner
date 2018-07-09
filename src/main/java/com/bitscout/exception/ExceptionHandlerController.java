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
package com.bitscout.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/*
    Kudos to:
    Kudos to: http://stackoverflow.com/questions/24292373/spring-boot-rest-controller-how-to-return-different-http-status-codes
    http://stackoverflow.com/questions/23580509/how-to-write-a-proper-global-error-handler-with-spring-mvc-spring-boot
    https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
 */
@ControllerAdvice
public class ExceptionHandlerController {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }
        log.warn(String.format("Failed: %s", request.getRequestURL().toString()), e);
    }
}
