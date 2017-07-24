(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('PeticionDeleteController',PeticionDeleteController);

    PeticionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Peticion'];

    function PeticionDeleteController($uibModalInstance, entity, Peticion) {
        var vm = this;

        vm.peticion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Peticion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
