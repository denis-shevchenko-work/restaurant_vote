package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.User;
import org.bitbucket.shevchenkod.restaurant.model.UserVote;
import org.bitbucket.shevchenkod.restaurant.util.Pair;
import org.bitbucket.shevchenkod.restaurant.view.dto.UserVoteDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Transactional
public interface UserVoteService extends CrudService<Long, UserVote>{

	/**
	 * Find vote for specified day.  Day starts from 11AM.
	 * @param user
	 * @param date
	 * @return
	 */
	Optional<UserVote> findForUserByDate(User user, Date date);

	/**
	 * Create new or update existing vote for specified day. Day starts from 11AM.
	 * @param user
	 * @param menu
	 * @param time
	 * @return
	 */
	UserVote createOrUpdateVote(User user, Menu menu, Date time);

	/**
	 * Find vote for today.  Day starts from 11AM.
	 * @param user
	 * @return
	 */
	Optional<UserVote> findForUserForToday(User user);


	List<Object[]> collectResultsForToday();
}
