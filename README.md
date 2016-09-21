[![](https://jitpack.io/v/AleksanderMielczarek/FieldNames.svg)](https://jitpack.io/#AleksanderMielczarek/FieldNames)

# FieldNames

Generate classes with field names.

## Usage

Add it in your root build.gradle:

```groovy
buildscript {  
    repositories {
        ...
        mavenCentral()
    }
    dependencies {
        ...
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'            
    }
}

allprojects {
	repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency:

```groovy
apply plugin: 'android-apt'

dependencies {
    ...
    compile 'com.github.AleksanderMielczarek.FieldNames:fieldnames:0.2.0'
    apt 'com.github.AleksanderMielczarek.FieldNames:fieldnames-processor:0.2.0'
}
```

## Example

Annotate your class with @FieldNames:

```java
@FieldNames
public class Person {

    private String name;
    private String surname;
    
    //getters and setters
}
```

Following class will be generated:

```java
public interface PersonFieldNames {

    String FIELD_NAME = "name";
    String FIELD_SURNAME = "surname";
}
```

Library supports [AutoValue](https://github.com/google/auto/tree/master/value):

```java
@FieldNames
@AutoValue
public abstract class User {

    public abstract String getName();
}
```

```java
public interface UserFieldNames {

    String FIELD_NAME = "name";
}
```

## Changelog

### 0.2.0 (2016-09-21)

- generate fields from getters

## License

    Copyright 2016 Aleksander Mielczarek

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.