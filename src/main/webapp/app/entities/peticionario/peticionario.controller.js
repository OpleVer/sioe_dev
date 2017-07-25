(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('PeticionarioController', PeticionarioController);

    PeticionarioController.$inject = ['Peticionario'];

    function PeticionarioController(Peticionario) {

        var vm = this;

        vm.peticionarios = [];

        loadAll();

        function loadAll() {
            Peticionario.query(function(result) {
                vm.peticionarios = result;
                vm.searchQuery = null;
            });
        }
    }
})();
