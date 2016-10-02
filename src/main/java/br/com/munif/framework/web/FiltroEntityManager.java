package br.com.munif.framework.web;

import br.com.munif.framework.core.MunifPersistence;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author munif
 */
@WebFilter(filterName = "EntityManagerFilter", urlPatterns = {"/*"})
public class FiltroEntityManager implements Filter {

    private FilterConfig filterConfig = null;

    public FiltroEntityManager() {
        criaUsuarioPadrao();
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "Content-Type, token, Connection");//Access-Control-Allow-Headers:"Content-Type, token, Connection"
        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS,HEAD");//Access-Control-Allow-Methods:"POST, GET, PUT, DELETE, OPTIONS,HEAD"
        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");
        HttpServletRequest hsr = (HttpServletRequest) request;
        Throwable problem = null;
        long inicio = System.currentTimeMillis();
        EntityManager em = MunifPersistence.getInstance().getEntityManager();
        MunifPersistence.getInstance().ip.set(request.getRemoteAddr().toString());
        boolean canDo=true;
        try {
            em.getTransaction().begin();
            if (canDo) {
                chain.doFilter(request, response);
            } else {
                ((HttpServletResponse) response).setStatus(403);
                response.getOutputStream().write("Not allowed.".getBytes());
            }
            em.getTransaction().commit();
        } catch (Throwable t) {
            em.getTransaction().rollback();
            problem = t;
            t.printStackTrace();
        }
        MunifPersistence.getInstance().destroyEntityManager();
        long tempo = System.currentTimeMillis() - inicio;
        System.out.println("-----> TEMPO " + tempo + "ms para " + hsr.getRequestURI());
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
        //MunifPersistence.getInstance().destroy();
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("FiltroEntityManager()");
        }
        StringBuffer sb = new StringBuffer("FiltroEntityManager(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

    private void criaUsuarioPadrao() {
        EntityManager em = MunifPersistence.getInstance().getEntityManager();
        em.getTransaction().begin();
        em.getTransaction().commit();
    }

}
