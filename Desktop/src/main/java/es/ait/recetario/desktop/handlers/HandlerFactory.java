package es.ait.recetario.desktop.handlers;

import es.ait.recetario.desktop.Utils;

/**
 * This class uses the method getHandler to produce the appropiate handler for a
 * request url.
 *
 * @author aitkiar
 */
public class HandlerFactory
{
    public static BaseHandler getHandler( String resource )
    {
        if ( Utils.getExtenxion( resource ).equals(""))
        {
            return new CommandHandler();
        }
        return new StaticHandler();
    }
}
