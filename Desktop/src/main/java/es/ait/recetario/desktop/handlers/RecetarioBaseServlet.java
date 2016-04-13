/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.handlers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class RecetarioBaseServlet extends HttpServlet
{
    
    
    public void processRequest( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        //Enable Cors
        response.addHeader("Access-Control-Allow-Origin", "*");
        String resource = request.getRequestURI();
        BaseHandler handler = HandlerFactory.getHandler( resource );
        response.setContentType( handler.getContentType(resource, request, response) );
        handler.handle( resource, request, response );
    }
    
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        processRequest( request, response );
    }
    
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        processRequest( request, response );
    }
    
}
