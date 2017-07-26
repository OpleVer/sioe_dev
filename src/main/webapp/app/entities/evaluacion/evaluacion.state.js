(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('evaluacion', {
            parent: 'entity',
            url: '/evaluacion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Evaluacions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/evaluacion/evaluacions.html',
                    controller: 'EvaluacionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('evaluacion-detail', {
            parent: 'evaluacion',
            url: '/evaluacion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Evaluacion'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/evaluacion/evaluacion-detail.html',
                    controller: 'EvaluacionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Evaluacion', function($stateParams, Evaluacion) {
                    return Evaluacion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'evaluacion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('evaluacion-detail.edit', {
            parent: 'evaluacion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evaluacion/evaluacion-dialog.html',
                    controller: 'EvaluacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Evaluacion', function(Evaluacion) {
                            return Evaluacion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('evaluacion.new', {
            parent: 'evaluacion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evaluacion/evaluacion-dialog.html',
                    controller: 'EvaluacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tipo: null,
                                numero_acta: null,
                                acta: null,
                                acuerdo: null,
                                cedula: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('evaluacion', null, { reload: 'evaluacion' });
                }, function() {
                    $state.go('evaluacion');
                });
            }]
        })
        .state('evaluacion.edit', {
            parent: 'evaluacion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evaluacion/evaluacion-dialog.html',
                    controller: 'EvaluacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Evaluacion', function(Evaluacion) {
                            return Evaluacion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('evaluacion', null, { reload: 'evaluacion' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('evaluacion.delete', {
            parent: 'evaluacion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evaluacion/evaluacion-delete-dialog.html',
                    controller: 'EvaluacionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Evaluacion', function(Evaluacion) {
                            return Evaluacion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('evaluacion', null, { reload: 'evaluacion' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
