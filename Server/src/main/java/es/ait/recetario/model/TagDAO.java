/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao objecto for work with tags.
 * @author aitkiar
 */
@Repository
@Transactional
public class TagDAO
{
    @PersistenceContext(unitName = "recetarioPU")
    protected EntityManager em;
    
    /**
     * Save a tag in BBDD
     * @param tag 
     */
    public void save( Tag tag )
    {
        if ( em.find( Tag.class, tag.getTag()) == null )
        {
            em.persist( tag );
        }
        else
        {
            em.merge( tag );
        }
    }
    
    /**
     * removes a tag from BBDD
     * @param tag 
     */
    public void remove ( Tag tag )
    {
        tag = em.find( Tag.class, tag.getTag() );
        em.remove(tag);
    }
    
    /**
     * Searchs for all tags that aren't inside the tags parameter.
     * @param tags
     * @return 
     */
    public List<Tag> searchNotIn( List<Tag> tags )
    {
        String sql = "select * from tags";
        
        if ( tags != null && !tags.isEmpty())
        {
            sql += " where tag not in ( ";
            String separator ="";
            for ( Tag tag : tags )
            {
                sql += separator + "?";
            }
            sql +=" )";
        }
        sql += " order by tag";
        Query query = em.createNativeQuery( sql, Tag.class );
        int i = 1;
        for ( Tag tag : tags )
        {
            query.setParameter( i++, tag.getTag());
        }
        return query.getResultList();
    }
}
