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

package org.apache.shardingsphere.data.pipeline.opengauss.check.datasource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.data.pipeline.core.check.datasource.AbstractDataSourceChecker;
import org.apache.shardingsphere.data.pipeline.core.exception.job.PipelineJobPrepareFailedException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Data source checker of openGauss.
 */
@Slf4j
public final class OpenGaussDataSourceChecker extends AbstractDataSourceChecker {
    
    private static final String SHOW_GRANTS_SQL = "SELECT * FROM pg_roles WHERE rolname = ?";
    
    @Override
    public void checkPrivilege(final Collection<? extends DataSource> dataSources) {
        for (DataSource each : dataSources) {
            checkPrivilege(each);
        }
    }
    
    private void checkPrivilege(final DataSource dataSource) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SHOW_GRANTS_SQL)) {
            DatabaseMetaData metaData = connection.getMetaData();
            preparedStatement.setString(1, metaData.getUserName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new PipelineJobPrepareFailedException(String.format("No role exists, rolname: %s.", metaData.getUserName()));
                }
                String isSuperRole = resultSet.getString("rolsuper");
                String isReplicationRole = resultSet.getString("rolreplication");
                String isSystemAdminRole = resultSet.getString("rolsystemadmin");
                log.info("checkPrivilege: isSuperRole: {}, isReplicationRole: {}, isSystemAdminRole: {}", isSuperRole, isReplicationRole, isSystemAdminRole);
                if (!StringUtils.equalsAnyIgnoreCase("t", isSuperRole, isReplicationRole, isSystemAdminRole)) {
                    throw new PipelineJobPrepareFailedException(String.format("Source data source is lack of REPLICATION privileges, you could try `ALTER ROLE \"%s\" REPLICATION;`.",
                            metaData.getUserName()));
                }
            }
        } catch (final SQLException ex) {
            throw new PipelineJobPrepareFailedException("Source data source check privileges failed.", ex);
        }
    }
    
    @Override
    public void checkVariable(final Collection<? extends DataSource> dataSources) {
    }
    
    @Override
    protected String getDatabaseType() {
        return "openGauss";
    }
}
