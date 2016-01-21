/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands;

import es.ait.recetario.desktop.preferences.Preferences;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author aitkiar
 */
public abstract class Command
{
    protected Preferences preferences;
    protected HttpSession session;
    
    public void handle( HttpServletRequest request, HttpServletResponse response, PrintWriter out ) throws IOException, ServletException
    {
        preferences = Preferences.getInstance();
        session = request.getSession(true);
        if ( preferences.isFirstRun() )
        {
            preferences.setFirstRun( false );
            try
            {
                forward("/FirstRun", request, response, out );
            }
            catch ( ClassNotFoundException | InstantiationException | IllegalAccessException  e ) 
            {
                throw new ServletException( e );
            }
        }
        else
        {
            processRequest( request, response, out );
        }
    }
    
    protected void forward ( String resource, HttpServletRequest request, HttpServletResponse response, PrintWriter out ) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, IOException, ServletException
    {
        CommandFactory.getCommand( resource ).handle( request, response, out );
    }
    
    public abstract void processRequest( HttpServletRequest request, HttpServletResponse response, PrintWriter out ) throws IOException, ServletException;
    
    public String getContentType( String source )
    {
        return "text/html;charset=utf-8";
    }
}
