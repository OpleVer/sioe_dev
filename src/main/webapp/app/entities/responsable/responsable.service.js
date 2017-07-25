(function() {
    'use strict';
    angular
        .module('sioeDevApp')
        .factory('Responsable', Responsable);

    Responsable.$inject = ['$resource'];

    function Responsable ($resource) {
        var resourceUrl =  'api/responsables/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
