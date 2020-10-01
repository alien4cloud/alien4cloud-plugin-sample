# alien4cloud-plugin-sample-modifier
A simple A4C plugin including a sample modifier.

Modifiers allow you to process a topology before it's deployed. You can do whatever you want in such pieces of code : add nodes, changes property values, detect policies, and so on ...
Your imagination is your limit, but take care of modifiers dependency hell !

This sample modifier :

* adds a compute in the topology
* defines it's scalable max_instance property to 2
* adds relationships between all nodes of the topology to this compute

Since this modifier adds an abstract node that should be matched, this modifier must be added in a location having Compute resources configured, and you must setup it in phase `pre-node-match`


![Modifier Setup](doc/setupModifier.png?raw=true "Modifier setup")

At deployment phase you will see your modifier logs


![Modifier Logs](doc/logs.png?raw=true "Modifier Logs")
