(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('PeticionarioDeleteController',PeticionarioDeleteController);

    PeticionarioDeleteController.$inject = ['$uibModalInstance', 'entity', 'Peticionario'];

    function PeticionarioDeleteController($uibModalInstance, entity, Peticionario) {
        var vm = this;

        vm.peticionario = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Peticionario.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
