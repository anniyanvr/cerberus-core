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
package org.cerberus.core.crud.service.impl;

import java.util.List;
import java.util.Map;
import org.cerberus.core.crud.dao.ICountryEnvParam_logDAO;
import org.cerberus.core.crud.entity.CountryEnvParam_log;
import org.cerberus.core.engine.entity.MessageGeneral;
import org.cerberus.core.crud.factory.IFactoryCountryEnvParam_log;
import org.cerberus.core.crud.service.ICountryEnvParam_logService;
import org.cerberus.core.enums.MessageGeneralEnum;
import org.cerberus.core.enums.MessageEventEnum;
import org.cerberus.core.exception.CerberusException;
import org.cerberus.core.util.answer.Answer;
import org.cerberus.core.util.answer.AnswerItem;
import org.cerberus.core.util.answer.AnswerList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author vertigo17
 */
@Service
public class CountryEnvParam_logService implements ICountryEnvParam_logService {

    @Autowired
    private ICountryEnvParam_logDAO countryEnvParamLogDao;
    @Autowired
    private IFactoryCountryEnvParam_log countryEnvParamLogFactory;

    @Override
    public AnswerItem<CountryEnvParam_log> readByKey(Long id) {
        return countryEnvParamLogDao.readByKey(id);
    }

    @Override
    public AnswerList<CountryEnvParam_log> readAll() {
        return readByVariousByCriteria(null, null, null, null, null, 0, 0, "id", "asc", null, null);
    }

    @Override
    public AnswerList<CountryEnvParam_log> readByVariousByCriteria(String system, String country, String environment, String build, String revision, int startPosition, int length, String columnName, String sort, String searchParameter, Map<String, List<String>> individualSearch) {
        return countryEnvParamLogDao.readByVariousByCriteria(system, country, environment, build, revision, startPosition, length, columnName, sort, searchParameter, individualSearch);
    }

    @Override
    public AnswerList<CountryEnvParam_log> readLastChanges(String system, String country, Integer nbDays, String envGp) {
        return countryEnvParamLogDao.readLastChanges(system, country, nbDays, envGp);
    }

    @Override
    public boolean exist(Long id) {
        AnswerItem objectAnswer = readByKey(id);
        return (objectAnswer.isCodeEquals(MessageEventEnum.DATA_OPERATION_OK.getCode())) && (objectAnswer.getItem() != null); // Call was successfull and object was found.
    }

    @Override
    public Answer create(CountryEnvParam_log countryEnvParamLog) {
        return countryEnvParamLogDao.create(countryEnvParamLog);
    }

    @Override
    public Answer delete(CountryEnvParam_log countryEnvParamLog) {
        return countryEnvParamLogDao.delete(countryEnvParamLog);
    }

    @Override
    public Answer update(CountryEnvParam_log countryEnvParamLog) {
        return countryEnvParamLogDao.update(countryEnvParamLog);
    }

    @Override
    public Answer createLogEntry(String system, String country, String environment, String build, String revision, String description, String creator) {
        return this.create(countryEnvParamLogFactory.create(system, country, environment, build, revision, 0, description, creator));
    }

    @Override
    public CountryEnvParam_log convert(AnswerItem<CountryEnvParam_log> answerItem) throws CerberusException {
        if (answerItem.isCodeEquals(MessageEventEnum.DATA_OPERATION_OK.getCode())) {
            //if the service returns an OK message then we can get the item
            return answerItem.getItem();
        }
        throw new CerberusException(new MessageGeneral(MessageGeneralEnum.DATA_OPERATION_ERROR));
    }

    @Override
    public List<CountryEnvParam_log> convert(AnswerList<CountryEnvParam_log> answerList) throws CerberusException {
        if (answerList.isCodeEquals(MessageEventEnum.DATA_OPERATION_OK.getCode())) {
            //if the service returns an OK message then we can get the item
            return answerList.getDataList();
        }
        throw new CerberusException(new MessageGeneral(MessageGeneralEnum.DATA_OPERATION_ERROR));
    }

    @Override
    public void convert(Answer answer) throws CerberusException {
        if (answer.isCodeEquals(MessageEventEnum.DATA_OPERATION_OK.getCode())) {
            //if the service returns an OK message then we can get the item
            return;
        }
        throw new CerberusException(new MessageGeneral(MessageGeneralEnum.DATA_OPERATION_ERROR));
    }

    @Override
    public AnswerList<String> readDistinctValuesByCriteria(String system, String searchParameter, Map<String, List<String>> individualSearch, String columnName) {
        return countryEnvParamLogDao.readDistinctValuesByCriteria(system, searchParameter, individualSearch, columnName);
    }

}
