/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop;

import es.ait.recetario.desktop.templates.TemplateFactory;
import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aitkiar
 */
public class Utils
{
    /**
     * Returns the extension of a file name in lowercase.
     * @param fileName the file name
     * @return the extension lowercased, or "" if no extension found.
     */
    public static String getExtenxion( String fileName )
    {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) 
        {
            extension = fileName.substring( i + 1 );
        }
        return extension.toLowerCase();
    }
    
    /**
     * Transform an exception stack trace in an html document that can be printed
     * on a HttpServeltResponse.
     * @param e
     * @return 
     */
    public static String exceptionToHTMLString( Exception e ) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( bos );
        e.printStackTrace(ps);
        ps.close();
        String error = bos.toString();
        return TemplateFactory.getRawTemplate("errortemplate.html", null).replace("----------------",  error );
    }
    
    /**
     * Returns and estandar html page with the contents passed as parameter
     * @param body
     * @return 
     */
    public static String contentsToHtml( String contents ) throws IOException
    {
        return TemplateFactory.getRawTemplate("basictemplate.html", null).replace("----------------",  contents != null ? contents : "");
    }
    
    /**
     * Opens the url in the default system browser.
     * @param url the url open.
     * @throws IOException if security restrictions doesn't allow to open external browser.
     * @throws URISyntaxException if the url isn't properly constructed.
     */
    public static void browse( String url ) throws IOException, URISyntaxException
    {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if ( desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
        {
            desktop.browse( new URI( url ) );
        }
        else 
        {
            new ProcessBuilder("x-www-browser", url).start();
        }
    }
    
    /**
     * Splits a coma separated lists and retruns a List containing al not void parts
     * trimmed and lowercased.
     * @param string the string to split.
     * @return Tre list. An empty list is returned if there are no coma separated tags.
     */
    public static List<String> string2tags( String string )
    {
        return splitString( string, true );
    }
    
    public static List<String> splitString( String string, boolean toLowerCase )
    {
        List<String> result = new ArrayList<>();
        if ( string != null )
        {
            String[] tags = string.split(",");
            for ( String tag : tags )
            {
                if ( !"".equals( tag.trim()))
                {
                    if ( toLowerCase )
                    {
                        result.add( tag.trim().toLowerCase());
                    }
                    else
                    {
                        result.add( tag.trim());
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Turns a list of strings in a coma separated string.
     * @param tags
     * @return 
     */
    public static String tags2string( List<String> tags )
    {
        String result = ""; 
        String separator = "";
        for ( String tag : tags )
        {
            result += separator + tag;
            separator = ", ";
        }
        return result;
    }
}
