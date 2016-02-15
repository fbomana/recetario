/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO objet for work with recipes.
 * @author aitkiar
 */
@Repository
@Transactional
public class RecipeDAO
{
    @PersistenceContext(unitName = "recetarioPU")
    protected EntityManager em;
    
    @Autowired
    protected TagDAO tagDAO;
    
    /**
     * Saves a recipe in the BBDD.
     * @param recipe 
     */
    public void save( Recipe recipe )
    {
        if ( recipe.getRecipeId() == null || em.find( Recipe.class, recipe.getRecipeId()) == null )
        {
            em.persist( recipe );
        }
        else
        {
            em.merge( recipe );
        }
        if ( recipe.getTagsList() != null )
        {
            for ( Tag tag : recipe.getTagsList())
            {
                tagDAO.save( tag );
            }
        }
    }
    
    /**
     * Returns the recipe with the id passed as parameter.
     * @param id
     * @return 
     */
    public Recipe find( Integer id )
    {
        return (Recipe)em.createNamedQuery("Recipe.findByRecipeId").setParameter("recipeId", id).getSingleResult();
    }
    
    /**
     * Reemoves a recipe from BBDD.
     * @param recipe 
     */
    public void remove ( Recipe recipe )
    {
        recipe = em.find( Recipe.class, recipe.getRecipeId());
        em.remove( recipe );
    }
    
    /**
     * Search for recipes that have the tags passed as parameter.
     * @param tags list of tags to search for
     * @param allTags if true search for recipes that hava all the tags, if false 
     *  search for recipes that have some of the tags.
     * @return 
     */
    @SuppressWarnings("unchecked")
	public List<Recipe> searchByTags( List<Tag> tags, boolean allTags )
    {
        String sql = "select r.* from recipe r";
        
        if ( tags != null && !tags.isEmpty())
        {
            String inClause = "(";
            String separator = " ";
            for ( Tag tag : tags )
            {
                inClause += separator + "?";
                separator = ", ";
            }
            inClause += " )";
            
            if ( allTags )
            {
                sql += " where recipe_id in ( select recipe_id from ( select recipe_id, count(*) as cuenta from recipe_tags where tag in " + inClause + " group by recipe_id ) f where cuenta = ?)";
            }
            else
            {
                sql += " where recipe_id in ( select distinct( recipe_id ) from recipe_tags where tag in " + inClause +" )";
            }
        }
        sql += " order by r.recipe_update";
        
        Query query = em.createNativeQuery(sql, Recipe.class );
        int i = 1;
        for ( Tag tag : tags )
        {
            query = query.setParameter( i++, tag.getTag() );
        }
        if ( tags != null && !tags.isEmpty() && allTags )
        {
            query = query.setParameter( i, tags.size());
        }
        return query.getResultList();
    }
    
    /**
     * Searchs for all the recipes with the selected shareIds.
     * @param shareIds An array containing the id's to search. If it's null or empty it returns an empty list
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Recipe> searchByShareId( String[] shareIds ) 
    {
    	if ( shareIds == null || shareIds.length == 0 )
    	{
    		return new ArrayList<Recipe>();
    	}
    	
    	String sql = "select r.* from Recipe r where r.shareId in ( ";
    	String separador = "";
    	for ( int i = 0; i < shareIds.length; i++  )
    	{
    		sql += separador + "?";
    		separador = ", ";
    	}
    	sql += " ) order by recipe_update desc";
    	
    	Query query = em.createQuery( sql );
    	for ( int i = 0; i < shareIds.length; i++  )
    	{
    		query = query.setParameter( i+1, shareIds[i]);
    	}
    	return query.getResultList();
    	
    }
    
    /**
     * Imports a recipe from an external source into the BBDD 
     * @param recipe
     */
    public void importRecipe( Recipe recipe )
    {
    	Recipe aux = null;
    	
    	try
    	{
    		aux = ( Recipe )em.createNamedQuery("Recipe.findByRecipeShareId").setParameter("recipeShareId", recipe.getRecipeShareId()).getSingleResult();
    		aux.setRecipeTitle( recipe.getRecipeTitle());
    		aux.setRecipe( recipe.getRecipeTitle());
    		aux.setRecipeOrigin( recipe.getRecipeOrigin());
    		aux.setRecipeDate( recipe.getRecipeDate());
    		aux.setRecipeUpdate( recipe.getRecipeUpdate());
    		aux.setTagsList( recipe.getTagsList());
    	}
    	catch ( NoResultException  e )
    	{
    		aux = recipe;
    		aux.setRecipeId( null );
    	}
    	save( aux );
    }

}
