package es.ait.recetario.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class RecipeJSONDeserializer extends JsonDeserializer<Recipe>
{

	@Override
	public Recipe deserialize( JsonParser parser, DeserializationContext context )
			throws IOException, JsonProcessingException
	{
		System.out.println("deserializer");
		SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss");
		Recipe recipe = new Recipe();
		JsonNode node = parser.getCodec().readTree( parser );
		recipe.setRecipeId( node.get("id").asInt());
		recipe.setRecipeTitle( node.get( "title").asText() );
		recipe.setRecipe( node.get( "recipe" ).asText());
		try
		{
			recipe.setRecipeDate( sdf.parse( node.get("date").asText()));
			recipe.setRecipeUpdate( sdf.parse( node.get("update").asText()));
		}
		catch ( ParseException e )
		{
			throw new IOException( e ); 
		}
		recipe.setRecipeOrigin( node.get("origin").asText());
		recipe.setRecipeShareId( node.get("shareId").asText());
		recipe.setTagsList( new ArrayList<Tag>())	;
		
		if ( node.get("tags").isArray() )
		{
			for ( JsonNode tag : node.get("tags"))
			{
				recipe.getTagsList().add( new Tag( tag.asText()));
			}
		}
		
		return recipe;
	}

}
