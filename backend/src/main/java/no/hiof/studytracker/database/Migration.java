package no.hiof.studytracker.database;

import java.sql.Connection;
import java.sql.SQLException;

public class Migration {
    private String version;
    private String sql;

    public Migration(String version, String sql) {
        this.version = version;
        this.sql = sql;
    }

    public Migration() {}

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
