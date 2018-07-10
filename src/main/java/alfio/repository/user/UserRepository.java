/**
 * This file is part of alf.io.
 *
 * alf.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alf.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alf.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package alfio.repository.user;

import alfio.model.user.User;
import ch.digitalfondue.npjt.*;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@QueryRepository
public interface UserRepository {

    @Query("SELECT * FROM ba_user WHERE id = :userId")
    User findById(@Bind("userId") int userId);

    @Query("select * from ba_user where id in (:userIds)")
    List<User> findByIds(@Bind("userIds") Collection<Integer> ids);

    @Query("select * from ba_user where username = :username")
    User getByUsername(@Bind("username") String username);

    @Query("select * from ba_user where username = :username")
    Optional<User> findByUsername(@Bind("username") String username);

    @Query("select id from ba_user where username = :username")
    Optional<Integer> findIdByUserName(@Bind("username") String username);

    @Query("select * from ba_user where username = :username and enabled = true")
    Optional<User> findEnabledByUsername(@Bind("username") String username);

    @Query("select password from ba_user where username = :username and enabled = true")
    Optional<String> findPasswordByUsername(@Bind("username") String username);

    @Query("INSERT INTO ba_user(username, password, first_name, last_name, email_address, enabled, user_type, valid_to) VALUES"
            + " (:username, :password, :first_name, :last_name, :email_address, :enabled, :userType, :validTo)")
    @AutoGeneratedKey("id")
    AffectedRowCountAndKey<Integer> create(@Bind("username") String username, @Bind("password") String password,
                                           @Bind("first_name") String firstname, @Bind("last_name") String lastname,
                                           @Bind("email_address") String emailAddress, @Bind("enabled") boolean enabled, @Bind("userType") User.Type userType,
                                           @Bind("validTo") ZonedDateTime validTo);

    @Query("update ba_user set username = :username, first_name = :firstName, last_name = :lastName, email_address = :emailAddress where id = :id")
    int update(@Bind("id") int id, @Bind("username") String username, @Bind("firstName") String firstName, @Bind("lastName") String lastName,
               @Bind("emailAddress") String emailAddress);

    @Query("update ba_user set enabled = :enabled where id = :id")
    int toggleEnabled(@Bind("id") int id, @Bind("enabled") boolean enabled);

    @Query("update ba_user set password = :password where id = :id")
    int resetPassword(@Bind("id") int id, @Bind("password") String newPassword);

    @Query("delete from sponsor_scan where user_id = :id")
    int deleteUserFromSponsorScan(@Bind("id") int id);

    @Query("delete from j_user_organization where user_id = :id")
    int deleteUserFromOrganization(@Bind("id") int id);

    @Query("delete from ba_user where id = :id")
    int deleteUser(@Bind("id") int id);

    @Query("update ba_user set enabled = false where user_type = :type and enabled = true and user_creation_time < :date")
    int disableAccountsOlderThan(@Bind("date") Date date, @Bind("type") User.Type type);

    @Query("select id from ba_user where user_type = :type and enabled = true and user_creation_time < :date")
    List<Integer> findUserToDisableOlderThan(@Bind("date") Date date, @Bind("type") User.Type type);
}
