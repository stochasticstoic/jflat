/*
 Copyright 2008 TecAceT Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.tecacet.jflat.introspection;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.tecacet.jflat.introspection.BeanIntrospectorException;
import com.tecacet.jflat.introspection.MapPropertyAccessor;

public class MapPropertyAccessorTest {

    @Test
    public void testSetGetProperty() throws BeanIntrospectorException {
        Map<String,Object> map = new HashMap<>();
        MapPropertyAccessor accessor = new MapPropertyAccessor();
        assertNull(accessor.getProperty(map, "any"));
        accessor.setProperty(map, "name", "Indigo");
        assertEquals("Indigo", accessor.getProperty(map, "name"));
    }

}
