A simple plugin for the IntelliJ Platform (IntelliJ IDEA, RubyMine, WebStorm, PhpStorm, PyCharm, Android Studio and AppCode) that provides keyboard shortcut access to Dash. This is necessary as the 'Services' menu is not populated in IntelliJ (apparently a Java limitation).

## Installation
To install the plugin in your IntelliJ IDE go to Preferences -> Plugins -> Browse repositories and search for "Dash".

## Kapeli Dash
Dash is a Mac application for rapid search of developer documentation. It is free with nags to persuade you to pay and lose the nags. The free version is fully functional and super-useful. Get karma for buying and supporting the developer :) It can be downloaded here:
[http://kapeli.com/dash](http://kapeli.com/dash)

## Usage
The default shortcut assigned in the plugin is **Mac-Shift-D**. It either 
uses the current selection for the search, or the caret position.

## Configuration
The plugin will use the documents file type to determine which docset keyword to use in Dash.
These associations are customizable via the "Preferences" -> "IDE Settings" -> "Dash" configuration panel from inside the IDE.
Types entered can either be file types (those can be found in the IDE settings) or extensions (must be prefixed with a dot, ie. ".html").

Please feel free to request improvements, or fork-it and make them yourself!
