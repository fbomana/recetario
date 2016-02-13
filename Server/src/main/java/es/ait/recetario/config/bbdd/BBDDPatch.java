/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.config.bbdd;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface that uses all BBDD Patchs
 *
 * @author aitkiar
 */
public interface BBDDPatch
{
    /**
     * Runs de BBDD update over the connection. Al the alters and updates are 
     * controlled in an outside transaction, no commit or rollback must be made
     * inside the method run.
     * 
     * @param conection
     * @throws SQLException 
     */
    public void run( Connection connection ) throws SQLException;
    
    /**
     * Returns the version of the app afected by the patch.
     * 
     * @return 
     */
    public String forVersion();
}
