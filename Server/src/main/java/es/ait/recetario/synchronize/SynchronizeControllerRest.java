package es.ait.recetario.synchronize;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.ait.recetario.model.Recipe;
import org.springframework.util.MimeType;

@RestController
@RequestMapping("/services/sync")
public class SynchronizeControllerRest
{

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // External services
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Reads from an external URL the list of recipes and returns them in JSON form. 
     * @param request
     * @return
     */
    @RequestMapping( path="/external/search", method = RequestMethod.POST, produces = "application/json" )
    public Recipe[] getExternalRecipes( HttpServletRequest request )
    {
        StringBuilder builder = new StringBuilder("");
        try
        {
        URL url = new URL ( request.getParameter("url") + "/services/recipe/search" );
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setInstanceFollowRedirects(true);
        http.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
        http.setRequestProperty( "charset", "utf-8");
        if ( request.getParameter("importList") != null && !"".equals(request.getParameter("importList")))
        {
            http.setDoOutput( true );
            String parametros = "importList=" + URLEncoder.encode( request.getParameter("importLIist"), StandardCharsets.UTF_8.name());
            http.getOutputStream().write( parametros.getBytes(StandardCharsets.UTF_8 ));
        }
        http.connect();
        if ( http.getResponseCode() == 200 )
        {
            try ( InputStreamReader irs = new InputStreamReader( http.getInputStream(), "UTF-8"); BufferedReader buf = new BufferedReader( irs );)
            {
                String line;
                while ( (line = buf.readLine()) != null )
                {
                    builder.append( line ).append("\n");
                }
            }
        }
        http.disconnect();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue( builder.toString(), Recipe[].class);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return new Recipe[0];
        }
    }
	
    /**
     * Gets a recipe from the external recetario bassed on it's shareId
     * @param id
     * @param request
     * @return
     */
    @RequestMapping( path="/external/recipe", produces = "application/json")
    public Recipe getRecipe( HttpServletRequest request )
    {
        StringBuilder builder = new StringBuilder("");
        try
        {
        Properties properties = new Properties();
        URL url = new URL ( request.getParameter("url") + "/services/recipe?id=" + request.getParameter("id") );
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod( "GET" );
        http.connect();
        if ( http.getResponseCode() == 200 )
        {
            try ( InputStreamReader irs = new InputStreamReader( http.getInputStream(), "UTF-8"); BufferedReader buf = new BufferedReader( irs );)
            {
                String line;
                while ( (line = buf.readLine()) != null )
                {
                    builder.append( line ).append("\n");
                }
            }
        }
        http.disconnect();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue( builder.toString(), Recipe.class);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }
    }
}
