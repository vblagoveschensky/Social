package social.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 *
 * @author Владимир
 */
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

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {

        return ("CharsetFilter()");

    }

    @Override
    public void init(FilterConfig filterConfig) {
        encoding = "UTF-8";
    }

    @Override
    public void destroy() {

    }

}
