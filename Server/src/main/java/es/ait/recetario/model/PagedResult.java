/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.model;

import java.util.List;

/**
 * This class encapsulates the result of a query with paging. It contains the info necesary to navigate through the pages
 * of results, and the selected subset of results.
 * 
 * @param <T>
 */
public class PagedResult<T>
{
    private Integer count;
    private Integer pageSize;
    private Integer pageNumber;
    private Integer numPages;
    
    private List<T> result;

    /**
     * @return the count
     */
    public Integer getCount()
    {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Integer count)
    {
        this.count = count;
    }

    /**
     * @return the pageSize
     */
    public Integer getPageSize()
    {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * @return the pageNumber
     */
    public Integer getPageNumber()
    {
        return pageNumber;
    }

    /**
     * @param pageNumber the pageNumber to set
     */
    public void setPageNumber(Integer pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    /**
     * @return the numPages
     */
    public Integer getNumPages()
    {
        return numPages;
    }

    /**
     * @param numPages the numPages to set
     */
    public void setNumPages(Integer numPages)
    {
        this.numPages = numPages;
    }

    /**
     * @return the result
     */
    public List<T> getResult()
    {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<T> result)
    {
        this.result = result;
    }
    
}
