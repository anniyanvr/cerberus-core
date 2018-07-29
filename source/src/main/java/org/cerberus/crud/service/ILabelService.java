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
package org.cerberus.crud.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cerberus.crud.entity.Label;
import org.cerberus.dto.TreeNode;
import org.cerberus.exception.CerberusException;
import org.cerberus.util.answer.Answer;
import org.cerberus.util.answer.AnswerItem;
import org.cerberus.util.answer.AnswerList;

/**
 *
 * @author bcivel
 */
public interface ILabelService {

    /**
     *
     * @param id
     * @return
     */
    AnswerItem readByKey(Integer id);

    /**
     *
     * @return
     */
    AnswerList readAll();

    /**
     *
     * @param system
     * @return
     */
    AnswerList readBySystem(List<String> system);

    /**
     *
     * @param system
     * @param type
     * @return
     */
    AnswerList readByVarious(List<String> system, List<String> type);

    /**
     *
     * @param startPosition
     * @param length
     * @param columnName
     * @param sort
     * @param searchParameter
     * @param individualSearch
     * @return
     */
    AnswerList readByCriteria(int startPosition, int length, String columnName, String sort, String searchParameter, Map<String, List<String>> individualSearch);

    /**
     *
     * @param system
     * @param type
     * @param startPosition
     * @param length
     * @param columnName
     * @param sort
     * @param searchParameter
     * @param individualSearch
     * @return
     */
    AnswerList readByVariousByCriteria(List<String> system, List<String> type, int startPosition, int length, String columnName, String sort, String searchParameter, Map<String, List<String>> individualSearch);

    /**
     *
     * @param id
     * @return true is label exist or false is label does not exist in database.
     */
    boolean exist(Integer id);

    /**
     *
     * @param object
     * @return
     */
    Answer create(Label object);

    /**
     *
     * @param object
     * @return
     */
    Answer delete(Label object);

    /**
     *
     * @param object
     * @return
     */
    Answer update(Label object);

    /**
     *
     * @param answerItem
     * @return
     * @throws CerberusException
     */
    Label convert(AnswerItem<Label> answerItem) throws CerberusException;

    /**
     *
     * @param answerList
     * @return
     * @throws CerberusException
     */
    List<Label> convert(AnswerList<Label> answerList) throws CerberusException;

    /**
     *
     * @param answer
     * @throws CerberusException
     */
    void convert(Answer answer) throws CerberusException;

    /**
     *
     * @param system
     * @param searchParameter
     * @param individualSearch
     * @param columnName
     * @return
     */
    public AnswerList<List<String>> readDistinctValuesByCriteria(String system, String searchParameter, Map<String, List<String>> individualSearch, String columnName);

    /**
     *
     * @param inputList
     * @return
     */
    public List<TreeNode> hierarchyConstructor(HashMap<Integer, TreeNode> inputList);
}
