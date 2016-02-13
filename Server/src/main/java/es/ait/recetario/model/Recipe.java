/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author aitkiar
 */
@Entity
@Table(name = "recipe")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Recipe.findAll", query = "SELECT r FROM Recipe r"),
    @NamedQuery(name = "Recipe.findByRecipeId", query = "SELECT r FROM Recipe r WHERE r.recipeId = :recipeId"),
    @NamedQuery(name = "Recipe.findByRecipeTitle", query = "SELECT r FROM Recipe r WHERE r.recipeTitle = :recipeTitle"),
    @NamedQuery(name = "Recipe.findByRecipeDate", query = "SELECT r FROM Recipe r WHERE r.recipeDate = :recipeDate"),
    @NamedQuery(name = "Recipe.findByRecipeUpdate", query = "SELECT r FROM Recipe r WHERE r.recipeUpdate = :recipeUpdate"),
    @NamedQuery(name = "Recipe.findByRecipeOrigin", query = "SELECT r FROM Recipe r WHERE r.recipeOrigin = :recipeOrigin"),
    @NamedQuery(name = "Recipe.findByRecipeShareId", query = "SELECT r FROM Recipe r WHERE r.recipeShareId = :recipeShareId")
})
@JsonSerialize( using = RecipeJSONSerializer.class )
@JsonDeserialize( using = RecipeJSONDeserializer.class )
public class Recipe implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "recipe_id")
    private Integer recipeId;
    @Basic(optional = false)
    @Column(name = "recipe_title")
    private String recipeTitle;
    @Basic(optional = false)
    @Lob
    @Column(name = "recipe")
    private String recipe;
    @Basic(optional = false)
    @Column(name = "recipe_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recipeDate;
    @Column(name = "recipe_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recipeUpdate;
    @Basic(optional = false)
    @Column(name = "recipe_origin")
    private String recipeOrigin;
    @Column(name = "recipe_share_id")
    private String recipeShareId;
    @JoinTable(name = "recipe_tags", joinColumns =
    {
        @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id")
    }, inverseJoinColumns =
    {
        @JoinColumn(name = "tag", referencedColumnName = "tag")
    })
    @ManyToMany
    private List<Tag> tagsList;

    public Recipe()
    {
    }

    public Recipe(Integer recipeId)
    {
        this.recipeId = recipeId;
    }

    public Recipe(Integer recipeId, String recipeTitle, String recipe, Date recipeDate, String recipeOrigin)
    {
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipe = recipe;
        this.recipeDate = recipeDate;
        this.recipeOrigin = recipeOrigin;
    }

    public Integer getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId)
    {
        this.recipeId = recipeId;
    }

    public String getRecipeTitle()
    {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle)
    {
        this.recipeTitle = recipeTitle;
        calculateShareId();
    }

    public String getRecipe()
    {
        return recipe;
    }

    public void setRecipe(String recipe)
    {
        this.recipe = recipe;
    }

    public Date getRecipeDate()
    {
        return recipeDate;
    }

    public void setRecipeDate(Date recipeDate)
    {
        this.recipeDate = recipeDate;
        calculateShareId();
    }

    public Date getRecipeUpdate()
    {
        return recipeUpdate;
    }

    public void setRecipeUpdate(Date recipeUpdate)
    {
        this.recipeUpdate = recipeUpdate;
    }

    public String getRecipeOrigin()
    {
        return recipeOrigin;
    }

    public void setRecipeOrigin(String recipeOrigin)
    {
        this.recipeOrigin = recipeOrigin;
        calculateShareId();
    }

    public String getRecipeShareId()
    {
        calculateShareId();
        return recipeShareId;
    }

    public void setRecipeShareId(String recipeShareId)
    {
        this.recipeShareId = recipeShareId;
    }

    @XmlTransient
    public List<Tag> getTagsList()
    {
        return tagsList;
    }

    public void setTagsList(List<Tag> tagsList)
    {
        this.tagsList = tagsList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (recipeId != null ? recipeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recipe))
        {
            return false;
        }
        Recipe other = (Recipe) object;
        if ((this.recipeId == null && other.recipeId != null) || (this.recipeId != null && !this.recipeId.equals(other.recipeId)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "es.ait.recetario.model.Recipe[ recipeId=" + recipeId + " ]";
    }
    
    private void calculateShareId()
    {
        if ( recipeShareId == null && recipeDate != null && recipeOrigin != null && recipeTitle != null)
        {
            try
            {
                String shareId = recipeOrigin + recipeTitle + new SimpleDateFormat("yyyyMMddHHmmss").format( recipeDate );
                recipeShareId = Base64.getEncoder().encodeToString( shareId.getBytes("UTF-8"));
            }
            catch ( UnsupportedEncodingException e )
            {
                // do nothing if utf-8 is not supported.
            }
        }
    }
}
