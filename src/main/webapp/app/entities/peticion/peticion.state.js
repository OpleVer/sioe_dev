(function() {
    'use strict';

    angular
        .module('sioeDevApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('peticion', {
            parent: 'entity',
            url: '/peticion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Peticions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/peticion/peticions.html',
                    controller: 'PeticionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('peticion-detail', {
            parent: 'peticion',
            url: '/peticion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Peticion'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/peticion/peticion-detail.html',
                    controller: 'PeticionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Peticion', function($stateParams, Peticion) {
                    return Peticion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'peticion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('peticion-detail.edit', {
            parent: 'peticion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/peticion/peticion-dialog.html',
                    controller: 'PeticionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Peticion', function(Peticion) {
                            return Peticion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('peticion.new', {
            parent: 'peticion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/peticion/peticion-dialog.html',
                    controller: 'PeticionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numero_peticion: null,
                                fecha: null,
                                acto_certificar: null,
                                responsable: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('peticion', null, { reload: 'peticion' });
                }, function() {
                    $state.go('peticion');
                });
            }]
        })
        .state('peticion.edit', {
            parent: 'peticion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/peticion/peticion-dialog.html',
                    controller: 'PeticionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Peticion', function(Peticion) {
                            return Peticion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('peticion', null, { reload: 'peticion' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('peticion.delete', {
            parent: 'peticion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/peticion/peticion-delete-dialog.html',
                    controller: 'PeticionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Peticion', function(Peticion) {
                            return Peticion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('peticion', null, { reload: 'peticion' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
