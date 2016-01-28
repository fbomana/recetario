/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.handlers;

import es.ait.recetario.desktop.Utils;
import es.ait.recetario.desktop.commands.Command;
import es.ait.recetario.desktop.commands.CommandFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class CommandHandler implements BaseHandler
{

    @Override
    public void handle( String resource, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
    {
        
        Command command = null;
        PrintWriter out = null;
        try
        {
            out = response.getWriter();
            command = CommandFactory.getCommand( resource );
            command.handle( request, response, out );
        }
        catch ( Exception e )
        {
            out.println ( Utils.exceptionToHTMLString( request, e ));
        }
        finally
        {
            out.close();
        }
    }

    @Override
    public String getContentType(String resource, HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        try
        {
            return CommandFactory.getCommand( resource ).getContentType(resource);
        }
        catch ( Exception e )
        {
            throw new ServletException( e );
        }
    }
    
}
