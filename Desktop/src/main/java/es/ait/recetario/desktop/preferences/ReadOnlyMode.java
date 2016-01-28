/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.preferences;

/**
 *
 * @author aitkiar
 */
public enum ReadOnlyMode
{
    FULLEDIT(0),
    LOCALEDIT(1),
    READONLY(2);
    
    private final int mode;
    
    ReadOnlyMode( int mode )
    {
        this.mode = mode;
    }
    
    public int getMode()
    {
        return this.mode;
    }
    
    public static ReadOnlyMode getMode( String mode )
    {
        switch( mode )
        {
            case "0":
                return FULLEDIT;
            case "1":
                return LOCALEDIT;
            case "2":
                return READONLY;
            default:
                return READONLY;
        }
    }
}
