
package middleware.externalinterfaces;

import java.sql.ResultSet;

import middleware.exceptions.DatabaseException;

/** All concrete dbclasses implement this interface */
public interface DbClass {
	public void buildQuery() throws DatabaseException;

	public void populateEntity(ResultSet resultSet) throws DatabaseException;

	public String getDbUrl();

	public String getQuery();
}
