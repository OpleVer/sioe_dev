(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('PeticionDetailController', PeticionDetailController);

    PeticionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Peticion', 'Peticionario', 'Responsable'];

    function PeticionDetailController($scope, $rootScope, $stateParams, previousState, entity, Peticion, Peticionario, Responsable) {
        var vm = this;

        vm.peticion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sioeDevApp:peticionUpdate', function(event, result) {
            vm.peticion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
