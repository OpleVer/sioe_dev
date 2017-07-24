(function() {
    'use strict';
    angular
        .module('sioeDevApp')
        .factory('Anexo', Anexo);

    Anexo.$inject = ['$resource'];

    function Anexo ($resource) {
        var resourceUrl =  'api/anexos/:id';

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
