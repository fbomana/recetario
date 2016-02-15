package es.ait.recetario.config.bbdd.patches;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import es.ait.recetario.config.bbdd.BBDDPatch;

public class Patch_0_4_0 implements BBDDPatch	 
{
	private final String version = "0.4.0";
	
	@Override
	public void run(Connection connection) throws SQLException 
	{
		try ( PreparedStatement ps = connection.prepareStatement("update preferences set app_version=?");)
		{
			ps.setString( 1, version );
			ps.executeUpdate();
		}
	}

	@Override
	public String forVersion() {
		return version;
	}

}
