/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.services;

import es.ait.recetario.desktop.commands.CommandPath;
import es.ait.recetario.desktop.commands.JSONServiceCommand;
import java.io.IOException;
import java.io.PrintWriter;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rest service to interact with the preferences object
 * 
 * @author aitkiar
 */
@CommandPath(path="/services/preferences")
public class Preferences extends JSONServiceCommand
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        switch( request.getMethod() )
        {
            case "GET":
            {
                get( request, response, out );
                break;
            }
            case "POST":
            {
                post ( request, response, out );
            }
            default:
            {
                response.sendError( 405, "Method not Allowed" );
            }
        }
    }
    
    private void get(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        es.ait.recetario.desktop.preferences.Preferences pref = es.ait.recetario.desktop.preferences.Preferences.getInstance();
        Json.createWriter(out).write( pref.toJSon() );
    }
    
    private void post(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        
    }
}
