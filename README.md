# alien4cloud-plugin-sample
Sample plugin with rest service and ui components.

This project is a small illustration of what can be done into alien4cloud in terms of customization.

## Description

### The backend part

In A4C, a plugin, when loaded and activated, will start a Spring context, child of the main application context. 
It can contain it's own beans that can access to main context's beans.

#### The @Configuration



The class [alien4cloud.sampleplugin.ContextConfiguration](src/main/java/alien4cloud/sampleplugin/ContextConfiguration.java) is the entry point of the plugin backend. It defines the context and how to configure it. In this example, we simply scan the package `alien4cloud.sampleplugin` in order to detect and instantiate beans.

#### The @Service

The class [alien4cloud.sampleplugin.services.MyPluginService](src/main/java/alien4cloud/sampleplugin/services/MyPluginService.java) is a Spring @Service that contains the 'logic'.

#### The @RestController

The class [alien4cloud.sampleplugin.rest.MyPluginController](src/main/java/alien4cloud/sampleplugin/rest/MyPluginController.java) is a Spring controller that exposes a REST endpoint (`/rest/sample/hello`).

It simply calls the service to build a simple response.

### The UI part

Optionally, a plugin can embed UI parts.

The UI part of this simple plugin add a menu in the main Alien4Cloud navigation bar that display a page. The page displays the result of the service call through the REST endpoint.

- the file [src/main/webapp/views/hello.html]() is the angular view.
- the file [src/main/webapp/scripts/hello-service.js]() creates an angular service used by the angular controller. It's in charge of calling the REST resource.
- the file [src/main/webapp/scripts/plugin.js]() is the UI entry point of the plugin : it creates a angular controller, bind it to the service, and define the view template. It also configure menu entry for the plugin.

### The manifest

Last but not least, the file [META-INF/plugin.yml]() is the manifest of the plugin.
It's needed by A4C to properly activate the plugin.

## Build and test

Simply build using `mvn clean install`. The assembly will generate a zip file in `target` folder (ie. `alien4cloud-plugin-sample-2.2.0-SNAPSHOT.zip`).

Just drag and drop this file in the Administration / Plugin page of a running A4C.

![Alt text](doc/upload.png?raw=true "Upload A4C plugin")

A 'Hello plugin' menu will appear in the main navigation bar. If you click on it, the plugin view will be displayed.

![Alt text](doc/display.png?raw=true "Display A4C plugin")

