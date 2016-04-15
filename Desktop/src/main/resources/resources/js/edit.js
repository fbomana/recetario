angular.module("recetario").controller( "EditController", [ '$scope', 'recipeService', '$routeParams', '$window', '$http', function( $scope, recipeService, $routeParams, $window, $http ) {
    
    $scope.tag = "";
    $scope.tagString = "";
    $scope.recipe = { recipe : ''};
    $scope.errorMessage = "";
    
    $scope.save = function() {
        $scope.recipe.origin = $scope.preferences.recetarioName;
        $scope.recipe.tags = $scope.tagString.split(",").map( function( value, index, arr) {
            return value.trim();
        });
        recipeService.save( $scope.recipe ).then( function( response ) {
            $scope.errorMessage = "Receta grabada correctamente";
        }, function( response ) {
            $scope.errorMessage = response.statusText;
        });
    }
    
    $scope.loadTags = function() {
        $http({
            method: "GET",
            url: "http://localhost:8080/services/tags",
            params : { related : 1, tags : $scope.tagString }
              }).then( function( response ) {
            console.log("[INFO] tags recovery ok function. Recovered " + response.data.length );
            $scope.avaliableTags = response.data;
            $scope.tag="";
        }, function() {
            console.log("Error during tag recover: " + response.status );
            $scope.tag = "";
        });
    }
    
    $scope.addTag = function() {
        if ( $scope.tag != "")
        {
            if ( $scope.tagString != "" )
            {
                $scope.tagString += ", ";        
            }
            $scope.tagString += $scope.tag;
            $scope.loadTags();
        }
    }
    
    var init = function() {

        if ( $routeParams.id )
        {
            recipeService.getRecipe( $routeParams.id ).then( function( recipe ) {
                $scope.recipe = recipe;
                $scope.tagString = recipe.tags.reduce( function( total, item ) {
                    if ( !total )
                    {
                        return item;
                    }
                    else
                    {
                        return total + ", " + item;
                    }    
                });
                $scope.loadTags();
            }, function ( error ) {
                this.errorMessage = error;
            }); 
        }
        else
        {
            $scope.loadTags();
        }
        
        new Vue({
          el: '#editor',
          data: {
            input: ( $scope.recipe.recipe)
          },
          filters: {
            marked: marked
          }
        })
    }
    
    var resizeElements = function()
    {
        document.getElementById("contentEditor").style.height = window.innerHeight / 2  + "px";
        document.getElementById("preview").style.top = document.getElementById("previewSection").getBoundingClientRect().top + "px";
        document.getElementById("preview").style.left = document.getElementById("previewSection").getBoundingClientRect().left + "px";
        var ancho = window.innerWidth - document.getElementById("previewSection").getBoundingClientRect().left;

        document.getElementById("preview").style.width = ( ancho -50 ) + "px";
        document.getElementById("preview").style.height = window.innerHeight * 3 / 4 + "px";
    }
    
    var w = angular.element($window);
    w.bind( 'resize', resizeElements );
    $scope.$watch('$viewContentLoaded', resizeElements );
    
    init();
}]);