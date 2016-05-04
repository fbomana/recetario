/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.model;

import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.preferences.Preferences;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author aitkiar
 */
public class TagDAOTest extends TestCase
{
    private Preferences preferences;
    private Connection connection;
    
    public TagDAOTest(String testName)
    {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        preferences = Preferences.getInstance();
        BBDDManager.getInstance( preferences.getDerbyFolder() ).startUp();
        connection = BBDDManager.getInstance( preferences.getDerbyFolder()).getConnection();
    }
    
    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        connection.close();
        BBDDManager.getInstance( preferences.getDerbyFolder() ).shutDown();
    }

    /**
     * Test of searchRelatedTags method, of class TagDAO.
     * @throws java.lang.Exception
     */
    public void testSearchRelatedTags() throws Exception
    {
        System.out.println("searchRelatedTags");
//        TagDAO instance = new TagDAO();
//        List<String> searchTags = new ArrayList<>();
//        searchTags.add("webapp");
//        List<String> result = instance.searchRelatedTags(connection, searchTags );
//        assertTrue( result.size() >= 3 );
//        assertTrue( result.contains( "springmvc" ));
//        assertTrue( result.contains( "spring" ));
//        assertTrue( result.contains( "jpa" ));
//        searchTags.add("springmvc");
//        result = instance.searchRelatedTags(connection, searchTags );
//        assertTrue( result.size() >= 2 );
//        assertTrue( result.contains( "spring" ));
//        assertTrue( result.contains( "jpa" ));
//        searchTags.add("jpa");
//        result = instance.searchRelatedTags(connection, searchTags );
//        assertTrue( result.size() >= 1 );
//        assertTrue( result.contains( "spring" ));
    }
    
}
