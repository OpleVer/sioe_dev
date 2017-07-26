(function() {
    'use strict';
    angular
        .module('sioeDevApp')
        .factory('Evaluacion', Evaluacion);

    Evaluacion.$inject = ['$resource'];

    function Evaluacion ($resource) {
        var resourceUrl =  'api/evaluacions/:id';

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
