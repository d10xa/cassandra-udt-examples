package example1;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class Example1Test {
    @Test
    public void test() {
        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Session session = cluster.newSession();
        UUID companyId = UUID.randomUUID();
        List<Company> companies;
        try {
            session.execute("DROP KEYSPACE IF EXISTS ks");
            session.execute("CREATE KEYSPACE ks WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };");
            session.execute("use ks");
            session.execute("CREATE TYPE address (street text, zip_code int);");
            session.execute("CREATE TABLE company (company_id uuid PRIMARY KEY, name text, address address);");
            MappingManager mappingManager = new MappingManager(session);
            Mapper<Company> mapper = mappingManager.mapper(Company.class);

            Company company = new Company(
                    companyId,
                    "company name",
                    new Address("street", 100000)
            );
            mapper.save(company);
            ResultSet resultSet = session.execute("select * from company");
            companies = mapper.map(resultSet).all();
        } finally {
            session.close();
            cluster.close();
        }

        Assert.assertEquals(companies.size(), 1);
        Assert.assertEquals(
                new Company(
                        companyId,
                        "company name",
                        new Address("street", 100000)
                ),
                companies.get(0)
        );
    }
}
