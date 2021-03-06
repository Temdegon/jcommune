/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.jcommune.test

import org.jtalks.common.model.entity.Component
import org.jtalks.jcommune.test.service.ComponentService
import org.jtalks.jcommune.test.service.GroupsService
import org.jtalks.jcommune.test.utils.Groups
import org.jtalks.jcommune.test.utils.Users
import org.jtalks.jcommune.test.utils.exceptions.WrongResponseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Ignore
import spock.lang.Specification

import static org.jtalks.jcommune.service.security.AdministrationGroup.*

/**
 * @author Oleg Tkachenko
 */
@WebAppConfiguration
@ContextConfiguration(locations = 'classpath:/org/jtalks/jcommune/web/view/test-configuration.xml')
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
class AdministrationControllerTest extends Specification {
    @Autowired Users users
    @Autowired Groups groups
    @Autowired GroupsService groupsService
    @Autowired ComponentService componentService
    private Component forum

    def setup() {
        groupsService.create()
        forum = componentService.createForumComponent()
    }

    def 'user without admin rights cannot see group administration page'() {
        given: 'user logged in but doesn`t have admin rights'
            def session = users.signInAsRegisteredUser(forum)
        when: 'user requests group administration page'
            groups.showGroupAdministrationPage(session)
        then: 'Access is denied'
            thrown(AccessDeniedException)
    }

    def 'must not be able to see group administration page for not authenticated users'() {
        when: 'anonymous user requests group administration page'
            groups.showGroupAdministrationPage(session = null)
        then: 'The session is missing, perform login'
            thrown(MissingPropertyException)
    }

    def 'user without admin rights cannot create groups'() {
        given: 'user logged in but doesn`t have admin rights'
            def session = users.signInAsRegisteredUser(forum)
        when: 'User attempts to create group'
            def groupDto = Groups.randomDto()
            groups.create(groupDto, session)
        then: 'Access is denied'
            thrown(AccessDeniedException)
    }

    def 'only user with admin rights can create group'() {
        given: 'User logged in and has admin rights'
            def session = users.signInAsAdmin(forum)
        when: 'User attempts to create group'
            def groupDto = Groups.randomDto()
            groups.create(groupDto,session)
        then: 'Group is created'
            groups.isExist(groupDto.name)
    }

    /**
     * Next tests will be ignored since we don't have such logic.
     */
    def 'user without admin rights cannot edit groups'() {
        given: 'user logged in but doesn`t have admin rights, random group created'
            def session = users.signInAsRegisteredUser(forum)
            def savedGroupId = groupsService.save(Groups.random())
        when: 'User attempts to edit an existing group'
            def groupDto = Groups.randomDto(id: savedGroupId)
            groups.edit(groupDto, session)
        then: 'Access is denied'
            thrown(AccessDeniedException)
    }

    def 'only user with admin rights can edit editable group'() {
        given: 'User logged in and has admin rights, random group created'
            def session = users.signInAsAdmin(forum)
            def savedGroupId = groupsService.save(Groups.random())
        when: 'User attempts to edit an existing editable group'
            def groupDto = Groups.randomDto(id: savedGroupId)
            groups.edit(groupDto, session)
        then: 'Group successfully edited'
            groups.isExist(groupDto.name)
            savedGroupId == groupsService.getIdByName(groupDto.name)
    }

    def 'must not be able to edit not existing group'() {
        given: 'User logged in and has admin rights, random group created'
            def session = users.signInAsAdmin(forum)
            def savedGroupId = groupsService.save(Groups.random())
        when: 'User attempts to edit not existing group'
            def groupDto = Groups.randomDto(id: savedGroupId + 1)
            groups.edit(groupDto, session)
        then: 'Group not found, error returned'
            thrown(WrongResponseException)
    }

    def 'must not be able to edit not editable group'() {
        given: 'User logged in and has admin rights'
            def session = users.signInAsAdmin(forum)
        when: 'User attempts to edit not editable group'
            def groupId = groupsService.getIdByName(notEditable.name)
            def groupDto = Groups.randomDto(id: groupId)
            groups.edit(groupDto, session)
        then: 'Validation error'
            thrown(WrongResponseException)
        and: 'Group is not edited'
            groups.assertDoesNotExist(groupDto.name)
        where: 'notEditable - list of not editable group names'
            notEditable << [ADMIN, USER, BANNED_USER]
    }
}
