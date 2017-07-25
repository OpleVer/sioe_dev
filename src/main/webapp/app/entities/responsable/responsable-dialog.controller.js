(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('ResponsableDialogController', ResponsableDialogController);

    ResponsableDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Responsable'];

    function ResponsableDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Responsable) {
        var vm = this;

        vm.responsable = entity;
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
            if (vm.responsable.id !== null) {
                Responsable.update(vm.responsable, onSaveSuccess, onSaveError);
            } else {
                Responsable.save(vm.responsable, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sioeDevApp:responsableUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
