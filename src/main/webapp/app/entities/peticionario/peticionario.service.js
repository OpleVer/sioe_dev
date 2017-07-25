(function() {
    'use strict';
    angular
        .module('sioeDevApp')
        .factory('Peticionario', Peticionario);

    Peticionario.$inject = ['$resource'];

    function Peticionario ($resource) {
        var resourceUrl =  'api/peticionarios/:id';

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
