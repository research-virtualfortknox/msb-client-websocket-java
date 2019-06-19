/*
 * Copyright (c) 2019 Fraunhofer Institute for Manufacturing Engineering and Automation (IPA).
 * Authors: Daniel Schel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package de.fhg.ipa.vfk.msb.client.parser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * The Class PackageScanner.
 *
 * @author des
 */
public final class PackageScanner {

    private static final Logger LOG = LoggerFactory.getLogger(PackageScanner.class);

    private PackageScanner() {}

    /**
     * Find my types.
     *
     * @param <A>         the generic type
     * @param basePackage the base package
     * @param annotations the annotations
     * @return the list
     * @throws IOException            Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException the class not found exception
     */
    public static <A extends Annotation> List<Class<?>> findMyTypes(String basePackage, Class<A>[] annotations)
            throws IOException, ClassNotFoundException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        List<Class<?>> candidates = new ArrayList<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage)
                + "/**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (isCandidate(metadataReader, annotations)) {
                    candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                }
            }
        }
        return candidates;
    }

    /**
     * Find my type.
     *
     * @param <A>         the generic type
     * @param basePackage the base package
     * @param annotation  the annotation
     * @return the list
     * @throws IOException            Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException the class not found exception
     */
    public static <A extends Annotation> List<Class<?>> findMyType(String basePackage, Class<A> annotation)
            throws IOException, ClassNotFoundException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        List<Class<?>> candidates = new ArrayList<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage)
                + "/**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (isCandidate(metadataReader, annotation)) {
                    candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                }
            }
        }
        return candidates;
    }

    /**
     * Resolve base package.
     *
     * @param basePackage the base package
     * @return the string
     */
    private static String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

    /**
     * Checks if is candidate.
     *
     * @param <A>            the generic type
     * @param metadataReader the metadata reader
     * @param annotation     the annotation
     * @return true, if is candidate
     */
    private static <A extends Annotation> boolean isCandidate(MetadataReader metadataReader, Class<A> annotation) {
        try {
            Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
            if (c.getAnnotation(annotation) != null) {
                return true;
            }
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {
            LOG.warn("Instantiation error, check if a default constructor is defined.", e);
        } catch (ClassNotFoundException e) {
            LOG.warn("ClassNotFoundException during class is candidate check: ", e);
        }
        return false;
    }

    /**
     * Checks if is candidate.
     *
     * @param <A>            the generic type
     * @param metadataReader the metadata reader
     * @param annotations    the annotations
     * @return true, if is candidate
     */
    private static <A extends Annotation> boolean isCandidate(MetadataReader metadataReader, Class<A>[] annotations) {
        try {
            Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
            for (Class<A> annotation : annotations) {
                if (c.getAnnotation(annotation) != null) {
                    return true;
                }
            }
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {
            LOG.warn("Instantiation error, check if a default constructor is defined.", e);
        } catch (ClassNotFoundException e) {
            LOG.warn("ClassNotFoundException during class is candidate check: ", e);
        }
        return false;
    }

}
