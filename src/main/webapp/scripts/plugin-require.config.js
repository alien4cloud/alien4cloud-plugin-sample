require.config({
  baseUrl: './',
  paths: {
    'a4c-plugin-sample': 'scripts/plugin'
  },
  shim: {
    'angular': {
      deps: ['jquery'],
      exports: 'angular'
    },
    'angular-cookies': { deps: ['angular'] },
    'angular-bootstrap': { deps: ['angular'] },
    'angular-resource': { deps: ['angular'] },
    'angular-sanitize': { deps: ['angular'] },
    'angular-ui-router': { deps: ['angular'] },
    'angular-translate-base': { deps: ['angular'] },
    'angular-translate': { deps: ['angular-translate-base'] },
    'angular-all': { deps: ['angular-cookies', 'angular-translate', 'angular-ui-router', 'angular-sanitize', 'angular-resource', 'angular-bootstrap', 'angular-cookies'] },
    'ng-table': { deps: ['angular'] },
    'toaster': { deps: ['angular-animate'] },
    'angular-animate': { deps: ['angular'] },
    'angular-xeditable': { deps: ['angular'] },
    'angular-ui-select': { deps: ['angular'] },
    'angular-file-upload': { deps: ['angular', 'angular-file-upload-shim'] },
    'button-confirm': { deps: ['angular'] },
    'angular-ui-ace': { deps: ['angular', 'ace'] },
    'angular-tree-control': { deps: ['angular'] },
    'stomp':  { deps: ['sockjs'] },
    'dagre-d3': { deps: ['d3'] },
    'd3-pie': { deps: ['d3'] }
  }
});
