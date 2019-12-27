package org.atos.commutermap.network.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
@Component
public class CorsFilter implements Filter {

    public static final Logger LOGGER = LoggerFactory.getLogger(CorsFilter.class);

    public CorsFilter() {
        LOGGER.info("CORS Filter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            LOGGER.debug("CORS header has been set: {}", ((HttpServletResponse) response).getHeaderNames());
        }
        chain.doFilter(request, response);
    }
}
