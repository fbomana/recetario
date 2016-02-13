/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario;

import java.util.ArrayList;
import java.util.List;

import es.ait.recetario.model.Tag;


/**
 *
 * @author aitkiar
 */
public class Util
{

    /**
     * Transforms a coma separated string into a list of tag objects detached from
     * persistence.
     * 
     * @param string
     * @return 
     */
    public static List<Tag> String2Tags( String string )
    {
        List<Tag> result = new ArrayList<>();
        if ( string != null && !"".equals( string.trim()))
        {
            String[] tags = string.split(",");

            for ( String tag : tags )
            {
                result.add( new Tag( tag.trim().toLowerCase()));
            }
        }
        return result;
    }
}
