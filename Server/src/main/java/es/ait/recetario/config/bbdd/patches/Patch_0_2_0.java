/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.config.bbdd.patches;

import es.ait.recetario.config.Preferences;
import es.ait.recetario.desktop.commands.BBDD.BBDDPatch;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;

/**
 *
 * @author aitkiar
 */
public class Patch_0_2_0 implements BBDDPatch
{

    @Override
    public void run(Connection connection) throws SQLException
    {
        try ( PreparedStatement ps = connection.prepareStatement("alter table recipe add column \n" +
            "  recipe_origin varchar( 512 )"))
        {
            ps.executeUpdate();
        }

        try ( PreparedStatement ps = connection.prepareStatement("alter table recipe add column \n" +
            "  recipe_share_id varchar( 1024 )"))
        {
            ps.executeUpdate();
        }
                    
        try (PreparedStatement ps = connection.prepareStatement("update recipe set recipe_origin=?"))
        {
            ps.setString(1, Preferences.getInstance().getName() );
            ps.executeUpdate();
        }
        
        try (PreparedStatement ps = connection.prepareStatement("select recipe_id, recipe_title, recipe_date from recipe");ResultSet rs = ps.executeQuery();)
        {
            try ( PreparedStatement ps2 = connection.prepareStatement("update recipe set recipe_origin=?, recipe_share_id=? where recipe_id = ?"))
            {
                int contador = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                while ( rs.next() )
                {
                    ps2.setString( 1, Preferences.getInstance().getName());
                    String shareId = Preferences.getInstance().getName() + rs.getString("recipe_title") + sdf.format( rs.getTimestamp("recipe_date"));
                    String encodedString = Base64.getEncoder().encodeToString( shareId.getBytes("UTF-8"));
                    ps2.setString( 2, encodedString );
                    ps2.setInt( 3, rs.getInt("recipe_id"));
                    ps2.addBatch();
                    contador ++;
                    if ( contador % 100 == 0 )
                    {
                        ps2.executeBatch();
                        ps2.clearBatch();
                    }
                }
                ps2.executeBatch();
            }
            catch ( UnsupportedEncodingException e )
            {
                throw new SQLException( e );
            }
        }

        try ( PreparedStatement ps = connection.prepareStatement("alter table recipe modify "
            + "recipe_origin varchar( 512 ) not null"))
        {
            ps.executeUpdate();
        }
        
//        try ( PreparedStatement ps = connection.prepareStatement("alter table recipe add constraint "
//                + "recipe_origin_title_uq unique ( recipe_origin, recipe_title )"))
//        {
//            ps.executeUpdate();
//        }
//        
//        try ( PreparedStatement ps = connection.prepareStatement("alter table recipe add constraint "
//                + "recipe_share_id_uq unique ( recipe_share_id )"))
//        {
//            ps.executeUpdate();
//        }
        
        try ( PreparedStatement ps = connection.prepareStatement(" update preferences set app_version = '0.2.0'"))
        {
            ps.executeUpdate();
        }
    }

    @Override
    public String forVersion()
    {
        return "0.2.0";
    }
    
}
