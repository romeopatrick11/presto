/*
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
 */
package com.facebook.presto.raptor;

import com.facebook.presto.spi.session.PropertyMetadata;
import com.facebook.presto.spi.type.TypeManager;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;

import static com.facebook.presto.spi.type.StandardTypes.ARRAY;
import static com.facebook.presto.spi.type.VarcharType.VARCHAR;
import static java.util.Locale.ENGLISH;
import static java.util.stream.Collectors.toList;

public class RaptorTableProperties
{
    public static final String ORDERING_PROPERTY = "ordering";
    public static final String TEMPORAL_COLUMN_PROPERTY = "temporal_column";

    private final List<PropertyMetadata<?>> tableProperties;

    @Inject
    public RaptorTableProperties(TypeManager typeManager)
    {
        tableProperties = ImmutableList.of(
                new PropertyMetadata<>(
                        ORDERING_PROPERTY,
                        "Sort order for each shard of the table",
                        typeManager.getParameterizedType(ARRAY, ImmutableList.of(VARCHAR.getTypeSignature()), ImmutableList.of()),
                        List.class,
                        ImmutableList.of(),
                        false,
                        value -> ImmutableList.copyOf(((List<String>) value).stream()
                                .map(name -> name.toLowerCase(ENGLISH))
                                .collect(toList()))),
                new PropertyMetadata<>(
                        TEMPORAL_COLUMN_PROPERTY,
                        "Temporal column of the table",
                        VARCHAR,
                        String.class,
                        null,
                        false,
                        value -> ((String) value).toLowerCase(ENGLISH)));
    }

    public List<PropertyMetadata<?>> getTableProperties()
    {
        return tableProperties;
    }

    public static List<String> getSortColumns(Map<String, Object> tableProperties)
    {
        return (List<String>) tableProperties.get(ORDERING_PROPERTY);
    }

    public static String getTemporalColumn(Map<String, Object> tableProperties)
    {
        return (String) tableProperties.get(TEMPORAL_COLUMN_PROPERTY);
    }
}
