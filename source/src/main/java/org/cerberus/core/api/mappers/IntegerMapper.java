/*
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
package org.cerberus.core.api.mappers;

import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.Mapper;


/**
 * @author lucashimpens
 */
@Mapper(componentModel = "spring")
public interface IntegerMapper {

    static final Logger LOG = LogManager.getLogger(IntegerMapper.class);

     default int toInteger(Optional<Integer> value) {
        LOG.debug("mapping from Optional " + value + " to int");

        //add your custom mapping implementation
        return value.get();
    }

    default Optional<Integer> toOptionalInteger(int value) {
        LOG.debug("mapping from int : " + value + " to Optional ");
        //add your custom mapping implementation
        return Optional.of(value);
    }
}