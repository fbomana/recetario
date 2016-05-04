var recetarioModule = angular.module("recetario",['ngSanitize', 'ngRoute']);

recetarioModule.config( ['$routeProvider','$httpProvider', function($routeProvider, $httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    $routeProvider.when('/home', {
        templateUrl : 'home.html',
        controller : 'HomeController'
    }).when("/new", {
        templateUrl : "edit.html",
        controller : "EditController"
    }).when("/edit/:id", {
        templateUrl : "edit.html",
        controller : "EditController"
    }).when ('/config', {
        templateUrl : 'config.html',
        controller : 'ConfigController'
    }).when ('/sync', {
        templateUrl : 'sync.html',
        controller : 'SyncController'
    }).otherwise('/home');
}]);

recetarioModule.directive("mainMenu", [ '$location', function( $location ) {
    return { 
        template : "<span id='search' ng-class='getSelectedClass(0)'><a ng-href='#/home'>Search Recipes</a></span>" +
                "<span id='new' ng-class='getSelectedClass(1)' ng-show='getCanEdit()'><a ng-href='#/new'>New Recipe</a></span>" +
                "<span id='synchronize' ng-class='getSelectedClass(2)' ng-show='getCanEdit()'><a ng-href='#/sync'>Synchronize</a></span>" + 
                "<span id='configuration' ng-class='getSelectedClass(3)' ng-show='getCanEdit()'><a ng-href='#/config'>Configuration</a></span>",
        restrict : "A",
        scope : {
            canEdit : "@mainMenu"
        },
        link: function (scope, element, attributes) {
            scope.getSelectedClass = function( linkId )
            {
                var path = $location.path();
                var id = 0;
                if ( path === "/home")
                {
                    id = 0;
                }
                else if ( path === "/new" || path.indexOf("/edit") > -1 )
                {
                    id = 1;
                }
                else if ( path === "/sync")
                {
                    id = 2;
                }
                else if ( path === "/config")
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

    
    this.getPreferences = function() {                                                   
        var deferred = $q.defer();
        $http.get( "http://localhost:8080/services/preferences").then( function( response ) 
        {
            thePreferences = response.data;
            deferred.resolve( thePreferences );
        }, function ( response ) {
            console.log( response.statusText );
            deferred.reject( response.statusText );
        });
        return deferred.promise;
    }
    
    this.save = function( preferences ) {
        return $http.post("http://localhost:8080/services/preferences", preferences );
    }
}]);

recetarioModule.service("recipeService", ['$http', '$q', 'preferencesService', function( $http, $q, preferencesService ) {
    var baseUrl = "http://localhost:8080";
    
    this.getRecipe = function( id, base )
    {
        var deferred = $q.defer();
        $http.get( ( base != undefined ? base : baseUrl )  + "/services/recipe/" + id ).then( function(response) {
            deferred.resolve( response.data )
        }, function ( response ) {
            console.log("Error " + response.status);
            deferred.reject( response.statusText );
        });
        return deferred.promise;
    }
    
    this.getRecipes = function ( tags, searchType, page, recipesPerPage, base )
    {
        var deferred = $q.defer();                                                          
        $http({
                method : "POST",
                url :  ( base != undefined ? base : baseUrl ) + "/services/recipe/search",
                params : {
                    page : page,
                    pageSize : recipesPerPage,
                    tags : tags,
                    searchType : searchType
                }
            }).then( function( response ) {
                console.log("[INFO]Se encuentran recetas");
                deferred.resolve( response.data );
            }, function ( response ){
                console.log("Error during recipe recover: " + response.status );
                deferred.reject( response );
        });
        return deferred.promise;
    }
    
    this.save = function ( recipe )
    {
        return $http.put( baseUrl + "/services/recipe", recipe );
    }
    
    this.delete = function ( id )
    {
        return $http.delete( baseUrl + "/services/recipe/"+ id );
    }
    
    this.import = function( recipe )
    {
        return $http.post( baseUrl + "/services/recipe/import", recipe );
    }
    
}]);

recetarioModule.controller("recetarioController", ['$scope', '$location', 'preferencesService', function( $scope, $location, preferencesService ) {
    $scope.canEdit = false;
    $scope.preferences = {};
    $scope.recetarioJSinitialized = false;
    preferencesService.getPreferences().then( function( pref ) {
        $scope.preferences = pref;
        $scope.canEdit =  pref.mode === 0 || ( pref.mode === 1 && ( $location.host() === "localhost" || $location.host() === "127.0.0.1"));
        $scope.recetarioJSinitialized = true;
    });
    
    $scope.$on("$locationChangeStart", function(event, next, current) {
/*        if ( !$scope.canEdit && next.indexOf("home") == -1 )
        {
            event.preventDefault();
        }*/
    });
}]);
       