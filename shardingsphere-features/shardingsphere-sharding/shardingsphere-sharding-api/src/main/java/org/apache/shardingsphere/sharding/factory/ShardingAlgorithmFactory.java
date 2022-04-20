/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sharding.factory;

import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmFactory;
import org.apache.shardingsphere.sharding.spi.ShardingAlgorithm;
import org.apache.shardingsphere.spi.ShardingSphereServiceLoader;
import org.apache.shardingsphere.spi.type.typed.TypedSPIRegistry;

import java.util.Properties;

/**
 * Sharding algorithm factory.
 */
public final class ShardingAlgorithmFactory {
    
    static {
        ShardingSphereServiceLoader.register(ShardingAlgorithm.class);
    }
    
    /**
     * Create new instance of sharding algorithm.
     * 
     * @param shardingAlgorithmConfig sharding algorithm configuration
     * @return new instance of sharding algorithm
     */
    public static ShardingAlgorithm newInstance(final ShardingSphereAlgorithmConfiguration shardingAlgorithmConfig) {
        return ShardingSphereAlgorithmFactory.createAlgorithm(shardingAlgorithmConfig, ShardingAlgorithm.class);
    }
    
    /**
     * Judge whether contains sharding algorithm.
     *
     * @param shardingAlgorithmType sharding algorithm type
     * @return contains sharding algorithm or not
     */
    public static boolean contains(final String shardingAlgorithmType) {
        return TypedSPIRegistry.findRegisteredService(ShardingAlgorithm.class, shardingAlgorithmType, new Properties()).isPresent();
    }
}
