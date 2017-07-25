(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('ResponsableDeleteController',ResponsableDeleteController);

    ResponsableDeleteController.$inject = ['$uibModalInstance', 'entity', 'Responsable'];

    function ResponsableDeleteController($uibModalInstance, entity, Responsable) {
        var vm = this;

        vm.responsable = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Responsable.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
