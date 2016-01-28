/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.templates;

import es.ait.recetario.desktop.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * Class to read html templates and transform them in an html string.
 * @author aitkiar
 */
public class TemplateFactory
{
    /**
     * Returns the template in /resources/html and substitutes all properties in the template.
     * 
     * @param name name of the template. Can contain a URL relative to /resources/html
     * @param properties the values to substitute in the template.
     * @return a String with the html
     * @throws IOException 
     */
    public static String getRawTemplate( HttpServletRequest request, String name, Properties properties ) throws IOException
    {
        BufferedReader br = null;
        InputStreamReader isr = null;
        try
        {
            isr = new InputStreamReader( TemplateFactory.class.getResourceAsStream( "/resources/html/" + name ) );
            br = new BufferedReader( isr );
            String line;
            String template = "";
            while ( (line = br.readLine()) != null )
            {
                template += line + "\n";
            }

            if ( properties == null )
            {
                properties = new Properties();
            }
            
            // We don't overwirte the property canEdit if it hava a previous value
            if ( properties.getProperty("canEdit", null ) == null )
            {
                properties.setProperty("canEdit", Utils.canEdit( request ) + "");
            }
            for ( Object key : properties.keySet().toArray( new String[1]))
            {
                template = template.replaceAll( Pattern.quote("{" + key.toString() + "}"), properties.get( key ).toString());
            }
            
            return template;
        }
        finally
        {
            if ( br != null )
            {
                br.close();
            }
            if ( isr != null )
            {
                isr.close();
            }
        }
    }
    
    /**
     * Transform the template in /resources/html/<name> into a String containing
     * an html page. It also substitutes all properties in the template.
     * 
     * @param name name of the template. Can contain a URL relative to /resources/html
     * @param properties the values to substitute in the template.
     * @return a String with the html
     * @throws IOException 
     */
    public static String getTemplate( HttpServletRequest request, String name, Properties properties ) throws IOException
    {
        return Utils.contentsToHtml( request, getRawTemplate( request, name, properties ));
    }
}
