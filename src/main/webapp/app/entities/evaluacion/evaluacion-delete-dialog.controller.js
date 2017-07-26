(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('EvaluacionDeleteController',EvaluacionDeleteController);

    EvaluacionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Evaluacion'];

    function EvaluacionDeleteController($uibModalInstance, entity, Evaluacion) {
        var vm = this;

        vm.evaluacion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Evaluacion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
