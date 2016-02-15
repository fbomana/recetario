/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.config.bbdd.patches;
		
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import es.ait.recetario.config.bbdd.BBDDPatch;

/**
 * First patch. Creates all the basic tables.
 * 	
 */
public class Patch_0_1_0 implements BBDDPatch
{
	private final String version = "0.1.0";

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
                + " recipe_id INTEGER NOT NULL AUTO_INCREMENT,"
                + " recipe_title varchar( 200 ) NOT NULL,"
                + " recipe text NOT NULL,"
                + " recipe_date timestamp not null default CURRENT_TIMESTAMP,"
                + " recipe_update timestamp, "
                + " CONSTRAINT recipe_pk PRIMARY KEY (recipe_id))"))
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
  
        try ( PreparedStatement ps = connection.prepareStatement("create table preferences ("
                + " app_version varchar(20) not null,"
                + " name varchar( 512 ) not null,"
                + " autosave_interval int not null )");)
        {
            ps.executeUpdate();
        }
        
        try ( PreparedStatement ps = connection.prepareStatement("insert into preferences ( app_version, name, autosave_interval ) values ( ?, 'default', 30000 )"))
        {
        	ps.setString( 1, version );
            ps.executeUpdate();
        }
    }

    @Override
    public String forVersion()
    {
        return version;
    }
    
}
