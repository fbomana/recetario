/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aitkiar
 */
@Entity
@Table(name = "tags")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Tag.findAll", query = "SELECT t FROM Tag t"),
    @NamedQuery(name = "Tag.findByTag", query = "SELECT t FROM Tag t WHERE t.tag = :tag")
})
public class Tag implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "tag")
    private String tag;
//    @ManyToMany(mappedBy = "tagsList")
//    private List<Recipe> recipeList;

    public Tag()
    {
    }

    public Tag(String tag)
    {
        this.tag = tag;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

//    @XmlTransient
//    public List<Recipe> getRecipeList()
//    {
//        return recipeList;
//    }
//
//    public void setRecipeList(List<Recipe> recipeList)
//    {
//        this.recipeList = recipeList;
//    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (tag != null ? tag.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tag))
        {
            return false;
        }
        Tag other = (Tag) object;
        if ((this.tag == null && other.tag != null) || (this.tag != null && !this.tag.equals(other.tag)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "es.ait.recetario.model.Tags[ tag=" + tag + " ]";
    }
    
}
