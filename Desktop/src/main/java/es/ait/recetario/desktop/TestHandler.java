/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop;

import es.ait.recetario.desktop.handlers.HandlerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 *
 * @author aitkiar
 */
public class TestHandler extends AbstractHandler
{

    @Override
    public void handle(String string, Request rqst, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        rqst.setHandled( true );
        PrintWriter out = response.getWriter();
        try
        {
//            HandlerFactory.getHandler(string).handle( string, request, response, out );
//            out.println("<!DOCTYPE html><html>");
//            out.println("<head></head>");
//            out.println("<body>");
//            out.println("Texto de respuesta a la petición:" + string );
//            out.println( "request.getRequestURI()= " + request.getRequestURI());
//            out.println("</body>");
//            out.println("</html>");
        }
        finally
        {
            out.close();
        }
    }
    
}
