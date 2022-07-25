# JCStress playground
For more detail check [jcstress](https://github.com/openjdk/jcstress)

# Project template.

Template generated with maven archetype using:
```
    mvn archetype:generate \
     -DinteractiveMode=false \
     -DarchetypeGroupId=org.openjdk.jcstress \
     -DarchetypeArtifactId=jcstress-java-test-archetype \
     -DgroupId=org.max.jcstress \
     -DartifactId=jcstress-benchmark \
     -Dversion=1.0.0
```
Maven wrapper added with:
```
mvn -N wrapper:wrapper -Dmaven=3.6.3
```

Local java version added using `jenv`:
```
    jenv local 11
```


