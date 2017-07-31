(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('PeticionDialogController', PeticionDialogController);

    PeticionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Peticion', 'Peticionario', 'Responsable', 'Evaluacion'];

    function PeticionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Peticion, Peticionario, Responsable, Evaluacion) {
        var vm = this;

        vm.peticion = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.peticionarios = Peticionario.query();
        vm.responsables = Responsable.query();
        vm.evaluacions = Evaluacion.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.peticion.id !== null) {
                Peticion.update(vm.peticion, onSaveSuccess, onSaveError);
            } else {
                Peticion.save(vm.peticion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sioeDevApp:peticionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fecha = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
