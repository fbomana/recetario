/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstracrt class to handle commands thar works as "json rest services."
 * @author aitkiar
 */
public abstract class JSONServiceCommand extends Command
{
 
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
//        response.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS,HEAD");
//        response.setHeader("Access-Control-Allow-Header", "X-Requested-With");
        switch ( request.getMethod() )
        {
            case "GET":
                get( request, response, out );
                break;
            case "PUT":
                put( request, response, out );
                break;
            case "POST":
                post( request, response, out );
                break;
            case "DELETE":
                delete( request, response, out );
                break;
            case "HEAD":
            case "OPTIONS":
                break;
            default:
                response.sendError(405, "Method no allowed");
        }
    }
    
    protected void get(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        response.sendError(405, "Method no allowed");
    }
    
    protected void post(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        response.sendError(405, "Method no allowed");
    }
    
    protected void put(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        response.sendError(405, "Method no allowed");
    }
    
    protected void delete(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        response.sendError(405, "Method no allowed");
    }
            
    @Override
    public String getContentType( String source )
    {
        return "application/json;charset=utf-8";
    }
}
