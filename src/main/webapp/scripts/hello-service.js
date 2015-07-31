/* global define */

'use strict';

define(function (require) {
  var modules = require('modules');

  modules.get('a4c-plugin-sample', ['ngResource']).factory('helloService', ['$resource',
    function($resource) {
      return $resource('rest/sample/hello');
    }
  ]);
});
