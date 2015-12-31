package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.User;
import org.bitbucket.shevchenkod.restaurant.model.UserVote;
import org.bitbucket.shevchenkod.restaurant.service.repository.UserVoteRepository;
import org.bitbucket.shevchenkod.restaurant.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
@Transactional
public class UserVoteServiceImpl extends GenericCrudService<Long, UserVote, UserVoteRepository> implements UserVoteService {

	@Autowired
	MenuService menuService;

	/**
	 * Create or update vote for specified day. Day start end finish at 11AM.
	 * @param user
	 * @param menu
	 * @param date
	 * @return
	 */
	@Override
	public UserVote createOrUpdateVote(User user, Menu menu, Date date) {

		Optional<UserVote> userVoteOptional = findForUserByDate(user, date);

		UserVote userVote = null;
		if (userVoteOptional.isPresent()) {
			userVote = userVoteOptional.get();
		} else {
			userVote = new UserVote();
		}
		userVote.setUser(user);
		userVote.setMenu(menu);
		userVote.setDate(date);
		return create(userVote);
	}

	/**
	 * Retrieve user vote  for specified day. Day start end finish at 11AM.
	 * @param user
	 * @param date
	 * @return
	 */
	@Override
	public Optional<UserVote> findForUserByDate(User user, Date date) {
		Pair<Date, Date> range = getRange(date);
		return getRepository().findForUserBetweenDates(user, range.getLeft(), range.getRight());
	}

	@Override
	public Optional<UserVote> findForUserForToday(User user) {
		return findForUserByDate(user, new Date());
	}

	/**
	 * Vote results starting from 11AM.
	 * @return
	 */
	@Override
	public List<Object[]> collectResultsForToday() {
		Date date = new Date();
		Pair<Date, Date> range = getRange(date);
		return getRepository().collectResultsBetweenDates(range.getLeft(), range.getRight());
	}

	private Pair<Date,Date> getRange(Date date){
		Date from;
		Date to;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 11);

		if (date.compareTo(calendar.getTime()) < 0) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}

		from = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		to = calendar.getTime();
		return new Pair<>(from, to);
	}

}
