(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('AnexoController', AnexoController);

    AnexoController.$inject = ['Anexo'];

    function AnexoController(Anexo) {

        var vm = this;

        vm.anexos = [];

        loadAll();

        function loadAll() {
            Anexo.query(function(result) {
                vm.anexos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
