/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands;

/**
 *
 * @author aitkiar
 */
public class CommandFactory
{
    public static Command getCommand( String resource ) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        return ( Command ) Class.forName( "es.ait.recetario.desktop.commands" + resource.replaceAll("\\/", ".")).newInstance();
    }
}
