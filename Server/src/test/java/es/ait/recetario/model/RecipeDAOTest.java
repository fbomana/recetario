/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import junit.framework.TestCase;

/**
 *
 * @author aitkiar
 */
public class RecipeDAOTest extends TestCase
{
    private EntityManagerFactory emf;
    private EntityManager em;
    
    public RecipeDAOTest(String testName)
    {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        emf = Persistence.createEntityManagerFactory("persistencePUTest");
        em = emf.createEntityManager();
    }
    
    @Override
    protected void tearDown() throws Exception
    {
        em.close();
        emf.close();
        super.tearDown();
    }

    /**
     * Test of save method, of class RecipeDAO.
     */
    public void testCrud()
    {
        RecipeDAO recipeDao = new RecipeDAO();
        recipeDao.em = this.em;
        TagDAO tagDao = new TagDAO();
        tagDao.em = this.em;
        recipeDao.tagDAO = tagDao;
        
        EntityTransaction ts = em.getTransaction();
        ts.begin();
        try
        {
            List<Tag> tags = new ArrayList<Tag>();
            List<Tag> secondTagList = new ArrayList<Tag>();
            Tag tag = new Tag();
            tag.setTag("junittest1");
            tagDao.save( tag );
            tags.add( tag );
            secondTagList.add ( tag );
            
            tag = new Tag();
            tag.setTag("junittest2");
            tagDao.save( tag );
            tags.add( tag );
            secondTagList.add ( tag );
            
            tag = new Tag();
            tag.setTag("junittest3");
            tagDao.save( tag );
            tags.add( tag );
            
            assertTrue( em.createQuery("select t from Tag t where t.tag like 'junittest%'").getResultList().size() == 3 );
                    
            Recipe recipe = new Recipe();
            recipe.setRecipeTitle("test recipe");
            recipe.setRecipe("En un lugar de la mancha de cuyo nombre no quiero acordarme no ha mucho vivia un hidalgo.");
            recipe.setRecipeOrigin("test");
            recipe.setRecipeDate( new java.util.Date());
            recipe.setRecipeUpdate( recipe.getRecipeDate());
            recipe.setTagsList( secondTagList );
            
            recipeDao.save( recipe );
            
            //Buscamos por tres tags
            assertTrue( recipeDao.searchByTags( tags, true ).isEmpty());
            assertFalse( recipeDao.searchByTags( tags, false ).isEmpty());
            
            // Buscamos por dos tags.
            assertFalse( recipeDao.searchByTags( secondTagList, true ).isEmpty());
            assertFalse( recipeDao.searchByTags( secondTagList, false ).isEmpty());
            
            recipe = recipeDao.searchByTags( secondTagList, true ).get( 0 );
            assertTrue( recipe.getTagsList().size() == 2 );

            for ( Tag aux : recipe.getTagsList())
            {
                System.out.println( aux.getTag());
            }
            
            recipeDao.remove( recipe );
            
            for ( Tag theTag : tags )
            {
                tagDao.remove( theTag );
            }
            ts.commit();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if ( ts.isActive() )
            {
                ts.rollback();
            }
        }
    }
    
}
