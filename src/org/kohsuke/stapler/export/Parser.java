package org.kohsuke.stapler.export;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.io.IOException;

/**
 * Writes all the property of one {@link ExportedBean} to {@link DataWriter}.
 *
 * @author Kohsuke Kawaguchi
 */
public class Parser<T> {
    private final Class<T> type;

    /**
     * {@link Parser} for the super class.
     */
    private final Parser<? super T> superParser;

    private final Property[] properties;

    /*package*/ final ParserBuilder parent;
    /*package*/ final int defaultVisibility;

    /*package*/ Parser(ParserBuilder parent, Class<T> type) {
        this.parent = parent;
        this.type = type;
        ExportedBean eb = type.getAnnotation(ExportedBean.class);
        if(eb ==null)
            throw new IllegalArgumentException(type+" doesn't have @ExposedBean");
        this.defaultVisibility = eb.defaultVisibility();
        
        parent.parsers.put(type,this);

        Class<? super T> sc = type.getSuperclass();
        if(sc!=null && sc.getAnnotation(ExportedBean.class)!=null)
            superParser = parent.get(sc);
        else
            superParser = null;

        List<Property> properties = new ArrayList<Property>();

        // Use reflection to find out what properties are exposed.
        for( Field f : type.getFields() ) {
            if(f.getDeclaringClass()!=type) continue;
            Exported exported = f.getAnnotation(Exported.class);
            if(exported !=null)
                properties.add(new FieldProperty(this,f, exported));
        }

        for( Method m : type.getMethods() ) {
            if(m.getDeclaringClass()!=type) continue;
            Exported exported = m.getAnnotation(Exported.class);
            if(exported !=null)
                properties.add(new MethodProperty(this,m, exported));
        }

        this.properties = properties.toArray(new Property[properties.size()]);
        Arrays.sort(this.properties);
    }

    /**
     * Gets all the exported properties.
     */
    public List<Property> getProperties() {
        return Collections.unmodifiableList(Arrays.asList(properties));
    }

    /**
     * Writes the property values of the given object to the writer.
     */
    public void writeTo(T object, DataWriter writer) throws IOException {
        writer.startObject();
        writeTo(object,1,writer);
        writer.endObject();
    }

    void writeTo(T object, int depth, DataWriter writer) throws IOException {
        if(superParser!=null)
            superParser.writeTo(object,depth,writer);

        for (Property p : properties)
            p.writeTo(object,depth,writer);
    }
}