package es.ait.recetario.desktop.commands.recipes;


import es.ait.recetario.desktop.Utils;
import es.ait.recetario.desktop.commands.Command;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This command shows all recipes and the tags ordered by tag density.
 * 
 * @author aitkiar
 */
public class SearchRecipes extends Command
{
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        out.print( Utils.contentsToHtml("Todas las recetas"));
    }
    
}
