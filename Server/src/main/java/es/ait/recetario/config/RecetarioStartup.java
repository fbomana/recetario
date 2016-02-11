package es.ait.recetario.config;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import es.ait.recetario.config.bbdd.BBDDManager;

/**
 * This servlets initialilze the preferences object with the context configured
 * preferences on startup.
 * @author aitkiar
 */
public class RecetarioStartup extends HttpServlet
{

    @Override
    public void init()
    {
        System.out.println("pasa por el init");
        ServletContext context = getServletContext();
        Preferences preferences = Preferences.getInstance();
        preferences.setBbddHost( context.getInitParameter("es.ait.recetario.bbdd.host"));
        preferences.setBbddPassword( context.getInitParameter("es.ait.recetario.bbdd.password"));
        preferences.setBbddUser( context.getInitParameter("es.ait.recetario.bbdd.user"));
        
        try
        {
            BBDDManager.getInstance().versionControl();
            preferences.load();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }

}
