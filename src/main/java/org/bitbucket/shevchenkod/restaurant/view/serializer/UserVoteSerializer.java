package org.bitbucket.shevchenkod.restaurant.view.serializer;

import org.bitbucket.shevchenkod.restaurant.model.UserVote;

import java.util.HashMap;
import java.util.Map;

public class UserVoteSerializer extends PublicViewSerializer<UserVote> {
	private final MenuSerializer menuSerializer;

	public UserVoteSerializer(MenuSerializer menuSerializer) {
		this.menuSerializer = menuSerializer;
	}

	@Override
    public Object serialize(UserVote value) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("date", value.getDate());
        result.put("vote", menuSerializer.serialize(value.getMenu()));
        return result;
    }
}