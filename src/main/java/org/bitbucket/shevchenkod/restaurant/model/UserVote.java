package org.bitbucket.shevchenkod.restaurant.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by d_shevchenko on 11.12.2015.
 */
@Entity
@Table(name = "user_vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id","user_id","date"})})
@NamedQueries({
		@NamedQuery(name = "UserVote.findForUserBetweenDates", query = "SELECT userVote FROM UserVote userVote WHERE userVote.date > ?2 AND userVote.date < ?3 AND userVote.user = ?1"),
		@NamedQuery(name = "UserVote.collectResultsBetweenDates", query = "SELECT DISTINCT m, count(u) FROM UserVote u LEFT JOIN u.menu m WHERE u.date > ?1 AND u.date < ?2 GROUP BY m ORDER BY count(u) ")
})
@Getter
@Setter
@ToString
public class UserVote extends BasePersistable implements Serializable {

	private static final long serialVersionUID = -639631642012898515L;

	@ManyToOne
	@JoinColumn(name="menu_id")
	private Menu menu;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	@Column(name = "date")
	private Date date;


}
