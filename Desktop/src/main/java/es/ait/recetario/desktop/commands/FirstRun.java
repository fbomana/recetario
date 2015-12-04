package es.ait.recetario.desktop.commands;

import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.templates.TemplateFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is the command for the mandatory configurations in the first-run
 * 
 * @author aitkiar
 */
public class FirstRun extends Command
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        if ( "1".equals( request.getParameter("post")))
        {
            try
            {
                if ( "S".equals( request.getParameter("portable")))
                {
                    preferences.setDerbyFolder("data");
                }
                else
                {
                    File file = new File( request.getParameter("bbddFolder"));
                    if ( !file.exists() )
                    {
                        if ( !file.mkdirs())
                        {
                            throw new Exception("Can't create folder: " + file.getAbsolutePath());
                        }
                        else
                        {
                            file.delete();
                        }
                    }
                    preferences.setDerbyFolder( request.getParameter("bbddFolder") );
                }
                
                try
                {
                    BBDDManager.getInstance( preferences.getDerbyFolder() ).startUp();
                }
                catch ( SQLException e )
                {
                    e.printStackTrace();
                    throw new Exception("Error starting up the BBDD: " + e.getMessage());
                }
                preferences.save();
                preferences.setFirstRun( false );
                forward(request.getParameter("initialResource"), request, response, out );
            }
            catch ( Exception e )
            {
                Properties properties = new Properties();
                properties.setProperty("defaultFolderValue", preferences.getDerbyFolder() != null ? preferences.getDerbyFolder() : ( System.getProperty("user.home") + "/.recetario/data"));
                properties.setProperty("errorMessage", e.getMessage());
                properties.setProperty("initialResource", request.getParameter("initialResource"));
                out.print( TemplateFactory.getTemplate( "firstRun.html", properties ));
                return;
            }
        }
        else
        {
            Properties properties = new Properties();
            properties.setProperty("defaultFolderValue", System.getProperty("user.home") + "/.recetario/data");
            properties.setProperty("errorMessage", "");
            properties.setProperty("initialResource", request.getRequestURI() );
            out.print( TemplateFactory.getTemplate( "firstRun.html", properties ));
        }
    }
    
}
