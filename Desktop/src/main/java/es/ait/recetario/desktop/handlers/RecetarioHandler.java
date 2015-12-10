/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.handlers;

import es.ait.recetario.desktop.Utils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 *
 * @author aitkiar
 */
public class RecetarioHandler extends AbstractHandler
{
    @Override
    public void handle(String resource, Request baseRequest, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
    {
        BaseHandler handler = HandlerFactory.getHandler( resource );
        response.setContentType( handler.getContentType(resource, request, response) );
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled( true );

        handler.handle( resource, request, response );

    }
}
