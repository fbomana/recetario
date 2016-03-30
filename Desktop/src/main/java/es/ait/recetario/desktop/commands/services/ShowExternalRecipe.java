/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.services;

import es.ait.recetario.desktop.commands.JSONServiceCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class ShowExternalRecipe extends JSONServiceCommand
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        Properties properties = new Properties();
        URL url = new URL ( request.getParameter("url") + "/services/ShowRecipe" );
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod( "POST" );
        http.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
        http.setRequestProperty( "charset", "utf-8");
        http.setDoOutput( true );
        http.getOutputStream().write( ("id=" + URLEncoder.encode( request.getParameter("id"), "UTF-8")).getBytes(StandardCharsets.UTF_8 ));
        http.connect();
        if ( http.getResponseCode() == 200 )
        {
            try ( InputStreamReader irs = new InputStreamReader( http.getInputStream(), "UTF-8");BufferedReader buf = new BufferedReader( irs ))
            {
                String linea;
                while ( ( linea = buf.readLine()) != null )
                {
                    out.println( linea );
                }
            }
            catch ( Exception e )
            {
                throw new ServletException( e );
            }
        }
        http.disconnect();
    }
    
}
