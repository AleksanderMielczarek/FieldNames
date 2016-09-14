package com.github.aleksandermielczarek.processor;

import com.github.aleksandermielczarek.FieldNames;
import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

/**
 * Created by Aleksander Mielczarek on 13.09.2016.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.github.aleksandermielczarek.FieldNames")
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
                    typeElement.getEnclosedElements().stream()
                            .filter(element -> element.getKind().equals(ElementKind.FIELD))
                            .filter(element -> !element.getModifiers().contains(Modifier.STATIC))
                            .map(element -> (VariableElement) element)
                            .map(VariableElement::getSimpleName)
                            .map(CharSequence::toString)
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
