// This is the ui entry point for the plugin

/* global define */

define(function (require) {
  'use strict';
  
  var states = require('states');
  var modules = require('modules');
  var prefixer = require('scripts/plugin-url-prefixer');

  require('scripts/hello-service.js');

  modules.get('a4c-plugin-sample').controller('HelloController', ['$scope', 'helloService',
    function($scope, helloService) {
      $scope.hello = helloService.get({name: 'world'});
    }
  ]);

  var templateUrl = prefixer.prefix('views/hello.html');
  // register plugin state
  states.state('a4cpluginsample', {
    url: '/a4c-plugin-sample',
    templateUrl: templateUrl,
    controller: 'HelloController',
    menu: {
      id: 'menu.a4c-plugin-sample',
      state: 'a4cpluginsample',
      key: 'Hello plugin',
      icon: 'fa fa-coffee',
      priority: 11000,
      roles: ['ADMIN']
    }
  });
});
