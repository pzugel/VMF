/*
 * Copyright 2017-2018 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 * Copyright 2017-2018 Goethe Center for Scientific Computing, University Frankfurt. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * If you use this software for scientific research then please cite the following publication(s):
 *
 * M. Hoffer, C. Poliwoda, & G. Wittum. (2013). Visual reflection library:
 * a framework for declarative GUI programming on the Java platform.
 * Computing and Visualization in Science, 2013, 16(4),
 * 181–192. http://doi.org/10.1007/s00791-014-0230-y
 */
package eu.mihosoft.vmf.runtime.core;

import eu.mihosoft.vmf.runtime.core.internal.VObjectInternal;
import eu.mihosoft.vmf.runtime.core.internal.VObjectInternalModifiable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("deprecation")
public final class Property {

    private final VObjectInternal parent;
    private final int propertyId;
    private final String name;
    private final Type type;

    private Property(VObjectInternal parent, String name) {
        this.parent = parent;
        this.name = name;
        this.propertyId = parent._vmf_getPropertyIdByName(name);

        boolean isModelType = parent._vmf_getPropertyTypes()[propertyId]!=-1;
        boolean isListType  = parent._vmf_getPropertyTypes()[propertyId]==-2;

        // if we are a list type we check whether this property id is listed as
        // being part of 'properties with model element types' array
        // -> if that's the case we define this property as being a model type
        if (isListType) {
            isModelType = false;
            for (int pId : parent._vmf_getIndicesOfPropertiesWithModelElementTypes()) {
                if(propertyId == pId) {
                    isModelType = true;
                    break;
                }
            }
        }

        this.type = Type.newInstance(isModelType, isListType, parent._vmf_getPropertyTypeNames()[propertyId]);
    }

    @Deprecated
    public static Property newInstance(VObjectInternal parent, String name) {
        return new Property(parent, name);
    }

    public boolean isSet() {
        return parent._vmf_isSetById(propertyId);
    }

    public void set(Object o) {

        if(parent == null) {
            throw new RuntimeException("Cannot set property without access to an instance.");
        }

        if(parent instanceof VObjectInternalModifiable) {
            ((VObjectInternalModifiable)parent)._vmf_setPropertyValueById(propertyId, o);
        } else {
            throw new RuntimeException("Cannot modify unmodifiable object");
        }
    }

    public void unset() {

        if(parent == null) {
            throw new RuntimeException("Cannot set property without access to an instance.");
        }

        if(parent instanceof VObjectInternalModifiable) {
            ((VObjectInternalModifiable)parent)._vmf_setPropertyValueById(propertyId, getDefault());
        } else {
            throw new RuntimeException("Cannot modify unmodifiable object");
        }
    }

    public Object get() {

        if(parent == null) {
            throw new RuntimeException("Cannot set property without access to an instance.");
        }

        return parent._vmf_getPropertyValueById(propertyId);
    }

    @Deprecated()
    public void setDefault(Object value) {

        if(parent == null) {
            throw new RuntimeException("Cannot set property without access to an instance.");
        }

        if(parent instanceof VObjectInternalModifiable) {
            ((VObjectInternalModifiable)parent)._vmf_setDefaultValueById(propertyId, value);
        } else {
            throw new RuntimeException("Cannot modify unmodifiable object");
        }
    }

    public Object getDefault() {

        if(parent == null) {
            throw new RuntimeException("Cannot set property without access to an instance.");
        }

        return parent._vmf_getDefaultValueById(propertyId);
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the list of annotations of this object.
     * @return the list of annotations of this object
     */
    public List<Annotation> annotations() {
        return parent._vmf_getPropertyAnnotationsById(propertyId);
    }

    /**
     * Returns the annotation specified by key.
     * @param key the key of the annotation to return
     * @return the annotation specified by key
     */
    public Optional<Annotation> annotationByKey(String key) {
        return annotations().stream().filter(a->key.equals(a.getKey())).findFirst();
    }
}
