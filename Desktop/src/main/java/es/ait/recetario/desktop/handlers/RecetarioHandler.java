/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.handlers;

import es.ait.recetario.desktop.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
        String contentType = "";
        switch ( Utils.getExtenxion( resource ))
        {
            case "gif":
                contentType = "image/gif";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "jpg":
                contentType = "image/jpeg";
                break;
            case "js":
                contentType = "text/javascript;charset=utf-8;";
                break;
            case "css":
                contentType = "text/css;charset=utf-8;";
                break;
            default:
                contentType = "text/html;charset=utf-8";
                break;               
        }
        
        response.setContentType( contentType );
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled( true );

        HandlerFactory.getHandler(resource).handle( resource, request, response );

    }
}
