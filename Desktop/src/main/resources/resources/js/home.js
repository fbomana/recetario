recetarioModule.controller("HomeController", ['$scope', '$http', '$rootScope', '$timeout','recipeService', '$location', function($scope, $http, $rootScope, $timeout, recipeService, $location )
{
    $scope.tag = "";
    $scope.tagString = "";
    $scope.tags =[];
    $scope.recipeDetail = null;
    $scope.showDetail = false;
    $scope.searchType = 1;
    
    $scope.loadTags = function() {
        $http({
            method: "GET",
            url: "http://localhost:8080/services/tags",
            params : { related : 1, tags : $scope.tagString }
              }).then( function( response ) {
            console.log("[INFO] tags recovery ok function. Recovered " + response.data.length );
            $scope.tags = response.data;
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
            $scope.resetRecipes();
        }
    }
    
    $scope.searchRecipes = function()  {
        recipeService.getRecipes( $scope.tagString, $scope.searchType, $scope.page + 1, $scope.preferences.recipesPerPage ).
            then( function( data ) {
                console.log("[INFO]Se encuentran recetas");
                for ( var i = 0; i < data.recipes.length; i ++ )
                {
                    $scope.recipes.push( data.recipes[i] );
                }
                $scope.page = data.page;
                $scope.totalPages = data.totalPages;
        }, function ( response ){
        })
    }
    
    $scope.openDetail = function( recipe )
    {
        recipeService.getRecipe( recipe.id ).then( function( data ){
            $scope.recipeDetail = data;
            $scope.showDetail = true;
        }, function( data ){
            $scope.recipeDetail = false;
            $scope.showDetail = false;
        });
    }
    
    $scope.closeDetail = function() {
        $scope.showDetail = false;
    }
    
    $scope.resetRecipes = function() {
        $scope.page = 0;
        $scope.totalPages = 0;
        $scope.recipes = [];
        $scope.searchRecipes();
    }
    
    $scope.delete = function( id ) {
        recipeService.delete( id ).then( function( response ) {
            $scope.resetRecipes();
        }, function( response ) {
            console.log( "error");
        });
    }
    
    // Retrasamon la inicialización a que las preferencias estén cargadas.
    if ( $scope.recetarioJSinitialized )
    {
        $scope.loadTags();
        $scope.resetRecipes();
    }
    else
    {
        $scope.$watch($scope.recetarioJSinitialized, function() {
            if ( $scope.recetarioJSinitialized )
            {
                $scope.loadTags();
                $scope.resetRecipes();
            }
        });
    }
}]);
