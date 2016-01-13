/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.recipes;

import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.commands.Command;
import es.ait.recetario.desktop.model.Recipe;
import es.ait.recetario.desktop.model.RecipeDAO;
import es.ait.recetario.desktop.templates.TemplateFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class SynchronizeRecipes extends Command
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        if ( "/recipes/SynchronizeRecipes".equals( request.getParameter("post") ) )
        {
            processPost( request, response, out );
            return;
        }
        
        Properties properties = new Properties();
        out.print( TemplateFactory.getTemplate( "synchronizeRecipes.html", properties ));
    }
    
    private void processPost(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        URL url = new URL ( request.getParameter("synchronizeURL") + "/services/RecipeSearch" );
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod( "POST" );
        http.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
        http.setRequestProperty( "charset", "utf-8");
        http.setDoOutput( true );
        http.getOutputStream().write( ("importList=" + URLEncoder.encode( request.getParameter("importList"), "UTF-8")).getBytes(StandardCharsets.UTF_8 ));
        http.connect();
        if ( http.getResponseCode() == 200 )
        {
            try ( InputStreamReader irs = new InputStreamReader( http.getInputStream(), "UTF-8");)
            {
                String texto = "";
                try( BufferedReader buf = new BufferedReader( irs ))
                {
                    String linea;
                    while ( ( linea = buf.readLine()) != null )
                    {
                        texto += linea + "\n";
                    }
                }
                
                StringReader sr = new StringReader(texto);
                JsonReader jsr = Json.createReader( sr );
                Connection connection = null;
                try
                {
                    connection = BBDDManager.getInstance(null).getConnection();
                    connection.setAutoCommit( false );
                    RecipeDAO dao = new RecipeDAO();
                    JsonArray array = jsr.readArray();
                    for ( int i = 0; i < array.size(); i ++ )
                    {
                        JsonObject recipeJson = array.getJsonObject(i);
                        dao.importRecipe( connection, new Recipe( recipeJson ));
                    }
                    connection.commit();
                }
                catch ( Exception e )
                {
                    if ( connection != null )
                    {
                        connection.rollback();
                    }
                    throw e;
                }
                finally
                {
                    if ( connection != null )
                    {
                        connection.close();
                    }
                }
            }
            catch ( Exception e )
            {
                throw new ServletException( e );
            }
        }
        http.disconnect();
        try
        {
            forward("/recipes/SearchRecipes", request, response, out);
        }
        catch ( Exception e )
        {
            throw new ServletException( e );
        }
    }
}
