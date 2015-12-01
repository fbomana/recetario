/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;

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
    public static String exceptionToHTMLString( Exception e )
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( bos );
        e.printStackTrace(ps);
        ps.close();
        String error = bos.toString();
        error = "<!DOCTYPE html>"
                + "<html>"
                + "<head><link rel='stylesheet' href='/css/recetario.css'></head>"
                + "<body class='error'>"
                + "<pre>"
                + error
                + "</pre>"
                + "</body>"
                + "</html>";
        return error;
    }
    
    /**
     * Returns and estandar html page with the contents passed as parameter
     * @param body
     * @return 
     */
    public static String contentsToHtml( String contents )
    {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head><link rel='stylesheet' href='/css/recetario.css'></head>"
                + "<body>"
                + ( contents != null ? contents : "") 
                + "</body>"
                + "</html>";
    }
    
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
}
