/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.services;

import es.ait.recetario.desktop.Utils;
import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.commands.JSONServiceCommand;
import es.ait.recetario.desktop.model.TagDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service that searchs for the list of tags that are not already included in the
 * search parameter.
 * @author aitkiar
 */
public class TagSearch extends JSONServiceCommand
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        List<String> excludedTags = Utils.string2tags( request.getParameter("tags"));
        try
            ( Connection connection = BBDDManager.getInstance("").getConnection())
        {
            List<String> tags = new TagDAO().searchTags(connection, excludedTags);
            JsonArrayBuilder builder = Json.createArrayBuilder();
            for ( String tag : tags )
            {
                builder = builder.add( tag );
            }
            Json.createWriter(out).write( builder.build() );
        }
        catch ( Exception e )
        {
            throw new ServletException( e );
        }
    }
    
}
