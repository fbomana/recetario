/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.services;

import es.ait.recetario.desktop.commands.JSONServiceCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This commands makes a call to another recetario RecipeSearch service, avoiding XSS alerts from
 * the webbrowser.
 *
 * @author aitkiar
 */
public class GetExternalRecipes extends JSONServiceCommand
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        URL url = new URL ( request.getParameter("url") + "/services/RecipeSearch" );
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setInstanceFollowRedirects(true);
        http.connect();
        if ( http.getResponseCode() == 200 )
        {
            try ( InputStreamReader irs = new InputStreamReader( http.getInputStream(), "UTF-8"); BufferedReader buf = new BufferedReader( irs );)
            {
                String line;
                while ( (line = buf.readLine()) != null )
                {
                    out.println(line);
                }
            }
        }
        http.disconnect();
    }
    
}
