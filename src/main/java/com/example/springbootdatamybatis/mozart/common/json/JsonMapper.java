package com.example.springbootdatamybatis.mozart.common.json;

/**
 * @Author he.zhou
 * @Date 2019-10-04 15:41
 **/
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.example.springbootdatamybatis.mozart.common.CommonFrameworkModule;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class JsonMapper {
    private final ObjectMapper current;

    public JsonMapper() {
        this.current = new ObjectMapper();
        configure(this.current, false);
    }

    public JsonMapper(boolean bizDateEnhance) {
        this.current = new ObjectMapper();
        configure(this.current, bizDateEnhance);
    }

    public JsonMapper(boolean timestampPriority, String formatPattern) {
        this.current = new ObjectMapper();
        configure(this.current, false, timestampPriority, formatPattern);
    }

    public JsonMapper(boolean bizDateEnhance, boolean timestampPriority, String formatPattern) {
        this.current = new ObjectMapper();
        configure(this.current, bizDateEnhance, timestampPriority, formatPattern);
    }

    public JsonMapper(ObjectMapper current) {
        this.current = current;
    }

    public static void configure(ObjectMapper current, boolean bizDateEnhance) {
        configure(current, bizDateEnhance, true, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static void configure(ObjectMapper current, boolean bizDateEnhance, boolean timestampPriority, String formatPattern) {
        current.setTimeZone(TimeZone.getDefault());
        current.setDateFormat(new JacksonDateFormat(formatPattern));
        if (timestampPriority) {
            current.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        }

        current.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true).configure(Feature.ALLOW_COMMENTS, true).configure(Feature.ALLOW_YAML_COMMENTS, true).configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true).configure(Feature.ALLOW_NON_NUMERIC_NUMBERS, true).configure(Feature.ALLOW_NUMERIC_LEADING_ZEROS, true).configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).configure(Feature.ALLOW_SINGLE_QUOTES, true).configure(Feature.IGNORE_UNDEFINED, true).setSerializationInclusion(Include.NON_NULL);
        current.registerModules(new Module[]{new AfterburnerModule()});
        current.registerModules(new Module[]{new GuavaModule()});
        current.registerModules(new Module[]{new JodaModule(formatPattern)});
        ClassLoader loader = JsonMapper.class.getClassLoader();
        if (ClassUtils.isPresent("java.util.Optional", loader)) {
            try {
                current.registerModule((Module)BeanUtils.instantiateClass(ClassUtils.forName("com.fasterxml.jackson.datatype.jdk8.Jdk8Module", loader)));
            } catch (ClassNotFoundException var9) {
            }
        }

        if (ClassUtils.isPresent("java.time.LocalDate", loader)) {
            try {
                current.registerModules(new Module[]{(Module)BeanUtils.instantiate(ClassUtils.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule", loader))});
            } catch (ClassNotFoundException var8) {
            }
        }

        if (ClassUtils.isPresent("kotlin.Unit", loader)) {
            try {
                current.registerModules(new Module[]{(Module)BeanUtils.instantiate(ClassUtils.forName("com.fasterxml.jackson.module.kotlin.KotlinModule", loader))});
            } catch (ClassNotFoundException var7) {
            }
        }

        if (bizDateEnhance && ClassUtils.isPresent("com.wormpex.api.json.BizDateStdModule", loader)) {
            try {
                current.registerModules(new Module[]{(Module)BeanUtils.instantiate(ClassUtils.forName("com.wormpex.api.json.BizDateStdModule", loader))});
            } catch (ClassNotFoundException var6) {
            }
        }

        CommonFrameworkModule commonFrameworkModule = new CommonFrameworkModule();
        current.registerModule(commonFrameworkModule);
    }

    public static List<Module> findModules() {
        return findModules((ClassLoader)null);
    }

    public static List<Module> findModules(ClassLoader classLoader) {
        ArrayList<Module> modules = new ArrayList();
        ServiceLoader<Module> loader = classLoader == null ? ServiceLoader.load(Module.class) : ServiceLoader.load(Module.class, classLoader);
        Iterator var3 = loader.iterator();

        while(var3.hasNext()) {
            Module module = (Module)var3.next();
            modules.add(module);
        }

        return modules;
    }

    public ObjectMapper getDelegate() {
        return this.current;
    }

    public JsonMapper copy() {
        return new JsonMapper(this.current.copy());
    }

    public Version version() {
        return this.current.version();
    }

    public JsonMapper registerModule(Module module) {
        this.current.registerModule(module);
        return this;
    }

    public JsonMapper registerModules(Module... modules) {
        this.current.registerModules(modules);
        return this;
    }

    public JsonMapper registerModules(Iterable<Module> modules) {
        this.current.registerModules(modules);
        return this;
    }

    public JsonMapper findAndRegisterModules() {
        this.current.findAndRegisterModules();
        return this;
    }

    public SerializationConfig getSerializationConfig() {
        return this.current.getSerializationConfig();
    }

    public DeserializationConfig getDeserializationConfig() {
        return this.current.getDeserializationConfig();
    }

    public DeserializationContext getDeserializationContext() {
        return this.current.getDeserializationContext();
    }

    public SerializerFactory getSerializerFactory() {
        return this.current.getSerializerFactory();
    }

    public JsonMapper setSerializerFactory(SerializerFactory f) {
        this.current.setSerializerFactory(f);
        return this;
    }

    public SerializerProvider getSerializerProvider() {
        return this.current.getSerializerProvider();
    }

    public JsonMapper setSerializerProvider(DefaultSerializerProvider p) {
        this.current.setSerializerProvider(p);
        return this;
    }

    public SerializerProvider getSerializerProviderInstance() {
        return this.current.getSerializerProviderInstance();
    }

    public JsonMapper setMixIns(Map<Class<?>, Class<?>> sourceMixins) {
        this.current.setMixIns(sourceMixins);
        return this;
    }

    public JsonMapper addMixIn(Class<?> target, Class<?> mixinSource) {
        this.current.addMixIn(target, mixinSource);
        return this;
    }

    public JsonMapper setMixInResolver(MixInResolver resolver) {
        this.current.setMixInResolver(resolver);
        return this;
    }

    public Class<?> findMixInClassFor(Class<?> cls) {
        return this.current.findMixInClassFor(cls);
    }

    public int mixInCount() {
        return this.current.mixInCount();
    }

    public VisibilityChecker<?> getVisibilityChecker() {
        return this.current.getVisibilityChecker();
    }

    public JsonMapper setVisibility(VisibilityChecker<?> vc) {
        this.current.setVisibility(vc);
        return this;
    }

    public JsonMapper setVisibility(PropertyAccessor forMethod, Visibility visibility) {
        this.current.setVisibility(forMethod, visibility);
        return this;
    }

    public SubtypeResolver getSubtypeResolver() {
        return this.current.getSubtypeResolver();
    }

    public JsonMapper setSubtypeResolver(SubtypeResolver str) {
        this.current.setSubtypeResolver(str);
        return this;
    }

    public JsonMapper setAnnotationIntrospector(AnnotationIntrospector ai) {
        this.current.setAnnotationIntrospector(ai);
        return this;
    }

    public JsonMapper setAnnotationIntrospectors(AnnotationIntrospector serializerAI, AnnotationIntrospector deserializerAI) {
        this.current.setAnnotationIntrospectors(serializerAI, deserializerAI);
        return this;
    }

    public PropertyNamingStrategy getPropertyNamingStrategy() {
        return this.current.getPropertyNamingStrategy();
    }

    public JsonMapper setPropertyNamingStrategy(PropertyNamingStrategy s) {
        this.current.setPropertyNamingStrategy(s);
        return this;
    }

    public JsonMapper setSerializationInclusion(Include incl) {
        this.current.setSerializationInclusion(incl);
        return this;
    }

    public JsonMapper setPropertyInclusion(Value incl) {
        this.current.setPropertyInclusion(incl);
        return this;
    }

    public JsonMapper setDefaultPrettyPrinter(PrettyPrinter pp) {
        this.current.setDefaultPrettyPrinter(pp);
        return this;
    }

    public JsonMapper enableDefaultTyping() {
        this.current.enableDefaultTyping();
        return this;
    }

    public JsonMapper enableDefaultTyping(DefaultTyping dti) {
        this.current.enableDefaultTyping(dti);
        return this;
    }

    public JsonMapper enableDefaultTyping(DefaultTyping applicability, As includeAs) {
        this.current.enableDefaultTyping(applicability, includeAs);
        return this;
    }

    public JsonMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName) {
        this.current.enableDefaultTypingAsProperty(applicability, propertyName);
        return this;
    }

    public JsonMapper disableDefaultTyping() {
        this.current.disableDefaultTyping();
        return this;
    }

    public JsonMapper setDefaultTyping(TypeResolverBuilder<?> typer) {
        this.current.setDefaultTyping(typer);
        return this;
    }

    public void registerSubtypes(Class... classes) {
        this.current.registerSubtypes(classes);
    }

    public void registerSubtypes(NamedType... types) {
        this.current.registerSubtypes(types);
    }

    public TypeFactory getTypeFactory() {
        return this.current.getTypeFactory();
    }

    public JsonMapper setTypeFactory(TypeFactory f) {
        this.current.setTypeFactory(f);
        return this;
    }

    public JavaType constructType(Type t) {
        return this.current.constructType(t);
    }

    public JsonNodeFactory getNodeFactory() {
        return this.current.getNodeFactory();
    }

    public JsonMapper setNodeFactory(JsonNodeFactory f) {
        this.current.setNodeFactory(f);
        return this;
    }

    public JsonMapper addHandler(DeserializationProblemHandler h) {
        this.current.addHandler(h);
        return this;
    }

    public JsonMapper clearProblemHandlers() {
        this.current.clearProblemHandlers();
        return this;
    }

    public JsonMapper setConfig(DeserializationConfig config) {
        this.current.setConfig(config);
        return this;
    }

    public JsonMapper setFilterProvider(FilterProvider filterProvider) {
        this.current.setFilterProvider(filterProvider);
        return this;
    }

    public JsonMapper setBase64Variant(Base64Variant v) {
        this.current.setBase64Variant(v);
        return this;
    }

    public JsonMapper setConfig(SerializationConfig config) {
        this.current.setConfig(config);
        return this;
    }

    public JsonFactory getFactory() {
        return this.current.getFactory();
    }

    public DateFormat getDateFormat() {
        return this.current.getDateFormat();
    }

    public JsonMapper setDateFormat(DateFormat dateFormat) {
        this.current.setDateFormat(dateFormat);
        return this;
    }

    public Object setHandlerInstantiator(HandlerInstantiator hi) {
        return this.current.setHandlerInstantiator(hi);
    }

    public InjectableValues getInjectableValues() {
        return this.current.getInjectableValues();
    }

    public JsonMapper setInjectableValues(InjectableValues injectableValues) {
        this.current.setInjectableValues(injectableValues);
        return this;
    }

    public JsonMapper setLocale(Locale l) {
        this.current.setLocale(l);
        return this;
    }

    public JsonMapper setTimeZone(TimeZone tz) {
        this.current.setTimeZone(tz);
        return this;
    }

    public boolean isEnabled(MapperFeature f) {
        return this.current.isEnabled(f);
    }

    public JsonMapper configure(MapperFeature f, boolean state) {
        this.current.configure(f, state);
        return this;
    }

    public JsonMapper enable(MapperFeature... f) {
        this.current.enable(f);
        return this;
    }

    public JsonMapper disable(MapperFeature... f) {
        this.current.disable(f);
        return this;
    }

    public boolean isEnabled(SerializationFeature f) {
        return this.current.isEnabled(f);
    }

    public JsonMapper configure(SerializationFeature f, boolean state) {
        this.current.configure(f, state);
        return this;
    }

    public JsonMapper enable(SerializationFeature f) {
        this.current.enable(f);
        return this;
    }

    public JsonMapper enable(SerializationFeature first, SerializationFeature... f) {
        this.current.enable(first, f);
        return this;
    }

    public JsonMapper disable(SerializationFeature f) {
        this.current.disable(f);
        return this;
    }

    public JsonMapper disable(SerializationFeature first, SerializationFeature... f) {
        this.current.disable(first, f);
        return this;
    }

    public boolean isEnabled(DeserializationFeature f) {
        return this.current.isEnabled(f);
    }

    public JsonMapper configure(DeserializationFeature f, boolean state) {
        this.current.configure(f, state);
        return this;
    }

    public JsonMapper enable(DeserializationFeature feature) {
        this.current.enable(feature);
        return this;
    }

    public JsonMapper enable(DeserializationFeature first, DeserializationFeature... f) {
        this.current.enable(first, f);
        return this;
    }

    public JsonMapper disable(DeserializationFeature feature) {
        this.current.disable(feature);
        return this;
    }

    public JsonMapper disable(DeserializationFeature first, DeserializationFeature... f) {
        this.current.disable(first, f);
        return this;
    }

    public boolean isEnabled(Feature f) {
        return this.current.isEnabled(f);
    }

    public JsonMapper configure(Feature f, boolean state) {
        this.current.configure(f, state);
        return this;
    }

    public JsonMapper enable(Feature... features) {
        this.current.enable(features);
        return this;
    }

    public JsonMapper disable(Feature... features) {
        this.current.disable(features);
        return this;
    }

    public boolean isEnabled(com.fasterxml.jackson.core.JsonGenerator.Feature f) {
        return this.current.isEnabled(f);
    }

    public JsonMapper configure(com.fasterxml.jackson.core.JsonGenerator.Feature f, boolean state) {
        this.current.configure(f, state);
        return this;
    }

    public JsonMapper enable(com.fasterxml.jackson.core.JsonGenerator.Feature... features) {
        this.current.enable(features);
        return this;
    }

    public JsonMapper disable(com.fasterxml.jackson.core.JsonGenerator.Feature... features) {
        this.current.disable(features);
        return this;
    }

    public boolean isEnabled(com.fasterxml.jackson.core.JsonFactory.Feature f) {
        return this.current.isEnabled(f);
    }

    public <T> T readValue(JsonParser jp, Class<T> valueType) throws JsonException {
        try {
            return this.current.readValue(jp, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(JsonParser jp, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return this.current.readValue(jp, valueTypeRef);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public final <T> T readValue(JsonParser jp, ResolvedType valueType) throws JsonException {
        try {
            return this.current.readValue(jp, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(JsonParser jp, JavaType valueType) throws JsonException {
        try {
            return this.current.readValue(jp, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T extends TreeNode> T readTree(JsonParser jp) throws JsonException {
        try {
            return this.current.readTree(jp);
        } catch (Exception var3) {
            throw new JsonException(var3.getMessage(), var3);
        }
    }

    public <T> MappingIterator<T> readValues(JsonParser p, ResolvedType valueType) throws JsonException {
        try {
            return this.current.readValues(p, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> MappingIterator<T> readValues(JsonParser p, JavaType valueType) throws JsonException {
        try {
            return this.current.readValues(p, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> MappingIterator<T> readValues(JsonParser p, Class<T> valueType) throws JsonException {
        try {
            return this.current.readValues(p, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> MappingIterator<T> readValues(JsonParser p, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return this.current.readValues(p, valueTypeRef);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public JsonNode readTree(InputStream in) throws JsonException {
        try {
            return this.current.readTree(in);
        } catch (Exception var3) {
            throw new JsonException(var3.getMessage(), var3);
        }
    }

    public JsonNode readTree(Reader r) throws JsonException {
        try {
            return this.current.readTree(r);
        } catch (Exception var3) {
            throw new JsonException(var3.getMessage(), var3);
        }
    }

    public JsonNode readTree(String content) throws JsonException {
        try {
            return this.current.readTree(content);
        } catch (Exception var3) {
            throw new JsonException(var3.getMessage(), var3);
        }
    }

    public JsonNode readTree(byte[] content) throws JsonException {
        try {
            return this.current.readTree(content);
        } catch (Exception var3) {
            throw new JsonException(var3.getMessage(), var3);
        }
    }

    public JsonNode readTree(File file) throws JsonException {
        try {
            return this.current.readTree(file);
        } catch (Exception var3) {
            throw new JsonException(var3.getMessage(), var3);
        }
    }

    public JsonNode readTree(URL source) throws JsonException {
        try {
            return this.current.readTree(source);
        } catch (Exception var3) {
            throw new JsonException(var3.getMessage(), var3);
        }
    }

    public void writeValue(JsonGenerator g, Object value) throws JsonException {
        try {
            this.current.writeValue(g, value);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public void writeTree(JsonGenerator jgen, TreeNode rootNode) throws JsonException {
        try {
            this.current.writeTree(jgen, rootNode);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws JsonException {
        try {
            this.current.writeTree(jgen, rootNode);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public ObjectNode createObjectNode() {
        return this.current.createObjectNode();
    }

    public ArrayNode createArrayNode() {
        return this.current.createArrayNode();
    }

    public JsonParser treeAsTokens(TreeNode n) {
        return this.current.treeAsTokens(n);
    }

    public <T> T treeToValue(TreeNode n, Class<T> valueType) throws JsonException {
        try {
            return this.current.treeToValue(n, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T extends JsonNode> T valueToTree(Object fromValue) throws JsonException {
        try {
            return this.current.valueToTree(fromValue);
        } catch (Exception var3) {
            throw new JsonException(var3.getMessage(), var3);
        }
    }

    public boolean canSerialize(Class<?> type) {
        return this.current.canSerialize(type);
    }

    public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause) {
        return this.current.canSerialize(type, cause);
    }

    public boolean canDeserialize(JavaType type) {
        return this.current.canDeserialize(type);
    }

    public boolean canDeserialize(JavaType type, AtomicReference<Throwable> cause) {
        return this.current.canDeserialize(type, cause);
    }

    public <T> T readValue(File src, Class<T> valueType) throws JsonException {
        try {
            return this.current.readValue(src, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(File src, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return this.current.readValue(src, valueTypeRef);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(File src, JavaType valueType) throws JsonException {
        try {
            return this.current.readValue(src, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(URL src, Class<T> valueType) throws JsonException {
        try {
            return this.current.readValue(src, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(URL src, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return this.current.readValue(src, valueTypeRef);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(URL src, JavaType valueType) throws JsonException {
        try {
            return this.current.readValue(src, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(String content, Class<T> valueType) throws JsonException {
        try {
            return this.current.readValue(content, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(String content, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return this.current.readValue(content, valueTypeRef);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(String content, JavaType valueType) throws JsonException {
        try {
            return this.current.readValue(content, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(Reader src, Class<T> valueType) throws JsonException {
        try {
            return this.current.readValue(src, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(Reader src, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return this.current.readValue(src, valueTypeRef);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(Reader src, JavaType valueType) throws JsonException {
        try {
            return this.current.readValue(src, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(InputStream src, Class<T> valueType) throws JsonException {
        try {
            return this.current.readValue(src, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(InputStream src, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return this.current.readValue(src, valueTypeRef);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(InputStream src, JavaType valueType) throws JsonException {
        try {
            return this.current.readValue(src, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(byte[] src, Class<T> valueType) throws JsonException {
        try {
            return this.current.readValue(src, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType) throws JsonException {
        try {
            return this.current.readValue(src, offset, len, valueType);
        } catch (Exception var6) {
            throw new JsonException(var6.getMessage(), var6);
        }
    }

    public <T> T readValue(byte[] src, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return this.current.readValue(src, valueTypeRef);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(byte[] src, int offset, int len, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return this.current.readValue(src, offset, len, valueTypeRef);
        } catch (Exception var6) {
            throw new JsonException(var6.getMessage(), var6);
        }
    }

    public <T> T readValue(byte[] src, JavaType valueType) throws JsonException {
        try {
            return this.current.readValue(src, valueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T readValue(byte[] src, int offset, int len, JavaType valueType) throws JsonException {
        try {
            return this.current.readValue(src, offset, len, valueType);
        } catch (Exception var6) {
            throw new JsonException(var6.getMessage(), var6);
        }
    }

    public void writeValue(File resultFile, Object value) throws JsonException {
        try {
            this.current.writeValue(resultFile, value);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public void writeValue(OutputStream out, Object value) throws JsonException {
        try {
            this.current.writeValue(out, value);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public void writeValue(Writer w, Object value) throws JsonException {
        try {
            this.current.writeValue(w, value);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public String writeValueAsString(Object value) throws JsonException {
        try {
            return this.current.writeValueAsString(value);
        } catch (Exception var3) {
            throw new JsonException(var3.getMessage(), var3);
        }
    }

    public byte[] writeValueAsBytes(Object value) throws JsonException {
        try {
            return this.current.writeValueAsBytes(value);
        } catch (Exception var3) {
            throw new JsonException(var3.getMessage(), var3);
        }
    }

    public JsonWriter writer() {
        return new JsonWriter(this.current.writer());
    }

    public JsonWriter writer(SerializationFeature feature) {
        return new JsonWriter(this.current.writer(feature));
    }

    public JsonWriter writer(SerializationFeature first, SerializationFeature... other) {
        return new JsonWriter(this.current.writer(first, other));
    }

    public JsonWriter writer(DateFormat df) {
        return new JsonWriter(this.current.writer(df));
    }

    public JsonWriter writerWithView(Class<?> serializationView) {
        return new JsonWriter(this.current.writerWithView(serializationView));
    }

    public JsonWriter writerFor(Class<?> rootType) {
        return new JsonWriter(this.current.writerFor(rootType));
    }

    public JsonWriter writerFor(TypeReference<?> rootType) {
        return new JsonWriter(this.current.writerFor(rootType));
    }

    public JsonWriter writerFor(JavaType rootType) {
        return new JsonWriter(this.current.writerFor(rootType));
    }

    public JsonWriter writer(PrettyPrinter pp) {
        return new JsonWriter(this.current.writer(pp));
    }

    public JsonWriter writerWithDefaultPrettyPrinter() {
        return new JsonWriter(this.current.writerWithDefaultPrettyPrinter());
    }

    public JsonWriter writer(FilterProvider filterProvider) {
        return new JsonWriter(this.current.writer(filterProvider));
    }

    public JsonWriter writer(FormatSchema schema) {
        return new JsonWriter(this.current.writer(schema));
    }

    public JsonWriter writer(Base64Variant defaultBase64) {
        return new JsonWriter(this.current.writer(defaultBase64));
    }

    public JsonWriter writer(CharacterEscapes escapes) {
        return new JsonWriter(this.current.writer(escapes));
    }

    public JsonWriter writer(ContextAttributes attrs) {
        return new JsonWriter(this.current.writer(attrs));
    }

    public JsonReader reader() {
        return new JsonReader(this.current.reader());
    }

    public JsonReader reader(DeserializationFeature feature) {
        return new JsonReader(this.current.reader(feature));
    }

    public JsonReader reader(DeserializationFeature first, DeserializationFeature... other) {
        return new JsonReader(this.current.reader(first, other));
    }

    public JsonReader readerForUpdating(Object valueToUpdate) {
        return new JsonReader(this.current.readerForUpdating(valueToUpdate));
    }

    public JsonReader readerFor(JavaType type) {
        return new JsonReader(this.current.readerFor(type));
    }

    public JsonReader readerFor(Class<?> type) {
        return new JsonReader(this.current.readerFor(type));
    }

    public JsonReader readerFor(TypeReference<?> type) {
        return new JsonReader(this.current.readerFor(type));
    }

    public JsonReader reader(JsonNodeFactory f) {
        return new JsonReader(this.current.reader(f));
    }

    public JsonReader reader(FormatSchema schema) {
        return new JsonReader(this.current.reader(schema));
    }

    public JsonReader reader(InjectableValues injectableValues) {
        return new JsonReader(this.current.reader(injectableValues));
    }

    public JsonReader readerWithView(Class<?> view) {
        return new JsonReader(this.current.readerWithView(view));
    }

    public JsonReader reader(Base64Variant defaultBase64) {
        return new JsonReader(this.current.reader(defaultBase64));
    }

    public JsonReader reader(ContextAttributes attrs) {
        return new JsonReader(this.current.reader(attrs));
    }

    public <T> T convertValue(Object fromValue, Class<T> toValueType) throws JsonException {
        try {
            return this.current.convertValue(fromValue, toValueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) throws JsonException {
        try {
            return this.current.convertValue(fromValue, toValueTypeRef);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public <T> T convertValue(Object fromValue, JavaType toValueType) throws JsonException {
        try {
            return this.current.convertValue(fromValue, toValueType);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public void acceptJsonFormatVisitor(Class<?> type, JsonFormatVisitorWrapper visitor) throws JsonException {
        try {
            this.current.acceptJsonFormatVisitor(type, visitor);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }

    public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor) throws JsonException {
        try {
            this.current.acceptJsonFormatVisitor(type, visitor);
        } catch (Exception var4) {
            throw new JsonException(var4.getMessage(), var4);
        }
    }
}
