/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO objet for work with recipes.
 * @author aitkiar
 */
@Repository
@Transactional
public class RecipeDAO extends AbstractDAO<Recipe>
{   
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
        return em.find(Recipe.class, id);
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
    public List<Recipe> searchByTags( List<Tag> tags, boolean allTags )
    {
        return (List<Recipe>)searchByTags( tags, allTags, null, null ).getResult();
    }
    
    @SuppressWarnings("unchecked")
    public PagedResult<Recipe> searchByTags( List<Tag> tags, boolean allTags, Integer page, Integer pageSize )
    {
        String sql = "from recipe r";
        List<Object> params = new ArrayList<Object>();
        
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
        sql += " order by r.recipe_update desc";

        if ( tags != null && !tags.isEmpty())
        {
            for ( Tag tag : tags )
            {
                params.add( tag.getTag());
            }
            if ( allTags )
            {
                params.add( tags.size());
            }
        }

        return super.getPagedResult( "select r.* " + sql, "select count(*) " + sql, params, page, pageSize, true );
    }
    
    /**
     * Searchs for all the recipes with the selected shareIds.
     * @param shareIds An array containing the id's to search. If it's null or empty it returns an empty list
     * @return
     */
    @SuppressWarnings("unchecked")
	public PagedResult<Recipe> searchByShareId( String[] shareIds ) 
    {   	
    	String sql = " from Recipe r where r.shareId in ( ";
    	String separador = "";
    	for ( int i = 0; i < shareIds.length; i++  )
    	{
    		sql += separador + "?";
    		separador = ", ";
    	}
    	sql += " ) order by recipe_update desc";
    	
    	List params = new ArrayList();

    	for ( int i = 0; i < shareIds.length; i++  )
    	{
            params.add( shareIds[i]);
    	}
    	return getPagedResult("select r.*" + sql, "select count(*) " + sql, params, 1, null, false);
    	
    }
    
    /**
     * Imports a recipe from an external source into the BBDD 
     * @param recipe
     */
    public void importRecipe( Recipe recipe )
    {
    	Recipe aux;
    	
    	try
    	{
    		aux = ( Recipe )em.createNamedQuery("Recipe.findByRecipeShareId").setParameter("recipeShareId", recipe.getRecipeShareId()).getSingleResult();
    		aux.setRecipeTitle( recipe.getRecipeTitle());
    		aux.setRecipe( recipe.getRecipe() );
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
