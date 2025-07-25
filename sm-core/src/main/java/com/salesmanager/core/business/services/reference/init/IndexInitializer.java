//package com.salesmanager.core.business.services.reference.init;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.Statement;
//
//@Component
//public class IndexInitializer {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @PostConstruct
//    public void createConditionalIndex() {
//    	String createNewIndex = """
//    		    BEGIN
//    		      EXECUTE IMMEDIATE '
//    		        CREATE UNIQUE INDEX SALESMANAGERS.UK_CUSTOMER_NICK_IDX 
//    		        ON SALESMANAGERS.CUSTOMER (
//    		          MERCHANT_ID, 
//    		          NVL(CUSTOMER_NICK, ''##NULL##'')
//    		        )
//    		      ';
//    		    EXCEPTION
//    		      WHEN OTHERS THEN
//    		        IF SQLCODE != -955 THEN
//    		          RAISE;
//    		        END IF;
//    		    END;
//    		""";
//
//
//        try (Connection conn = dataSource.getConnection();
//             Statement stmt = conn.createStatement()) {
//            
//            stmt.execute(createNewIndex);
//            System.out.println("✅ Created new conditional index UK_CUSTOMER_NICK_IDX.");
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("❌ Failed to manage index: " + e.getMessage());
//        }
//    }
//}
