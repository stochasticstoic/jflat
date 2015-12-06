/*
 Copyright 2008  TecaceT Ltd.

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
package com.tecacet.jflat;

/**
 * A 'pass-through' role mapper that preserves the array character of a row.
 * 
 * @author Dimitri Papaioannou
 * 
 */
@SuppressWarnings("rawtypes")
public class DefaultRowMapper extends AbstractReaderRowMapper implements  WriterRowMapper {

    public Object getRow(String[] row, int rowNumber) {
        return row;
    }

    public String[] getRow(Object bean) {
        return (String[]) bean;
    }

}
