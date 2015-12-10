/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.handlers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class ErrorHandler implements BaseHandler
{

    @Override
    public void handle(String url, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
    {
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><tittle>Error</title></head><body><h1>Error:</h1>Don't know how to handle: <b>" + url + "</b></body></html>");
        out.close();
    }    

    @Override
    public String getContentType(String resource, HttpServletRequest request, HttpServletResponse response)
    {
        return "text/html;charset=utf-8";
    }
}
