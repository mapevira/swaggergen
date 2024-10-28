package com.mapfre.tron.api.swaggergen.repository;

import com.mapfre.tron.api.swaggergen.entity.Property;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for accessing Property data from an Oracle database.
 * <p>
 * This class provides methods for executing SQL queries using {@link JdbcTemplate}
 * to retrieve property descriptions based on specific criteria.
 * <p>
 *
 * @author architecture - rperezv
 * @version 25/10/2024 - 12:48
 * @since jdk 1.17
 */
@Repository
@RequiredArgsConstructor
public class OracleRepository {

    /**
     * {@link JdbcTemplate} instance used for executing SQL queries.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Finds a list of {@link Property} entities by matching the provided property name.
     * <p>
     * Executes a query on the database to retrieve property descriptions
     * based on the specified name and locale.
     *
     * @param name the name of the property to search for
     * @return a list of {@link Property} entities containing the property name and description
     */
    @Cacheable("descriptionByProperty")
    public List<Property> findDescriptionByProperty(final String name) {

        // SQL query to retrieve property description based on locale and property name
        final String sql = new StringBuilder()
                .append("SELECT")
                .append("    t.prp_idn          AS property,")
                .append("    initcap(s.lng_lgc) AS description ")
                .append("FROM")
                .append("    t_dvl_trn_d_prp         t,")
                .append("    TABLE ( prp_lng_dsp_t ) s ")
                .append("WHERE")
                .append("        s.lng_idn = 'en_US'")
                .append("    AND t.prp_idn = ?")
                .append("    AND s.lng_lgc IS NOT NULL")
                .append("    AND upper(s.lng_lgc) != 'NULL' ")
                .append("GROUP BY")
                .append("    t.prp_idn,")
                .append("    s.lng_lgc")
                .toString();

        // Executes the SQL query with the specified property name
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> Property.builder()
                        .name(rs.getString("property"))
                        .description(rs.getString("description"))
                        .build(),
                this.getPlyPropertyName(name));
    }

    private String getPlyPropertyName(String input) {
        StringBuilder formattedString = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            // Check if character is uppercase and it's not the first character
            if (Character.isUpperCase(currentChar) && i != 0) {
                formattedString.append('_');
            }

            // Append the current character
            formattedString.append(currentChar);
        }

        return formattedString.toString().toUpperCase(); // Convert the result to uppercase
    }

}
