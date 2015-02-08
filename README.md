A Web-Based (Debugging) Editor for the Table-Algebra

# Readme #

FerryLeaks is an editor for the table algebra, used for debugging query plans for [Ferry](http://db.inf.uni-tuebingen.de/projects/Ferry.html) and [Pathfinder](http://db.inf.uni-tuebingen.de/projects/Pathfinder.html). It supports SQL-generation for either the whole plan or sub-plans, features 'SQL-Listeners' that can be attached to any node and provides editing in a graphical manner as well as through direct XML-manipulation.

Moreover, FerryLeaks comes with a simple XML descriptor language that is used to define and validate nodes and plans. It is highly flexible and can be used as an editor/evaluator for almost any kind of algebra that can be expressed in a tree-like structure. A testing example for FerryLeaks, loaded with descriptor files for a simple algebra supporting literals, 'plus', 'minus', 'mult' and 'div', can be found [here](http://patrickbrosi.de/algebraeditor/).

## 0. Example ##

http://dbwiscam.informatik.uni-tuebingen.de/AlgebraEditor/


## 1. Requirements ##

WebFerry is a web application using Google's WebToolkit (GWT). To run a precompiled WAR
of WebFerry on your server, you need the have Tomcat installed.
The GraphCanvas uses GraphViz' dot to layout the logical plan graphs, so a dot installation
is highly recommended, however not mandatory. Without dot being installed WebFerry falls back
to very simple layouting algorithms.
To compile you own version of WebFerry the GWT-SDK is needed which can be downloaded at
http://code.google.com/intl/de-DE/webtoolkit/download.html

You need at least GWT SDK 2.3. Newer versions of GWT SDK _MAY_ work, however, 2.3 was the last version
FerryLeaks has been tested with.

## 2. Compilation ##

WebFerry comes with an ant script to generate a ready-to-go WAR (WebARchive) which can be easily
uploaded to a live Tomcat server. Compilation can be started be typing

    ant war

If you only want to compile WebFerry but not to create the WAR-file, you can do so by typing

    ant build

NOTE, however, that you must first change the build.properties.example to fit your system environment.
(More specifically, you have to change the path of GWT-SDK). Otherwise, compilation won't be possible.
The build.properties.example  HAS TO BE RENAMED  to build.properties after modification.

## 3. Installation ##

Deploy the WAR-File (build/AlgebraEditor.war) to a Tomcat server.

Your Tomcat must allow file-uploads to use FerryLeaks' upload mechanisms. It must also allow
system calls to communicate with dot and Ferry.

