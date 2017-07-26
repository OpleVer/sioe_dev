(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('EvaluacionDetailController', EvaluacionDetailController);

    EvaluacionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Evaluacion'];

    function EvaluacionDetailController($scope, $rootScope, $stateParams, previousState, entity, Evaluacion) {
        var vm = this;

        vm.evaluacion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sioeDevApp:evaluacionUpdate', function(event, result) {
            vm.evaluacion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
