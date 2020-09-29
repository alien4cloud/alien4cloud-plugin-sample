This pom project is an example of an overlay project you'll need to use if you want top embed
a Wizard addon in a A4C war.

It embeds the [alien4cloud-plugin-sample-addon](../alien4cloud-plugin-sample-addon) sample Wizard addon in A4C war and make this
addon available from Wizard home page.

To achieve this, we need to:

- include the content of the addon webapp in the war.
- copy it's `wizard_addon.json` file into `wizard_addons` folder.
- merge it's ``addon-*.json`` files in commons ``addons-*.json`` to make addon title and description 
translations available for Wizard home page.

To add the addon content and description (`wizard_addon.json`) into the war we use overlays 
configuration of the ``maven-war-plugin`` :

```xml
<!-- [[ Wizard addon for sample -->
<overlay>
    <groupId>alien4cloud</groupId>
    <artifactId>alien4cloud-plugin-sample-addon</artifactId>
    <type>zip</type>
    <excludes>
        <exclude>wizard_addon.json</exclude>
    </excludes>
    <!-- The targetPath will define the addon webapp url -->
    <targetPath>sample_addon</targetPath>
</overlay>
<overlay>
    <groupId>alien4cloud</groupId>
    <artifactId>alien4cloud-plugin-sample-addon</artifactId>
    <type>zip</type>
    <includes>
        <include>wizard_addon.json</include>
    </includes>
    <!-- The targetPath after wizard_addons/ must be related to the zip targetPath above -->
    <targetPath>wizard_addons/sample_addon</targetPath>
</overlay>
<!-- ]] -->
```

We have 2 overlays :
- former for the addon content
- later for the `wizard_addon.json`

For the war content, you can use the `targetPath` you want, it will be the context path 
of your addon webapp.
For the `wizard_addon.json`, you must use the same after ``wizard_addons/``
Here we have chosen ``sample_addon``.

> :information_source: under the hood, A4C will explore the content of ``/wizard_addons`` and look 
> for all folders into ``/wizard_addons`` containing a ``wizard_addon.json``file. Json file will be parsed 
> and the addons list will be passed to the wizard home page throught ``/wizard/addons`` REST endpoint. 

Finally, to make the Wizard home page able to translate the id and description of the addon 
to display a user friendly clickable card, we need to merge all ``addon-*.json`` translation 
files into a single ``addons-*.json`` file. The Wizard translate configuration will look for this file in 
addition to main ``*.json`` i18n file.

To achieve this, we use the ``wso2-maven-json-merge-plugin`` maven plugin :

```xml
<tasks>
    <task>
        <base>${project.build.directory}/${project.build.finalName}/wizard/assets/i18n/addons.json</base>
        <!-- For each addon, add an include tag -->
        <include>
            <param>
                ${project.build.directory}/${project.build.finalName}/sample_addon/assets/i18n/addon-en.json
            </param>
        </include>
        <target>
            ${project.build.directory}/${project.build.finalName}/wizard/assets/i18n/addons-en.json
        </target>
        <mergeChildren>true</mergeChildren>
    </task>
    <task>
        <base>${project.build.directory}/${project.build.finalName}/wizard/assets/i18n/addons.json</base>
        <!-- For each addon, add an include tag -->
        <include>
            <param>
                ${project.build.directory}/${project.build.finalName}/sample_addon/assets/i18n/addon-fr.json
            </param>
        </include>
        <target>
            ${project.build.directory}/${project.build.finalName}/wizard/assets/i18n/addons-fr.json
        </target>
        <mergeChildren>true</mergeChildren>
    </task>
</tasks>
```

Please note that since the maven plugin ``wso2-maven-json-merge-plugin`` doesn't well manage UTF-8 file (output are ascii files), 
you need to encode your "Feature" section using [HTML special chars](https://ascii.cl/htmlcodes.htm) 
for accentuated characters (é = ```&egrave```; è = ```&aacute;``` ...).
