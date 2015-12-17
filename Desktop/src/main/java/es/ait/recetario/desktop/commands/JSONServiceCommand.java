/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands;

/**
 * Abstracrt class to handle commands thar works as "json rest services."
 * @author aitkiar
 */
public abstract class JSONServiceCommand extends Command
{
    
    @Override
    public String getContentType( String source )
    {
        return "application/json;charset=utf-8";
    }
}
