package es.ait.recetario.synchronize;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.ait.recetario.model.Recipe;

@RestController
@RequestMapping("/services/sync")
public class SynchronizeControllerRest
{

	/**
	 * Reads from an external URL the list of recipes and returns them in JSON form. 
	 * @param request
	 * @return
	 */
	@RequestMapping( path="/external", method = RequestMethod.POST, produces = "application/json" )
	public Recipe[] getExternalRecipes( HttpServletRequest request )
	{
    	StringBuilder builder = new StringBuilder("");
		try
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
	
	@RequestMapping( path="/sync/recipe/{id}")
	public Recipe getRecipe( @PathVariable int id, 	HttpServletRequest request )
	{
    	StringBuilder builder = new StringBuilder("");
		try
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
