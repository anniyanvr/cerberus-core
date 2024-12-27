/**
 * Cerberus Copyright (C) 2013 - 2025 cerberustesting
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of Cerberus.
 *
 * Cerberus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cerberus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cerberus.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.cerberus.core.api.dto.queueexecution;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.cerberus.core.api.dto.views.View;

/**
 * @author lucashimpens
 */
@Data
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "QueuedExecutionResult")
public class QueuedExecutionResultDTOV001 {

    @ApiModelProperty(position = 1)
    @JsonView(View.Public.GET.class)
    private String tag;

    @JsonView(View.Public.GET.class)
    @ApiModelProperty(position = 2)
    private int nbExecutions;

    @JsonView(View.Public.GET.class)
    @ApiModelProperty(position = 2)
    private int testcasesNotExist;

    @JsonView(View.Public.GET.class)
    @ApiModelProperty(position = 3)
    private int testcasesNotActive;

    @JsonView(View.Public.GET.class)
    @ApiModelProperty(position = 4)
    private int testcasesNotAllowedOnEnvironment;

    @JsonView(View.Public.GET.class)
    @ApiModelProperty(position = 5)
    private int environmentsNotExistOrNotActive;

    @JsonView(View.Public.GET.class)
    @ApiModelProperty(position = 6)
    private int robotsMissing;

    @JsonView(View.Public.GET.class)
    @ApiModelProperty(position = 7)
    private List<QueuedEntryDTOV001> queuedEntries;

    @JsonView(View.Public.GET.class)
    @ApiModelProperty(position = 8)
    private List<String> messages;
}
