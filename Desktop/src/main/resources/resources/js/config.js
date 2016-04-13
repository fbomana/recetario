'use strict';

angular.module("recetario").controller("ConfigController", ['$scope', 'preferencesService', function( $scope, preferencesService ) {
    $scope.preferences = {};
    $scope.message = "";
    $scope.modes = [{id : 0, label : 'Full Edit'},
                    {id : 1, label : 'Local Edit'},
                    {id : 2, label : 'Read Only'}];
    
    preferencesService.getPreferences().then( function( pref ) {
        $scope.preferences = pref;
    }, function ( error ) {
        $scope.message = "error";
    })
    
    $scope.savePreferences = function() {
        preferencesService.save( $scope.preferences ).then( function( response ) {
                $scope.message = "Preferences saved";
            }, function( response ) {
                $scope.message = "Error saving preferences: " + response.statusText;
        });
    }
    
}]);