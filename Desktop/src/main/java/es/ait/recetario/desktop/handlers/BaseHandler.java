/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.handlers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public interface BaseHandler
{
    public void handle( String resource, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException;
    
    public String getContentType( String resource, HttpServletRequest request, HttpServletResponse response ) throws ServletException;
}
