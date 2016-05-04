'use strict';

angular.module("recetario").controller("SyncController", ['$scope', 'recipeService', '$http', function( $scope, recipeService, $http) {
    $scope.base="";
    $scope.tagString="";
    $scope.tag="";
    $scope.tags=[];
    $scope.syncAll = "all";
    $scope.selectedRecipes=[];
    $scope.availableRecipes=[];
    $scope.availableRecipesSel=[];
    $scope.selectedRecipesSel=[];
    $scope.error = "";
    $scope.recipesToImport=0;
    $scope.recipesImported=0;
    
    $scope.loadTags = function() {
        $http({
            method: "GET",
            url: $scope.base + "/services/tags",
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
        }
    }
    
    $scope.searchRecipes = function() {
        if ( $scope.base !== "" && fsync.baseUrl.validity.valid )
        {
            recipeService.getRecipes( $scope.tagString, 1, 1, 100, $scope.base ).then( function( recipes ) {
                $scope.availableRecipes = recipes.recipes;
                $scope.availableRecipesSel = [];
            }, function( response ) {
                $scope.error = response.statusText;
            });
        }
    }
    
    $scope.selectRecipes = function() {
        var auxRecipe;
        for ( var i = $scope.availableRecipesSel.length - 1; i >= 0; i --)
        {
            auxRecipe = $scope.availableRecipesSel[i];
            $scope.selectedRecipes.push( auxRecipe );
            $scope.availableRecipesSel.pop();
            $scope.availableRecipes.splice($scope.availableRecipes.indexOf( auxRecipe ), 1 );
        }
    }
    
    $scope.unselectRecipes = function() {
        var auxRecipe;
        for ( var i = $scope.selectedRecipesSel.length - 1; i >= 0; i --)
        {
            auxRecipe = $scope.selectedRecipesSel[i];
            $scope.availableRecipes.push( auxRecipe );
            $scope.selectedRecipesSel.pop();
            $scope.selectedRecipes.splice($scope.selectedRecipes.indexOf( auxRecipe ), 1 );
        }
    }
    
    $scope.importRecipes = function() {
        if ( $scope.base !== "" && fsync.baseUrl.validity.valid  )
        {
            if ( $scope.syncAll === "all" ) 
            {
                recipeService.getRecipes("", 1, null, null, $scope.base ).then( function( data ) {
                    $scope.continueImport( data.recipes );
                });
            }
            else
            {
                $scope.continueImport( $scope.selectedRecipes );
            }
        }
    }
    
    $scope.continueImport = function( recipes )
    {
        $scope.recipesToImport = recipes.length;
        for ( var i = 0; i < recipes.length; i++ )
        {
            $scope.importRecipe( recipes[i] );
        }
    }
    
    $scope.importRecipe = function( recipe ) {
        recipeService.getRecipe( recipe.id, $scope.base ).then( function( wholeRecipe ) {
            recipeService.import( wholeRecipe ).then( function() {
               $scope.recipesImported ++; 
            });
        });
    }
}]);