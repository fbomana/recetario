package es.ait.recetario.synchronize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.ait.recetario.config.bbdd.BBDDManager;
import es.ait.recetario.model.Recipe;
import es.ait.recetario.model.RecipeDAO;

/**
 * Controller for the synchronization with other repositories
 *
 */
@Controller
@RequestMapping("/recipe/synchronize")
public class SynchronizeControllerWeb
{
	@Autowired
	private RecipeDAO dao;
	
	@RequestMapping( method = RequestMethod.GET )
	public String synchronize( Model model )
	{
		return "/synchronize/synchronize.jsp";
	}
	
	@RequestMapping( method = RequestMethod.POST )
	public String synchronize( HttpServletRequest request, Model model ) throws IOException
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
                
    			ObjectMapper mapper = new ObjectMapper();
    			Recipe[] recipes =  mapper.readValue( texto, Recipe[].class);
                try
                {
                	for ( Recipe recipe : recipes )
                	{
                        dao.importRecipe( recipe );
                    }
                }
                catch ( Exception e )
                {

                    throw e;
                }
             }
            catch ( Exception e )
            {
                throw new IOException( e );
            }
        }
        http.disconnect();

        model.addAttribute("url", "/recetario/recipe/search");
        return "/redirect.jsp";
	}
}
