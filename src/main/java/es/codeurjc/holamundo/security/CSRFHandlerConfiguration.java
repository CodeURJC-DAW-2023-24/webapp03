package es.codeurjc.holamundo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CSRFHandlerConfiguration implements WebMvcConfigurer{

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new CSRFHandlerInterceptor());
    }
}

class CSRFHandlerInterceptor implements HandlerInterceptor{

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
    final ModelAndView modelAndView) throws Exception {
        
        if(modelAndView != null){
            CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
            if (token != null) {
                modelAndView.addObject("token", token.getToken());
            }
        }
    }
}
