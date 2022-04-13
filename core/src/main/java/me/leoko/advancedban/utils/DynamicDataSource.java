package me.leoko.advancedban.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.leoko.advancedban.MethodInterface;
import me.leoko.advancedban.Universal;

public class DynamicDataSource {

    private HikariConfig config = new HikariConfig();

    public DynamicDataSource(boolean preferMySQL) throws ClassNotFoundException {
        MethodInterface mi = Universal.get().getMethods();
        if (preferMySQL) {
            Class.forName("com.mysql.jdbc.Driver");
            config.setJdbcUrl(mi.getString(mi.getMySQLFile(), "jdbc.url", "Unknown"));
            config.setUsername(mi.getString(mi.getMySQLFile(), "jdbc.user", "Unknown"));
            config.setPassword(mi.getString(mi.getMySQLFile(), "jdbc.password", "Unknown"));
        } else {
            // No need to worry about relocation because the maven-shade-plugin also changes strings
            String driverClassName = "org.hsqldb.jdbc.JDBCDriver";
            Class.forName(driverClassName);
            config.setDriverClassName(driverClassName);
            config.setJdbcUrl("jdbc:hsqldb:file:" + mi.getDataFolder().getPath() + "/data/storage;hsqldb.lock_file=false");
            config.setUsername("SA");
            config.setPassword("");
        }
    }

    public HikariDataSource generateDataSource(){
        return new HikariDataSource(config);
    }
}
