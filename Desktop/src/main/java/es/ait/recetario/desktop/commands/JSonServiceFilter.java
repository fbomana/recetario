/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands;

import javax.servlet.Filter;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class JSonServiceFilter implements Filter
{

    @Override
    public void init(FilterConfig fc) throws ServletException
    {
        
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fc) throws IOException, ServletException
    {
        if ( req instanceof HttpServletRequest && resp instanceof HttpServletResponse )
        {
            HttpServletRequest request = ( HttpServletRequest )req;
            if ( request.getRequestURI().startsWith("/services/"))
            {
                HttpServletResponse response = ( HttpServletResponse )resp;
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers")); 
            }
        }
        fc.doFilter(req, resp);
    }

    @Override
    public void destroy()
    {
        
    }


    
}
