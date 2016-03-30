package es.ait.recetario.desktop.commands.recipes;


import es.ait.recetario.desktop.commands.Command;
import es.ait.recetario.desktop.commands.CommandPath;
import es.ait.recetario.desktop.templates.TemplateFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This command shows all recipes ordered by last update
 * 
 * @author aitkiar
 */
@CommandPath(path="/recipes/SearchRecipes")
public class SearchRecipes extends Command
{
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        Properties properties = new Properties();
        out.print( TemplateFactory.getTemplate( request, "searchRecipes.html", properties ));
    }
    
}
