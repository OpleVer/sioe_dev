(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('PeticionAnexoController', PeticionAnexoController);

    PeticionAnexoController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Peticion', 'Peticionario', 'Responsable'];

    function PeticionAnexoController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Peticion, Peticionario, Responsable) {
        var vm = this;

        vm.peticion = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.peticionarios = Peticionario.query();
        vm.responsables = Responsable.query();

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
