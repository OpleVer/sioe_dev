(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('AnexoDeleteController',AnexoDeleteController);

    AnexoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Anexo'];

    function AnexoDeleteController($uibModalInstance, entity, Anexo) {
        var vm = this;

        vm.anexo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Anexo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
