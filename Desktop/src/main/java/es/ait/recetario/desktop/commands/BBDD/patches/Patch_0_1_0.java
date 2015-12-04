/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.BBDD.patches;

import es.ait.recetario.desktop.commands.BBDD.BBDDPatch;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * First patch. Creates all the basic tables.
 * 
 * @author aitkiar
 */
public class Patch_0_1_0 implements BBDDPatch
{

    /**
     * Creates the tables:
     * <ul>
     * <li>recipes</li>
     * <li>recipes_tags</li>
     * <li>tags</li>
     * <li>version</li>
     * </ul>
     * 
     * @param connection
     * @throws SQLException 
     */
    @Override
    public void run( Connection connection ) throws SQLException
    {
        
        try ( PreparedStatement ps = connection.prepareStatement("create table tags (\n" +
            "  tag varchar(50) not null\n" +
            "  )"))
        {
            ps.executeUpdate();
        }
  
        try ( PreparedStatement ps = connection.prepareStatement("alter table tags add "
                + "constraint tags_pk primary key ( tag )"))
        {
            ps.executeUpdate();
        }

        try ( PreparedStatement ps = connection.prepareStatement("create table recipe ("
                + " recipe_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + " recipe_title varchar( 200 ) NOT NULL,"
                + " recipe CLOB(64 K ) NOT NULL,"
                + " recipe_date timestamp not null default CURRENT_TIMESTAMP,"
                + " recipe_update timestamp )"))
        {
            ps.executeUpdate();
        }
  
        try ( PreparedStatement ps = connection.prepareStatement("alter table recipe add constraint "
                + "recipe_pk primary key ( recipe_id )"))
        {
            ps.executeUpdate();
        }

        try ( PreparedStatement ps = connection.prepareStatement("create table recipe_tags ("
                + " recipe_id INTEGER NOT NULL,"
                + " tag VARCHAR(50) not null )"))
        {
            ps.executeUpdate();
        }
  
        try ( PreparedStatement ps = connection.prepareStatement("alter table recipe_tags add "
                + "constraint recipe_tags_pk primary key ( recipe_id, tag )"))
        {
            ps.executeUpdate();
        }

        try ( PreparedStatement ps = connection.prepareStatement("alter table recipe_tags add constraint "
                + "recipe_tags_fk_recipe foreign key ( recipe_id ) references recipe ( recipe_id )"))
        {
            ps.executeUpdate();
        }
        
        try ( PreparedStatement ps = connection.prepareStatement("alter table recipe_tags add constraint "
                + "recipe_tags_fk_tags foreign key ( tag ) references tags ( tag )"))
        {
            ps.executeUpdate();
        }
  
        try ( PreparedStatement ps = connection.prepareStatement("create table version ("
                + " app_version varchar(20) not null)"))
        {
            ps.executeUpdate();
        }
        
        try ( PreparedStatement ps = connection.prepareStatement("insert into version values ( '0.1.0' )"))
        {
            ps.executeUpdate();
        }
    }

    @Override
    public String forVersion()
    {
        return "0.1.0";
    }
    
}
