(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('ResponsableDetailController', ResponsableDetailController);

    ResponsableDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Responsable'];

    function ResponsableDetailController($scope, $rootScope, $stateParams, previousState, entity, Responsable) {
        var vm = this;

        vm.responsable = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sioeDevApp:responsableUpdate', function(event, result) {
            vm.responsable = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
