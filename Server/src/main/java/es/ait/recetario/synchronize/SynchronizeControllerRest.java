package es.ait.recetario.synchronize;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public @ResponseBody byte[] getExternalRecipes( HttpServletRequest request )
    {
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
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if ( http.getResponseCode() == 200 )
            {
                try ( InputStream in =  http.getInputStream();)
                {
                    byte[] buf = new byte[4096];
                    int leidos = 0;
                    while (( leidos = in.read(buf) ) > 0  )
                    {
                        out.write(buf, 0, leidos);
                    }
                }
            }
            http.disconnect();


            return out.toByteArray();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            try
            {
                return "{result:[]}".getBytes("UTF-8");
            }
            catch ( Exception ex )
            {
                return null;
            }
        }
    }
	
    /**
     * Gets a recipe from the external recetario bassed on it's shareId
     * @param request
     * @return
     */
    @RequestMapping( path="/external/recipe", produces = "application/json")
    public @ResponseBody byte[] getRecipe( HttpServletRequest request )
    {
        try
        {
            Properties properties = new Properties();
            URL url = new URL ( request.getParameter("url") + "/services/recipe?id=" + request.getParameter("id") );
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod( "GET" );
            http.connect();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if ( http.getResponseCode() == 200 )
            {
                try ( InputStream in =  http.getInputStream();)
                {
                    byte[] buf = new byte[4096];
                    int leidos = 0;
                    while (( leidos = in.read(buf) ) > 0  )
                    {
                        out.write(buf, 0, leidos);
                    }
                }
            }
            http.disconnect();
            return out.toByteArray();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }
    }
}
