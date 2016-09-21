package com.github.aleksandermielczarek.processor;

import com.github.aleksandermielczarek.fieldnames.FieldNames;
import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * Created by Aleksander Mielczarek on 13.09.2016.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.github.aleksandermielczarek.fieldnames.FieldNames")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class FieldNamesProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(FieldNames.class).stream()
                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                .map(element -> (TypeElement) element)
                .forEach(typeElement -> {
                    PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
                    TypeSpec.Builder fieldNamesBuilder = TypeSpec.interfaceBuilder(typeElement.getSimpleName() + "FieldNames")
                            .addModifiers(Modifier.PUBLIC);

                    TypeMirror superMirror = typeElement.getSuperclass();
                    List<TypeElement> typeElements = new ArrayList<>();
                    typeElements.add(typeElement);
                    while (!superMirror.getKind().equals(TypeKind.NONE)) {
                        TypeElement superTypeElement = processingEnv.getElementUtils().getTypeElement(superMirror.toString());
                        superMirror = superTypeElement.getSuperclass();
                        if (!superMirror.getKind().equals(TypeKind.NONE)) {
                            typeElements.add(superTypeElement);
                        }
                    }

                    Set<String> fields = typeElements.stream()
                            .map(TypeElement::getEnclosedElements)
                            .flatMap(Collection::stream)
                            .filter(element -> element.getKind().equals(ElementKind.FIELD))
                            .filter(element -> !element.getModifiers().contains(Modifier.STATIC))
                            .map(element -> (VariableElement) element)
                            .map(VariableElement::getSimpleName)
                            .map(CharSequence::toString)
                            .collect(Collectors.toSet());

                    typeElements.stream()
                            .map(TypeElement::getEnclosedElements)
                            .flatMap(Collection::stream)
                            .filter(element -> element.getKind().equals(ElementKind.METHOD))
                            .filter(element -> !element.getModifiers().contains(Modifier.STATIC))
                            .map(element -> (ExecutableElement) element)
                            .map(ExecutableElement::getSimpleName)
                            .map(CharSequence::toString)
                            .filter(name -> name.startsWith("get"))
                            .map(name -> name.substring(3, name.length()))
                            .map(name -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name))
                            .forEach(fields::add);

                    fields.stream()
                            .map(name -> FieldSpec.builder(String.class, "FIELD_" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name))
                                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                    .initializer("$S", name))
                            .map(FieldSpec.Builder::build)
                            .forEach(fieldNamesBuilder::addField);

                    JavaFile javaFile = JavaFile.builder(packageElement.getQualifiedName().toString(), fieldNamesBuilder.build())
                            .build();
                    try {
                        javaFile.writeTo(processingEnv.getFiler());
                    } catch (IOException e) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                    }
                });

        return true;
    }

}
