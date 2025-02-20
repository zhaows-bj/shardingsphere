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

package org.apache.shardingsphere.infra.util.exception.external.sql.type.feature;

import org.apache.shardingsphere.infra.util.exception.external.sql.sqlstate.XOpenSQLState;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class FeatureSQLExceptionTest {
    
    @SuppressWarnings("serial")
    @Test
    public void assertNewFeatureSQLExceptionWithSingleDigitalForFeatureCode() {
        SQLException actual = new FeatureSQLException(XOpenSQLState.GENERAL_ERROR, 1, 1, "reason") {
        }.toSQLException();
        assertThat(actual.getSQLState(), is(XOpenSQLState.GENERAL_ERROR.getValue()));
        assertThat(actual.getErrorCode(), is(20101));
        assertThat(actual.getMessage(), is("reason"));
    }
    
    @SuppressWarnings("serial")
    @Test
    public void assertNewFeatureSQLExceptionWithDoubleDigitalForFeatureCode() {
        SQLException actual = new FeatureSQLException(XOpenSQLState.GENERAL_ERROR, 99, 10, "reason") {
        }.toSQLException();
        assertThat(actual.getSQLState(), is(XOpenSQLState.GENERAL_ERROR.getValue()));
        assertThat(actual.getErrorCode(), is(29910));
        assertThat(actual.getMessage(), is("reason"));
    }
}
