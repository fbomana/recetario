<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Recetario - new recipe</title>
    <link rel="stylesheet" href="../css/recetario.css">
    <link rel="stylesheet" href="../css/newrecipe.css">
    <script src="../js/vue.min.js"></script>
    <script src="../js/marked.min.js"></script>
    <script src="../js/recetario.js"></script>
</head>
<body>
<jsp:include page="../menu"/>
<div id="content">
<section id="formSection" style="left">
  <div class="contentBox">
    <h1>New Recipe</h1>
    <form method="post" action="${action}" name="newRecipe">
        <input type="hidden" name="id" id="id" value="${recipe.recipeId}"/>
        <label for="title">Title:<br>
            <input type="text" value="" required id="title" name="title"/>
        </label><br>
        <label for="contentEditor" id="editor">Content:<br>
            <textarea required id="contentEditor" name="contentEditor" v-model="input" debounce="300"></textarea>
            <div v-html="input | marked" id="preview"></div>
        </label><br>
        <label for="tags">Tags:<br>
            <input type="text" value="" required id="tags" pattern="[a-zA-Z0-9\-_]+(?:\s*,\s*[a-zA-Z0-9\-_]+)*" name="tags" onkeyup="loadTags()"/>
        </label><select id="availableTags" onChange="addTag();"></select><br>
        <br>
        <input type="submit" value="enviar"/>
    </form>
  </div>
</section>

<section id="previewSection" style="right">
    <div class="contentBox">
    <h1>Preview</h1>
    </div>
</section>
</div>
    <script>
        function init()
        {
            get("/recetario/services/recipe/session/load", null, function( status, xhr ) {
                if ( status )
                {
                    var recipe = JSON.parse( xhr.responseText );
                    if ( document.getElementById("id").value == "" || document.getElementById("id").value == recipe.id )
                    {
                        init2( recipe );
                    }
                    else
                    {
                         get("/recetario/services/recipe/" + document.getElementById("id").value, null, function( status, xhr ) {
                             if ( status )
                             {
                                 init2( JSON.parse( xhr.responseText ));
                             }
                         }, false );
                    }
                }
            }, false);
        }
        
        function init2( recipe )
        {
            document.getElementById("title").value = recipe.title;
            new Vue({
              el: '#editor',
              data: {
                input: (recipe.recipe ? recipe.recipe : "")
              },
              filters: {
                marked: marked
              }
            })
            if ( recipe.tags )
            {
                var separator = ""
                for ( var i = 0; i < recipe.tags.length; i++ )
                {
                    document.getElementById("tags").value += separator + recipe.tags[i];
                    separator = ", ";
                }
            }
            loadTags();
            resizeElements();
            setInterval( save, ${backupInterval});
        }

        function resizeElements( event )
        {
            document.getElementById("contentEditor").style.height = window.innerHeight / 2  + "px";
            document.getElementById("preview").style.top = document.getElementById("previewSection").getBoundingClientRect().top + "px";
            document.getElementById("preview").style.left = document.getElementById("previewSection").getBoundingClientRect().left + "px";
            var ancho = window.innerWidth - document.getElementById("previewSection").getBoundingClientRect().left;

            document.getElementById("preview").style.width = ( ancho -50 ) + "px";
            document.getElementById("preview").style.height = window.innerHeight * 3 / 4 + "px";
        }
        
        function save()
        {
            var parameters = new Array(0);
            parameters[0] = "recipe";
            parameters[1] = document.getElementById("contentEditor").value;
            parameters[2] = "tags";
            parameters[3] = document.getElementById("tags").value;            
            parameters[4] = "title";
            parameters[5] = document.getElementById("title").value;
            parameters[6] = "id";
            parameters[7] = document.getElementById("id").value;
            post( "../services/recipe/session/save", parameters, function ( status, xhr ) {
                if ( !status )
                {
                    alert( "Error saving draft:" + xhr.status );
                }
            });
        }
        
        function loadTags()
        {
            var parameters = new Array(0);
            parameters[0] = "tags";
            parameters[1] = document.getElementById( "tags" ).value;
            post("/recetario/services/tag/search/notin", parameters, function ( status, xhr){
                if ( status )
                {
                    var tags = JSON.parse( xhr.responseText );
                    var select = ClearOptionsFast( "availableTags");
                    select.options[0] = new Option("", "", true, true );
                    for ( var i = 0; i < tags.length; i ++ )
                    {
                        select.options[i+1] = new Option( tags[i].tag, tags[i].tag, false, false );
                    }
                }
            });
        }
        
        function addTag()
        {
            var select = document.getElementById("availableTags");
            if ( select.value !== "" )
            {
                var tags = document.getElementById("tags");
                if ( tags.value )
                {
                    tags.value += ", ";
                }
                tags.value += select.value;
                loadTags();
            }
            
            
        }
        
        window.onload = init;
        window.onresize = resizeElements;
    </script>
</body>
</html>