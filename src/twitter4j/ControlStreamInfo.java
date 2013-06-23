/*
 * Copyright 2007 Yusuke Yamamoto
 * Copyright (C) 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j;

import static twitter4j.internal.util.InternalParseUtil.getBoolean;
import static twitter4j.internal.util.InternalParseUtil.getRawString;

import java.io.Serializable;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Yusuke Yamamoto - yusuke at twitter.com
 * @since Twitter4J 2.2.6
 */
public final class ControlStreamInfo implements Serializable {
	private static final long serialVersionUID = -6761659771734942177L;
	private final StreamController.User[] users;
	private final boolean includeFollowingsActivity;
	private final boolean includeUserChanges;
	private final String replies;
	private final String with;
	private transient final StreamController controller;

	/* package */ControlStreamInfo(final StreamController controller, final JSONObject json) throws TwitterException {
		this.controller = controller;
		try {
			final JSONObject info = json.getJSONObject("info");
			includeFollowingsActivity = getBoolean("include_followings_activity", info);
			includeUserChanges = getBoolean("include_user_changes", info);
			replies = getRawString("replies", info);
			with = getRawString("with", info);
			final JSONArray usersJSON = info.getJSONArray("users");
			users = new StreamController.User[usersJSON.length()];
			for (int i = 0; i < usersJSON.length(); i++) {
				users[i] = this.controller.createUser(usersJSON.getJSONObject(i));
			}

		} catch (final JSONException e) {
			throw new TwitterException(e);
		}

	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final ControlStreamInfo that = (ControlStreamInfo) o;

		if (includeFollowingsActivity != that.includeFollowingsActivity) return false;
		if (includeUserChanges != that.includeUserChanges) return false;
		if (replies != null ? !replies.equals(that.replies) : that.replies != null) return false;
		if (!Arrays.equals(users, that.users)) return false;
		if (with != null ? !with.equals(that.with) : that.with != null) return false;

		return true;
	}

	public StreamController.User[] getUsers() {
		return users;
	}

	@Override
	public int hashCode() {
		int result = users != null ? Arrays.hashCode(users) : 0;
		result = 31 * result + (includeFollowingsActivity ? 1 : 0);
		result = 31 * result + (includeUserChanges ? 1 : 0);
		result = 31 * result + (replies != null ? replies.hashCode() : 0);
		result = 31 * result + (with != null ? with.hashCode() : 0);
		return result;
	}

	public boolean isIncludeFollowingsActivity() {
		return includeFollowingsActivity;
	}

	public boolean isIncludeUserChanges() {
		return includeUserChanges;
	}

	public String isReplies() {
		return replies;
	}

	public String isWith() {
		return with;
	}

	@Override
	public String toString() {
		return "ControlStreamInfo{" + "users=" + (users == null ? null : Arrays.asList(users))
				+ ", includeFollowingsActivity=" + includeFollowingsActivity + ", includeUserChanges="
				+ includeUserChanges + ", replies='" + replies + '\'' + ", with='" + with + '\'' + '}';
	}
}