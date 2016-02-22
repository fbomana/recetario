/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands;

import eu.infomas.annotation.AnnotationDetector;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
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
        URL url = this.getClass().getClassLoader().getResource( "es/ait/recetario/desktop/commands");
        System.out.println( "URL:"+ url.toString());
        if ( url != null )
        {
            if ( "jar".equals( url.getProtocol()) )
            {
                final AnnotationDetector cf = new AnnotationDetector( new Buscador( commands ));
                cf.detect("es/ait/recetario/desktop/commands");
            }
            else
            {
                scanPackage( new File( url.toURI()), "es.ait.recetario.desktop.commands");
            }
        }
    }
	
    public Command getCommand( String resource ) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	if ( commands.get( resource ) != null )
    	{
            return ( Command )commands.get( resource ).newInstance();
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
  
    public void scanPackage( File folder, String packageName ) throws URISyntaxException, ClassNotFoundException
    {
    	System.out.println("package: " + packageName );
    	File[] files = folder.listFiles();
    	for ( File file : files )
    	{
            if ( file.isDirectory() )
            {
                scanPackage( file, packageName + "." + file.getName());
            }
            else if ( file.getName().endsWith(".class"))
            {
                System.out.println("\tclass: " + file.getAbsolutePath() );
                Class<?> classObject = Class.forName( packageName + "." + file.getName().substring( 0, file.getName().lastIndexOf(".class")));
                Annotation[] annotations = classObject.getAnnotations();
                for ( Annotation annotation : annotations )
                {
                    System.out.println("\t\t" + annotation.annotationType().getName());
                }
                if ( classObject.isAnnotationPresent( CommandPath.class ))
                {
                    commands.put( classObject.getAnnotation(CommandPath.class ).path(), classObject );
                }
            }
    	}
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
