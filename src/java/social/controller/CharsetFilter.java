package social.controller;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Sets UTF-8 charset to request and response.
 */
@WebFilter(filterName = "CharsetFilter", urlPatterns = {"/*"},
        dispatcherTypes = {DispatcherType.ASYNC, DispatcherType.ERROR, DispatcherType.FORWARD,
            DispatcherType.INCLUDE, DispatcherType.REQUEST})
public class CharsetFilter implements Filter {

    private String encoding;

    public CharsetFilter() {
    }

    /**
     *
     * @param request
     * @param response
     * @param chain
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        encoding = "UTF-8";
    }

    @Override
    public void destroy() {

    }
}
