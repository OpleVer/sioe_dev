(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('PeticionarioDialogController', PeticionarioDialogController);

    PeticionarioDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Peticionario'];

    function PeticionarioDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Peticionario) {
        var vm = this;

        vm.peticionario = entity;
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
            if (vm.peticionario.id !== null) {
                Peticionario.update(vm.peticionario, onSaveSuccess, onSaveError);
            } else {
                Peticionario.save(vm.peticionario, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sioeDevApp:peticionarioUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
