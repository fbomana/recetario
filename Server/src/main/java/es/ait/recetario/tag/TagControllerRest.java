/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.tag;

import es.ait.recetario.Util;
import es.ait.recetario.model.Tag;
import es.ait.recetario.model.TagDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author aitkiar
 */
@RestController
@RequestMapping("/services/tag")
public class TagControllerRest
{
    @Autowired
    private TagDAO tagDAO;
    
    /**
     * search for all the tags that are not in the tags parameter.
     * @param tags
     * @return
     */
    @RequestMapping( path = "/search/notin", produces = "application/json")
    public List<Tag> getTagsNotIn( @Param("tags") String tags )
    {
        return tagDAO.searchNotIn( Util.String2Tags(tags));
    }
}
