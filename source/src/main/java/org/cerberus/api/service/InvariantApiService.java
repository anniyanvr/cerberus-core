/**
 * Cerberus Copyright (C) 2013 - 2017 cerberustesting
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
package org.cerberus.api.service;

import java.util.List;
import org.cerberus.api.dto.v001.InvariantDTOV001;
import org.cerberus.api.errorhandler.exception.EntityNotFoundException;
import org.cerberus.crud.dao.IInvariantDAO;
import org.cerberus.crud.entity.Invariant;
import org.cerberus.exception.CerberusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mlombard
 */
@Service
public class InvariantApiService {

    IInvariantDAO invariantDao;

    @Autowired
    public InvariantApiService(IInvariantDAO invariantDao) {
        this.invariantDao = invariantDao;
    }

    public Invariant readByKey(String idName, String value) throws CerberusException {
        Invariant invariant = this.invariantDao.readByKey(idName, value);
        if (invariant == null) {
            throw new EntityNotFoundException(InvariantDTOV001.class, "idName", idName, "value", value);
        }
        return invariant;
    }

    public List<Invariant> readyByIdName(String idName) throws CerberusException {
        List<Invariant> invariants = this.invariantDao.readByIdname(idName);
        if (invariants == null || invariants.isEmpty()) {
            throw new EntityNotFoundException(InvariantDTOV001.class, "idName", idName);
        }
        return invariants;
    }
}
