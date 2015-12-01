package es.ait.recetario.desktop.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class handles the static content on the application. Finds the file in 
 * the jar of the application and writes it to the output.
 * @author aitkiar
 */
public class StaticHandler implements BaseHandler
{
    /**
     * * handle binary based request.
     * 
     * @param resource the resource to return
     * @param request the request made
     * @param response the object that encapsulate the response
     * @param out the initialized response output.
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    public void handle(String resource, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
    {
        InputStream is = null;
        ServletOutputStream out = null;
        try
        {
            out = response.getOutputStream();
            is = getClass().getResourceAsStream("/resources" + resource );
            byte[] buffer = new byte[4096];
            int read;
            while ( ( read = is.read(buffer)) > 0 )
            {
                out.write( buffer, 0, read );
            }
        }
        finally
        {
            if ( is != null )
            {
                is.close();
            }
            out.close();
        }
    }
    
}
