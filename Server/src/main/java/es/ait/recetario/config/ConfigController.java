package es.ait.recetario.config;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * this class acts as a controller for the diferent config pages.
 * 
 * @author aitkiar
 */
@Controller
@RequestMapping(path="/config/parameters" )
public class ConfigController
{
    
    /**
     * Set up the preferences in the model and ask for the form view
     * @param model
     * @return 
     */
    @RequestMapping( method = {RequestMethod.GET } )
    public String displayForm( Model model )
    {
        model.addAttribute("preferences", Preferences.getInstance());
        return "/config/parameters.jsp";
    }
    
    /**
     * Set up the preferences in the model and ask for the form view
     * @param request the request object injected by spring
     * @param model
     * @return 
     * @throws java.sql.SQLException 
     */
    @RequestMapping( method = {RequestMethod.POST } )
    public String displayForm( HttpServletRequest request, Model model ) throws SQLException
    {
        Preferences preferences = Preferences.getInstance();
        preferences.setName( request.getParameter("name"));
        preferences.setAutoSaveInterval( Integer.parseInt( request.getParameter("autosaveInterval")));
        preferences.save();
        model.addAttribute("url", "/recetario/config/parameters");
        return "/redirect.jsp";
    }    
}
