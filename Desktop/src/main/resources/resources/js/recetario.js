var recetarioModule = angular.module("recetario",['ngSanitize', 'ngRoute']);

recetarioModule.config( ['$routeProvider', function($routeProvider) {
    $routeProvider.when('/home', {
        templateUrl : 'home.html',
        controller : 'HomeController'
    }).when ('/config', {
        templateUrl : 'config.html',
        controller : 'ConfigController'
    }).otherwise('/home');
}]);

recetarioModule.directive("mainMenu", [ '$window', function( $window ) {
    return { 
        template : "<span id='search' ng-class='getSelectedClass(0)'><a ng-href='#/home'>Search Recipes</a></span>" +
                "<span id='new' ng-class='getSelectedClass(1)' ng-show='getCanEdit()'><a href='/recipes/NewRecipe'>New Recipe</a></span>" +
                "<span id='synchronize' ng-class='getSelectedClass(2)' ng-show='getCanEdit()'><a href='/recipes/SynchronizeRecipes'>Synchronize</a></span>" + 
                "<span id='configuration' ng-class='getSelectedClass(3)' ng-show='getCanEdit()'><a ng-href='#/config'>Configuration</a></span>",
        restrict : "A",
        scope : {
            canEdit : "@mainMenu"
        },
        link: function (scope, element, attributes) {
            scope.getSelectedClass = function( linkId )
            {
                var path = $window.location.pathname;
                var id = 0;
                if ( path === "/html/index.html")
                {
                    id = 0;
                }
                else if ( path === "/recipes/NewRecipe")
                {
                    id = 1;
                }
                else if ( path === "/recipes/SynchronizeRecipes")
                {
                    id = 2;
                }
                else if ( path === "/recetario/Configuration")
                {
                    id = 3;
                }
                if ( linkId === id )
                {
                    return 'menuSelected';
                }
                return "";
            };
            scope.getCanEdit = function() {
                return scope.canEdit == "true";
            }
        }
    };
}]);

recetarioModule.filter("array2string", function() {
    return function( input )
    {
        if ( !input || !input.constructor === Array )
        {
            return input;
        }
        return input.reduce( function( total, item ) {
            if ( !total )
            {
                return item;
            }
            else
            {
                return total + ", " + item;
            }    
        });
    }
});

recetarioModule.filter("markdown", function() {
    return function( input )
    {
        if ( !input )
        {
            return "";
        }
        return marked( input );
    }
});
                                            
recetarioModule.service('preferencesService', [ '$http', '$timeout', '$q', function( $http,  $timeout, $q ) {

    var thePreferences = null;
    
    this.getPreferences = function() {                                                   
        var deferred = $q.defer();
        $http.get( "http://localhost:8080/services/preferences").then( function( response ) 
        {
            thePreferences = response.data;
            deferred.resolve( thePreferences );
        }, function ( response ) {
            console.log( response.statusText );
            deferred.reject();
        });
        return deferred.promise;
    }
}]);

recetarioModule.service("recipeService", ['$http', '$q', 'preferencesService', function( $http, $q, preferencesService ) {
    var baseUrl = "http://localhost:8080/services/";
    
    this.getRecipe = function( id )
    {
        var deferred = $q.defer();
        $http.get( baseUrl + "recipe?id=" + id ).then( function(response) {
            deferred.resolve( response.data )
        }, function ( response ) {
            console.log("Error " + response.status);
            deferred.reject( null );
        });
        return deferred.promise;
    }
    
    this.getRecipes = function ( page, tags, searchType )
    {
        var deferred = $q.defer();
        preferencesService.getPreferences().then( function( pref ) {                                                   
            $http({
                    method : "GET",
                    url : "http://localhost:8080/services/recipe/search",
                    params : {
                        page : page,
                        pageSize : pref.recipesPerPage,
                        tags : tags,
                        searchType : searchType
                    }
                }).then( function( response ) {
                    console.log("[INFO]Se encuentran recetas");
                    deferred.resolve( response.data );
                }, function ( response ){
                    console.log("Error during recipe recover: " + response.status );
                    deferred.rejcet( null );
                });
        });
        return deferred.promise;
    }
}]);

recetarioModule.controller("recetarioController", ['$scope', '$location', 'preferencesService', function( $scope, $location, preferencesService ) {
    $scope.canEdit = false;
    preferencesService.getPreferences().then( function( pref ) {
        $scope.canEdit =  pref.mode === 0 || ( pref.mode === 1 && ( $location.host() === "localhost" || $location.host() === "127.0.0.1"));
    });
}]);
       