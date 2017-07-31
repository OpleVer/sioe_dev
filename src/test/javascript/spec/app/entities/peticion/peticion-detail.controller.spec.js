'use strict';

describe('Controller Tests', function() {

    describe('Peticion Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPeticion, MockPeticionario, MockResponsable, MockEvaluacion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPeticion = jasmine.createSpy('MockPeticion');
            MockPeticionario = jasmine.createSpy('MockPeticionario');
            MockResponsable = jasmine.createSpy('MockResponsable');
            MockEvaluacion = jasmine.createSpy('MockEvaluacion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Peticion': MockPeticion,
                'Peticionario': MockPeticionario,
                'Responsable': MockResponsable,
                'Evaluacion': MockEvaluacion
            };
            createController = function() {
                $injector.get('$controller')("PeticionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sioeDevApp:peticionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
