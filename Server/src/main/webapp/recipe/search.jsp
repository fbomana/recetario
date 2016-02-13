<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Recetario - search recipes</title>
    <link rel="stylesheet" href="../css/recetario.css">
    <link rel="stylesheet" href="../css/searchRecipe.css">
    <script src="../js/marked.min.js"></script>
    <script src="../js/recetario.js"></script>
</head>
<body>
    <jsp:include page="../menu"/>
    <div id="content">
    <section>
    <form method="post" action="/recipes/SearchRecipes" onsubmit="searchRecipes();return false;">
        <label for="tags">Tags:
            <input type="text" value="" id="tags" name="tags" pattern="(?:[a-zA-Z0-9\-_]+(?:\s*,\s*[a-zA-Z0-9\-_]+)*)" onchange="checkEmpty( this );" />
        </label>
        <select id="availableTags" onChange="addTag();"></select>
        <input type="button" value="search" onclick="searchRecipes();"><br>
        <label for="searchType">All tags<input id="searchType" name="searchType" type="checkbox" checked value="1"></label>
    </form>
    </section>
    <section>
        <h1>Recipes</h1>
        <div id="recipes">

        </div>
    </section>
    </div>
    <div id="recipeDetail"></div>
<script>
    window.onload = init;
    var canEdit = true;
    
    function init()
    {
        loadTags();
        searchRecipes();
    }
    
    function checkEmpty( text )
    {
        loadTags();
        return true;
    }
    
    function addTag()
    {
        var text = document.getElementById( "tags" );
        if ( text.value )
        {
            text.value += ", ";
        }
        text.value += document.getElementById( "availableTags" ).value;
        loadTags();
    }
    
    function loadTags()
    {
        //disableForm();
        
        var paramArray = new Array();
        paramArray[0] = "tags";
        paramArray[1] = document.getElementById( "tags" ).value;
        if ( document.getElementById( "searchType" ).checked )
        {
            paramArray[2] = "related";
            paramArray[3] = "1";
        }
        
        post( '../services/tag/search/notin', paramArray, function ( ok, xhr ) {
            if ( ok ) 
            {
                var tags = JSON.parse( xhr.responseText );
                var select = ClearOptionsFast( "availableTags");
                select.options[0] = new Option("", "", true, true );
                for ( var i = 0; i < tags.length; i ++ )
                {
                    select.options[i+1] = new Option( tags[i].tag, tags[i].tag, false, false );
                }
                searchRecipes();
            } 
            else 
            {
                alert('Error: ' + xhr.status);
            }
            //enableForm();
        });        
    }
    
    function searchRecipes( id )
    {
        disableForm();
        
        var paramArray = new Array();
        if ( id )
        {
            paramArray[0] = "id";
            paramArray[1] = "" + id;
        }
        else
        {
            paramArray[0] = "tags";
            paramArray[1] = document.getElementById( "tags" ).value;
            paramArray[2] = "searchType";
            paramArray[3] = document.getElementById( "searchType" ).checked ? "true" : false;
        }
        
        post( '../services/recipe/search', paramArray, function ( ok, xhr ) {
            if ( ok ) 
            {
                printRecipeList( JSON.parse( xhr.responseText ));
            } 
            else 
            {
                alert('Error: ' + xhr.status);
            }
            enableForm();
        });
    }
    
    function disableForm()
    {
        document.getElementById("tags").disabled = true;
        document.getElementById("availableTags").disabled = true;
    }
    
    function enableForm()
    {
        document.getElementById("tags").disabled = false;
        document.getElementById("availableTags").disabled = false;
    }
    
    function printRecipeList( recipes )
    {
        var div = document.getElementById('recipes');
        while(div.firstChild)
        {
            div.removeChild(div.firstChild);
        }
        var i = 0;
        while ( i < recipes.length )
        {
            div.appendChild ( printRecipeListItem( recipes[i]));
            i++;
        }
    }
    
    function printRecipeListItem( recipe )
    {
        var div = document.createElement("div");
        div.className="recipe";
        
        var enlace = document.createElement("a");
        enlace.href="javascript:showRecipe(" + recipe.id + ");";
        enlace.appendChild( document.createTextNode( recipe.title ) );
        div.appendChild( enlace );
        
        if ( canEdit )
        {
            var icons = document.createElement("span");
            icons.className="right";

            var editIconLink = document.createElement("a");
            editIconLink.href= "edit?id=" + recipe.id
            var editIcon = document.createElement("img");
            editIcon.src="../img/edit.png"
            editIconLink.appendChild( editIcon );

            var deleteIconLink = document.createElement("a");
            deleteIconLink.href="javascript:deleteRecipe(" +  recipe.id + ");";
            var deleteIcon = document.createElement("img");
            deleteIcon.src="../img/delete.png"
            deleteIconLink.appendChild( deleteIcon );
            icons.appendChild( editIconLink );
            icons.appendChild( deleteIconLink );
            div.appendChild( icons );
        }
        
        div.appendChild( document.createElement( "br"));
        
        var tagsText = "Tags: ";
        for ( var i = 0; i < recipe.tags.length; i ++ )
        {
            if ( i !== 0 )
            {
                tagsText += ", ";
            }
            tagsText += recipe.tags[i];
        }
        div.appendChild( document.createTextNode( tagsText ));
        var span = document.createElement("span");
        span.className="right";
        span.appendChild( document.createTextNode("Update: " + new Date( recipe.update )));
        div.appendChild( span );
        return div;
    }
    
    function showRecipe( id )
    {       
        post( '../services/recipe/' + id, null, function ( ok, xhr ) {
            if ( ok ) 
            {
                printRecipe( JSON.parse( xhr.responseText ));
            } 
            else 
            {
                alert('Error: ' + xhr.status);
            }
        });
    }
    
    function printRecipe( recipe )
    {
        var div = ClearOptionsFast("recipeDetail");
        div.style.display="none";
        
        // Dimensions
        var divWidth = Math.max( 500, recetario_x / 2 );
        var divHeight = Math.max( 400, recetario_y / 2 );
        div.style.top = ( recetario_y - divHeight ) / 2 + "px";
        div.style.left =  ( recetario_x - divWidth )/ 2 + "px";
        div.style.width = divWidth + "px";
        div.style.height = divHeight + "px";
        
        var span = document.createElement("span");
        span.className="right";
        span.appendChild( document.createTextNode("X"));
        span.onclick = closeRecipe;
        span.style.cursor = "pointer";
        div.appendChild( span );
        
        var h1 = document.createElement("h1");
        h1.appendChild( document.createTextNode( recipe.title ));
        div.appendChild( h1 );
        
        var recipeDiv = document.createElement("div");
        recipeDiv.style.width = "calc(" + divWidth + "px - 1em)";
        recipeDiv.style.height = "calc(" + divHeight + "px - 4em - 50px)";
        recipeDiv.className = "recipeDiv";
        recipeDiv.innerHTML = marked( recipe.recipe );
        div.appendChild( recipeDiv );
        
        var tagsSpan = document.createElement("span");
        tagsSpan.className="tags";
        
        var tagsText = "Tags: "; 
        for ( var i = 0; i < recipe.tags.length; i ++ )
        {
            if ( i > 0 )
            {
                tagsText += ", ";
            }
            tagsText += recipe.tags[i];
        }
        tagsSpan.appendChild(document.createTextNode( tagsText ));
        div.appendChild( tagsSpan );
        
        
        div.style.display="block";
    }
    
    function closeRecipe()
    {
        var div = ClearOptionsFast("recipeDetail");
        div.style.display = "none";
    }
    
    function deleteRecipe( id )
    {
        if ( confirm("Deleted recipes can't be recovered. Are you sure ?") )
        {
            window.location.href="delete/" + id ;
        }
    }
</script>
</body>
</html>
