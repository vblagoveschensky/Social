package social.controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * This filters trims all request parameters.
 */
@WebFilter(filterName = "TrimParametersFilter", urlPatterns = {"/*"})
public class TrimParametersFilter implements Filter {

    public TrimParametersFilter() {
    }

    /**
     * Exchanges the original request with new one with trimmed parameters.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
        } catch (ClassCastException exception) {
            chain.doFilter(request, response);
        }
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * This request wrapper class overrides the getParameter method to trim the
     * parameter.
     */
    class RequestWrapper extends HttpServletRequestWrapper {

        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            try {
                return super.getParameter(name).trim();
            } catch (NullPointerException exception) {
                return null;
            }
        }
    }
}
