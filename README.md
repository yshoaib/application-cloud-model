application-cloud-model
=============
This software provides users an simple way to represent applications and clouds. For an example refer to 
DefaultAppBuilder class.

For Performance modeling, the models can be converted to an LqnModel and then written as an lqnx file, and then 
solved through LQNS.

Maven
=====
```
<!-- https://mvnrepository.com/artifact/ca.appsimulations/models -->
<dependency>
    <groupId>ca.appsimulations</groupId>
    <artifactId>models</artifactId>
    <version>LATEST.RELEASE.VERSION</version>
</dependency>
```

Changelog
=========
[Changelog](CHANGELOG.md)

Generating UML diagrams
=======================
- Refer to https://github.com/ruben2020/tags2uml
- Run following commands from within the `application-cloud-model` git repository:
```
find . -name "*.java" > ./JavaFiles.txt
ctags --fields=+latinK -L ./JavaFiles.txt
tags2uml --infile tags --outfile ApplicationCloudModel.dot
dot -Tpng -oApplicationCloudModel.png ApplicationCloudModel.dot
```

Related projects
===================
https://github.com/yshoaib/jLQNInterface