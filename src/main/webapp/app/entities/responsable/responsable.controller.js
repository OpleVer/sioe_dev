(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .controller('ResponsableController', ResponsableController);

    ResponsableController.$inject = ['Responsable'];

    function ResponsableController(Responsable) {

        var vm = this;

        vm.responsables = [];

        loadAll();

        function loadAll() {
            Responsable.query(function(result) {
                vm.responsables = result;
                vm.searchQuery = null;
            });
        }
    }
})();
