package apoc.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import static apoc.util.TestUtil.testCall;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;

public class XmlTest {

    public static final String XML_AS_NESTED_MAP =
            "{_type=parent, name=databases, " +
                    "_children=[" +
                    "{_type=child, name=Neo4j, _text=Neo4j is a graph database}, " +
                    "{_type=child, name=relational, _children=[" +
                      "{_type=grandchild, name=MySQL, _text=MySQL is a relational database}, " +
                      "{_type=grandchild, name=Postgres, _text=Postgres is a relational database}]}]}";
    private GraphDatabaseService db;
	@Before public void setUp() throws Exception {
	    db = new TestGraphDatabaseFactory().newImpermanentDatabase();
	    TestUtil.registerProcedure(db, Xml.class);
	}
    @After public void tearDown() {
	    db.shutdown();
    }

    @Test public void testLoadXml() throws Exception {
		testCall(db, "CALL apoc.util.loadXml('file:databases.xml')", //  YIELD value RETURN value
                (row) -> {
                    Object value = row.get("value");
                    assertEquals(XML_AS_NESTED_MAP, value.toString());
                });
    }
}