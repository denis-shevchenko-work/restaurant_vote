package org.bitbucket.shevchenkod.restaurant.service.repository;

import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.User;
import org.bitbucket.shevchenkod.restaurant.model.UserVote;
import org.bitbucket.shevchenkod.restaurant.util.Pair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserVoteRepository extends JpaRepository<UserVote, Long> {
	/**
	 * Find user vote between dates. This method will be translated into a query using the
	 * {@link javax.persistence.NamedQuery} annotation at the {@link User} class.
	 *
	 * @param user
	 * @param from
	 * @param to
	 * @return
	 */
	Optional<UserVote> findForUserBetweenDates(User user, Date from, Date to);

	/**
	 * Collect vote results between dates. This method will be translated into a query using the
	 * {@link javax.persistence.NamedQuery} annotation at the {@link User} class.
	 * @param from
	 * @param to
	 * @return
	 */
	List<Object[]> collectResultsBetweenDates(Date from, Date to);
}
