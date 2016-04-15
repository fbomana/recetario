/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands;

import eu.infomas.annotation.AnnotationDetector;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aitkiar
 */
public class CommandFactory
{
    private static CommandFactory instance;
    protected Map<String, Class> commands;

    private CommandFactory() throws URISyntaxException, ClassNotFoundException, IOException 
    {
        commands = new HashMap<>();
        final AnnotationDetector cf = new AnnotationDetector( new Buscador( commands ));
        cf.detect("es/ait/recetario/desktop/commands");
    }
	
    public Command getCommand( String resource ) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	if ( commands.get( resource ) != null )
    	{
            return ( Command )commands.get( resource ).newInstance();
    	}
        else
        {
            // Check if its a REST url in the form resource/id
            String subresource = resource.substring(0, resource.lastIndexOf("/"));
            if ( commands.get( subresource ) != null )
            {
                return ( Command )commands.get( subresource ).newInstance();
            }
        }
        return ( Command ) Class.forName( "es.ait.recetario.desktop.commands" + resource.replaceAll("\\/", ".")).newInstance();
    }
    
    public static CommandFactory getInstance()
    {
    	if ( instance == null )
    	{
            try
            {
                instance = new CommandFactory();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
    	}
    	return instance;
    }
    
    class Buscador implements AnnotationDetector.TypeReporter
    {
        private Map<String, Class> commands;
        
        public Buscador( Map<String, Class> commands )
        {
            this.commands = commands;
        }

        @Override
        public Class<? extends Annotation>[] annotations()
        {
            return new Class[]{CommandPath.class};
        }

        @Override
        public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className)
        {
            System.out.println("Anotacion encontrada: " + className );
            try
            {
                Class<?> classObject = Class.forName( className );
                System.out.println("\t" + classObject.getAnnotation(CommandPath.class ).path());
                commands.put( classObject.getAnnotation(CommandPath.class ).path(), classObject );
            }
            catch ( ClassNotFoundException e )
            {
            }
        }
    }
}
