(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('EvaluacionDialogController', EvaluacionDialogController);

    EvaluacionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Evaluacion'];

    function EvaluacionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Evaluacion) {
        var vm = this;

        vm.evaluacion = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.evaluacion.id !== null) {
                Evaluacion.update(vm.evaluacion, onSaveSuccess, onSaveError);
            } else {
                Evaluacion.save(vm.evaluacion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sioeDevApp:evaluacionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
