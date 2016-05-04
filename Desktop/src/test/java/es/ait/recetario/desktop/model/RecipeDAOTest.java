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
public class RecipeDAOTest extends TestCase
{
    private Preferences preferences;
    private Connection connection;
    
    public RecipeDAOTest(String testName)
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
     * Test of create method, of class RecipeDAO.
     */
    public void testCrud() throws Exception
    {
        RecipeDAO dao = RecipeDAO.getInstance();
        
        List<String> test1Tags = new ArrayList<>();
        test1Tags.add( "testTag1" );
        test1Tags.add( "testTag2" );
        
        Recipe test1 = new Recipe();
        test1.setRecipe("The recipe test body");
        test1.setRecipeDate( new java.util.Date());
        test1.setRecipeTitle( "Test recipe 1");
        test1.setRecipeOrigin( preferences.getRecetarioName() );
        test1.setRecipeUpdate( new java.util.Date());
        test1.setTags( test1Tags );
        
        System.out.println("create");
        dao.create(connection, test1);
        assertTrue( test1.getRecipeId() != -1 );        
        
        System.out.println("Read");
        Recipe searchResult = dao.search(connection, test1.getRecipeId());
        assertTrue( searchResult.getRecipeTitle().equals( test1.getRecipeTitle()));
        assertTrue( searchResult.getTags().size() == 2 );
        assertTrue( searchResult.getTags().contains("testTag1"));
        assertTrue( searchResult.getTags().contains("testTag2"));
        
        System.out.println("update");
        test1.setRecipeTitle( test1.getRecipeTitle() + " updated");
        test1Tags.remove("testTag1" );
        test1Tags.add( "testTag3" );
        test1.setTags(test1Tags);
        dao.update(connection, test1);
        Recipe updateTest = dao.search(connection, test1.getRecipeId());
        assertTrue( updateTest.getRecipeTitle().equals( test1.getRecipeTitle()));
        assertTrue( updateTest.getTags().size() == 2 );
        assertFalse( updateTest.getTags().contains("testTag1"));
        assertTrue( updateTest.getTags().contains("testTag2"));
        assertTrue( updateTest.getTags().contains("testTag3"));
        
        System.out.println("Delete");
        dao.delete(connection, test1.getRecipeId());
        Recipe deleteTest = dao.search(connection, test1.getRecipeId());
        assertNull( deleteTest );
    }
    
    public void testInclusiveSearch() throws Exception
    {
        RecipeDAO dao = RecipeDAO.getInstance();
        List<String> test1Tags = new ArrayList<>();
        test1Tags.add( "testTag1" );
        test1Tags.add( "testTag2" );
        
        Recipe test1 = new Recipe();
        test1.setRecipe("The recipe test body");
        test1.setRecipeDate( new java.util.Date());
        test1.setRecipeTitle( "Test recipe 1");
        test1.setRecipeOrigin( preferences.getRecetarioName() );
        test1.setRecipeUpdate( new java.util.Date());
        test1.setTags( test1Tags );
        dao.create(connection, test1);
        
        List<String> test2Tags = new ArrayList<>();
        test2Tags.add( "testTag2" );
        test2Tags.add( "testTag3" );
        
        Recipe test2 = new Recipe();
        test2.setRecipe("The recipe test body");
        test2.setRecipeDate( new java.util.Date());
        test2.setRecipeTitle( "Test recipe 2");
        test2.setRecipeOrigin( preferences.getRecetarioName() );
        test2.setRecipeUpdate( new java.util.Date());
        test2.setTags( test2Tags );
        dao.create(connection, test2);
        
        List<String> searchTags = new ArrayList<>();
        searchTags.add( "testTag2" );
        RecipeResult result = dao.search(connection, searchTags, true, 1, 500 );
        assertNotNull(result);
        assertNotNull(result.getRecipes());
        assertTrue( result.getRecipes().size() >= 2 );
        
        boolean found1 = false;
        boolean found2 = false;
        
        for ( Recipe recipe : result.getRecipes())
        {
            found1 = found1 || recipe.getRecipeId() == test1.getRecipeId();
            found2 = found2 || recipe.getRecipeId() == test2.getRecipeId();
        }
        
        assertTrue( found1 && found2 );
        
        searchTags.clear();
        searchTags.add( "testTag1" );
        result = dao.search(connection, searchTags, true, 1, 500 );
        assertNotNull(result);
        assertNotNull(result.getRecipes());
        assertTrue( result.getRecipes().size() >= 1 );
        found1 = false;
        found2 = false;
        for ( Recipe recipe : result.getRecipes())
        {
            found1 = found1 || recipe.getRecipeId() == test1.getRecipeId();
            found2 = found2 || recipe.getRecipeId() == test2.getRecipeId();
        }
        assertTrue( found1 );
        assertFalse( found2 );
        
        result = dao.search(connection, null, true, 1, 500 );
        assertNotNull(result);
        assertNotNull(result.getRecipes());
        assertTrue( result.getRecipes().size() >= 2 );
        
        dao.delete( connection,  test1.getRecipeId());
        dao.delete( connection,  test2.getRecipeId());
    }

    public void testExclusiveSearch() throws Exception
    {
        RecipeDAO dao = RecipeDAO.getInstance();
        List<String> test1Tags = new ArrayList<>();
        test1Tags.add( "testTag1" );
        test1Tags.add( "testTag2" );
        
        Recipe test1 = new Recipe();
        test1.setRecipe("The recipe test body");
        test1.setRecipeDate( new java.util.Date());
        test1.setRecipeTitle( "Test recipe 1");
        test1.setRecipeOrigin( preferences.getRecetarioName() );
        test1.setRecipeUpdate( new java.util.Date());
        test1.setTags( test1Tags );
        dao.create(connection, test1);
        
        List<String> test2Tags = new ArrayList<>();
        test2Tags.add( "testTag2" );
        test2Tags.add( "testTag3" );
        
        Recipe test2 = new Recipe();
        test2.setRecipe("The recipe test body");
        test2.setRecipeDate( new java.util.Date());
        test2.setRecipeTitle( "Test recipe 2");
        test2.setRecipeOrigin( preferences.getRecetarioName() );
        test2.setRecipeUpdate( new java.util.Date());
        test2.setTags( test2Tags );
        dao.create(connection, test2);
        
        List<String> searchTags = new ArrayList<>();
        searchTags.add( "testTag2" );
        RecipeResult result = dao.search(connection, searchTags, false, 1, 500 );
        assertNotNull(result);
        assertNotNull(result.getRecipes());
        assertTrue( result.getRecipes().size() >= 2 );
        
        boolean found1 = false;
        boolean found2 = false;
        
        for ( Recipe recipe : result.getRecipes())
        {
            found1 = found1 || recipe.getRecipeId() == test1.getRecipeId();
            found2 = found2 || recipe.getRecipeId() == test2.getRecipeId();
        }
        
        assertTrue( found1 && found2 );
        
        searchTags.add( "testTag3" );
        result = dao.search(connection, searchTags, false, 1, 500 );
        assertNotNull(result);
        assertNotNull(result.getRecipes());
        assertTrue( result.getRecipes().size() >= 1 );
        found1 = false;
        found2 = false;
        for ( Recipe recipe : result.getRecipes())
        {
            found1 = found1 || recipe.getRecipeId() == test1.getRecipeId();
            found2 = found2 || recipe.getRecipeId() == test2.getRecipeId();
        }
        assertFalse( found1 );
        assertTrue( found2 );
        
        result = dao.search(connection, null, false, 1, 500 );
        assertNotNull(result);
        assertNotNull(result.getRecipes());
        assertTrue( result.getRecipes().size() >= 2 );
        

        dao.delete( connection,  test1.getRecipeId());
        dao.delete( connection,  test2.getRecipeId());
    }
}
