import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

@MultipartConfig()
public class UploadServlet extends HttpServlet {
    private final static Logger log = Logger.getLogger(UploadServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String message;  // message to be sent back to client

        try {
            JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
            if (!isTableExist("PURCHASES", jdbc)) // create table if not exists
                createTable(jdbc);

            // read file contents
            Part part = request.getPart("filename");
            Scanner scanner = new Scanner(part.getInputStream()); // text scanner
            double totalGross = 0.0d;
            boolean dataFound = false;
            if (scanner.hasNextLine())
                scanner.nextLine(); // skip over header line
            while (scanner.hasNextLine()) {
                dataFound = true;
                String line = scanner.nextLine();
                String splitLine[] = line.split("\t", -1); // split row by tab delimiter
                double itemPrice = Double.parseDouble(splitLine[2]); // item_price
                int purchaseCount = Integer.parseInt(splitLine[3]); // purchase count
                totalGross += (itemPrice * purchaseCount); // calculate total gross revenue for this file input
                jdbc.update("insert into purchases " +
                        "(purchaser_name, item_description, item_price, purchase_count, merchant_address, merchant_name) " +
                        "values (?,?,?,?,?,?)",
                        splitLine[0], splitLine[1], itemPrice, purchaseCount, splitLine[4], splitLine[5]);
            }
            scanner.close();
            List<Map<String, Object>> purchases = jdbc.queryForList("select * from purchases");

            message = (dataFound ? "File uploaded and imported successfully into the database.<br>" +
                    "Total amount of gross revenue in the file currently imported is: " + totalGross : "No new data uploaded.") +
                    "<br><br><br>" +
                    (purchases.size() > 0 ? "Purchases:<br>" : "") +
                    StringUtils.collectionToDelimitedString(purchases, "<br>");

        } catch (DataAccessException ex) {
            message = "ERROR: Data access failure: " + ex.toString();
            log.severe(message);
        } catch (ArrayIndexOutOfBoundsException ex) {
            message = "ERROR: Invalid data file format: " + ex.toString();
            log.severe(message);
        }

        request.setAttribute("message", message);
        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private static DriverManagerDataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    private boolean isTableExist(String tableName, JdbcTemplate jdbc) throws DataAccessException {
        try {
            DatabaseMetaData metadata = jdbc.getDataSource().getConnection().getMetaData();
            return metadata.getTables(null, null, tableName, null).next();
        } catch (SQLException e) {
            throw new UncategorizedSQLException("table exists", "table name", e);
        }
    }

    private void createTable(JdbcTemplate jdbc) {
        String sql = "create table purchases (" +
                "purchaser_name VARCHAR(200), " +
                "item_description VARCHAR(200), " +
                "item_price INTEGER, " +
                "purchase_count DECIMAL (10,2), " +
                "merchant_address VARCHAR(200), " +
                "merchant_name VARCHAR(200))";

        jdbc.execute(sql);
    }

}