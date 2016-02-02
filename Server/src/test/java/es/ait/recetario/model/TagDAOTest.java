/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import junit.framework.TestCase;

/**
 *
 * @author aitkiar
 */
public class TagDAOTest extends TestCase
{
    private EntityManagerFactory emf;
    private EntityManager em;
    
    public TagDAOTest(String testName)
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
     * Test of save method, of class TagDAO.
     */
    public void testCrud()
    {
        TagDAO dao = new TagDAO();
        dao.em = this.em;
        EntityTransaction ts = em.getTransaction();
        ts.begin();
        try
        {
            Tag tag = new Tag();
            tag.setTag("tagDAOTest");
            
            dao.save( tag );
            assertFalse( em.createNamedQuery("Tag.findAll").getResultList().isEmpty());
            
            dao.remove( tag );
            try
            {
                Tag encontrado = em.find( Tag.class, tag.getTag());
                assertNull(encontrado);
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                assertTrue( false );
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
            if ( ts != null && ts.isActive())
            {
                ts.rollback();
            }
        }
    }
    
}
