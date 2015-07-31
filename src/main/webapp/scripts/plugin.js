// This is the ui entry point for the plugin

/* global define */

'use strict';

define(function (require) {
  var pluginId = 'a4c-plugin-sample';
  var states = require('states');
  var modules = require('modules');
  var plugins = require('plugins');
  var prefixer = {
    prefix: function(url) {
      return plugins.base(pluginId) + url;
    }
  };

  require('scripts/hello-service.js');

  modules.get('a4c-plugin-sample').factory('prefixer', [function() {
    return prefixer;
  }]);

  modules.get('a4c-plugin-sample').controller('HelloController', ['$scope', 'helloService',
    function($scope, helloService) {
      $scope.hello = helloService.get({name: 'toto'});
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
      icon: 'fa fa-wrench',
      priority: 11000,
      roles: ['ADMIN']
    }
  });
});
