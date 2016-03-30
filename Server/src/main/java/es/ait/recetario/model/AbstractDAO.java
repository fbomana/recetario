/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.model;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Class with the basic structure to run querys and return structures that can be paged.
 * @author aitkiar
 * @param <T>
 */
public class AbstractDAO<T>
{
    @PersistenceContext(unitName = "recetarioPU")
    protected EntityManager em;
    
    protected PagedResult getPagedResult( String jpqlBusqueda, String jpqlCuenta, List<Object> parameters, Integer page, Integer pageSize, boolean isNative )
    {
        Integer total = new Integer( getCountQuery(jpqlCuenta, parameters, isNative ).getSingleResult().toString());
        
        PagedResult<T> result = new PagedResult<>();
        result.setCount( total );
        result.setPageSize( pageSize );
        if ( page == null )
        {
            page = 1;
        }
        result.setPageNumber( page );
        if ( pageSize != null && pageSize != 0 )
        {
            result.setNumPages( new Double( Math.ceil( 1.0 * total / pageSize )).intValue() );
        }
        else
        {
            result.setNumPages( 1 );
        }
        
             
        if ( (total > 0 && result.getNumPages() < page ) || total == 0  )
        {
            result.setResult(new ArrayList<T>());
        }
        else
        {
            Query query = getQuery( jpqlBusqueda, parameters, isNative );
            if ( pageSize != null )
            {
                query.setFirstResult( ( page - 1) * pageSize );
                query.setMaxResults( pageSize );
            }
            result.setResult(query.getResultList());
        }
        return result;
    }
    
    protected Query getCountQuery( String jpql, List<Object> parameters, boolean isNative )
    {
        Query query =  isNative ? em.createNativeQuery(jpql) : em.createQuery( jpql );
        for ( int i = 0; parameters != null && i < parameters.size(); i++ )
        {
            query = query.setParameter(i+1, parameters.get( i ));
        }
        return query;
    }

    
    protected Query getQuery( String jpql, List<Object> parameters, boolean isNative )
    {
        Class<T> resultClass = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        Query query =  isNative ? em.createNativeQuery(jpql, resultClass) : em.createQuery( jpql );
        for ( int i = 0; parameters != null && i < parameters.size(); i++ )
        {
            query = query.setParameter(i+1, parameters.get( i ));
        }
        return query;
    }
    
}
